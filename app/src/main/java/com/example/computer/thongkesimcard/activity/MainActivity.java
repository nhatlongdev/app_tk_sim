package com.example.computer.thongkesimcard.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.computer.thongkesimcard.R;
import com.example.computer.thongkesimcard.configs.Apis;
import com.example.computer.thongkesimcard.configs.GlobalValue;
import com.example.computer.thongkesimcard.modelmanager.RequestQueueSingleton;
import com.example.computer.thongkesimcard.util.AppUtil;
import com.example.computer.thongkesimcard.util.SetupHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public Button btnTkSim, btnUpdateSim;
    public Dialog dialogUpdateSim, dialog;
    public TextView tvSumMoneyToday, tvSumSimToday, tvSumMoneyYesterday, tvSumSimYesterday, tvSumMoneyThisMonth, tvSumSimThisMonth, tvSumMoneyLastMoth, tvSumSimLastMonth, tvMsgError;
    public LinearLayout liInforSum;
    public ProgressBar proBar;
    public ImageView imgRefresh;

    // queue request server
    public RequestQueue requestQueue;

    /*biến kiểm tra nhấn back 2 lần*/
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        requestQueue = RequestQueueSingleton.getInstance(this).getRequestQueue();
        /*get infor sim, phone*/
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 109);
        } else {
            //TODO
            detectSimAndCreateJsonData();
        }

        //tham chiếu
        btnTkSim = findViewById(R.id.btn_tk_sim);
        btnUpdateSim = findViewById(R.id.btn_update_tk_sim);
        tvSumMoneyToday = findViewById(R.id.tv_sum_money_today);
        tvSumSimToday = findViewById(R.id.tv_sum_sim_today);
        tvSumMoneyYesterday = findViewById(R.id.tv_sum_money_yesterday);
        tvSumSimYesterday = findViewById(R.id.tv_sum_sim_yesterday);
        tvSumMoneyThisMonth = findViewById(R.id.tv_sum_money_this_month);
        tvSumSimThisMonth = findViewById(R.id.tv_sum_sim_this_month);
        tvSumMoneyLastMoth = findViewById(R.id.tv_sum_money_last_month);
        tvSumSimLastMonth = findViewById(R.id.tv_sum_sim_last_month);
        tvMsgError = findViewById(R.id.msg_error);
        liInforSum = findViewById(R.id.li_infor_sum);
        proBar = findViewById(R.id.pro_bar);
        imgRefresh = findViewById(R.id.img_refresh);

        //GET INFOR SUM TK
        getInforStatisticsSum();

        btnTkSim.setOnClickListener(this);
        btnUpdateSim.setOnClickListener(this);
        imgRefresh.setOnClickListener(this);

        /*on Broastcash*/
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessgeResultText, new IntentFilter("RESULT_TEXT"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_tk_sim:
                AppUtil.startActivity(this,ThongKeSimActivity.class);
                break;
            case R.id.btn_update_tk_sim:
                dialogUpdateSim = new Dialog(this);
                dialogUpdateSim.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogUpdateSim.setContentView(R.layout.dialog_msg_update_sim);
                final Button btnYes, btnNo;
                TextView tvMsg = dialogUpdateSim.findViewById(R.id.tv_msg);
                btnYes = dialogUpdateSim.findViewById(R.id.btn_yes);
                btnNo = dialogUpdateSim.findViewById(R.id.btn_no);

                //set nội dung cho msg dialog
                if(GlobalValue.jsonSimSelected != null && GlobalValue.jsonSimSelected.optString("serial_number") != null){
                    tvMsg.setText("Bạn có muốn cập nhật tài khoản hiện tại cho sim " + GlobalValue.jsonSimSelected.optString("serial_number") + " không ?");
                }
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogUpdateSim.dismiss();
                        //GỌI *101#
                        processUpdateTkSim();
                    }
                });

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogUpdateSim.dismiss();
                    }
                });

                Window window_switch_mode = dialogUpdateSim.getWindow();
                window_switch_mode.setBackgroundDrawableResource(R.color.transparent);
                window_switch_mode.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
                window_switch_mode.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                WindowManager.LayoutParams wmlpMsgwindowSwitchMode = dialogUpdateSim.getWindow().getAttributes();
                wmlpMsgwindowSwitchMode.gravity = Gravity.CENTER;
                dialogUpdateSim.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialogUpdateSim.dismiss();
                    }
                });
                dialogUpdateSim.setCancelable(false);
                dialogUpdateSim.show();
                break;
            case R.id.img_refresh:
                //Gọi hàm getinforsum
                getInforStatisticsSum();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 109:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                    detectSimAndCreateJsonData();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
        }else {
            doubleBackToExitPressedOnce = true;
            Toast.makeText(this,"Vui lòng nhấn thêm lần nữa để thoát" , Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            Runnable myTask = new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            };
            handler.postDelayed(myTask,2000);
        }
    }

    /*call phone number*/
    public void callPhoneNumber()
    {
        try
        {
            if(Build.VERSION.SDK_INT > 22)
            {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 101);

                    return;
                }
                Log.d("inForSIm", "Chay vao API > 22 voi gia tri value = " + 101);
                String ussdCode = "";
                Log.d("datax", "Chay vao day");
                ussdCode = "*" + 101 + Uri.encode("#");

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + ussdCode));
                callIntent.putExtra("simSlot", 0); //For sim 1
                startActivity(callIntent);

            }else {
                Log.d("inForSIm", "Chay vao API < 22 voi gia tri value = " + 101);
                String ussdCode = "";
                ussdCode = "*" + 101 + Uri.encode("#");
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + ussdCode));
                callIntent.putExtra("simSlot", 0); //For sim 1
                startActivity(callIntent);

            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    //Hàm xử lý gọi *101#
    public void processUpdateTkSim(){
        if(checkAvailability() == true){
            callPhoneNumber();
        }else {
            dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_accessibility);
            Button btnYes = dialog.findViewById(R.id.btn_yes);
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetupHelper.openAccessibilitySettings(MainActivity.this);
                    dialog.dismiss();
                }
            });
            Window window = dialog.getWindow();
            window.setBackgroundDrawableResource(R.color.transparent);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            WindowManager.LayoutParams wmlpMsgwindow_the_loi = dialog.getWindow().getAttributes();
            wmlpMsgwindow_the_loi.gravity = Gravity.CENTER;
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.dismiss();
                }
            });
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    /*ham check quyen va app trong accessibility da on chua*/
    public boolean checkAvailability() {
        if (SetupHelper.isPermissionGranted(this) && SetupHelper.isAccessibilityServiceEnabled(this)) {
            /*ok thi lam gi*/
            return true;
        } else {
            return false;
        }
    }

    /*Broastcash activity*/
    private BroadcastReceiver mMessgeResultText = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String text = bundle.getString("result_text");
            Log.d("inForSIm", "CHECK RESULT NHAN VE tu BroadcastReceiver====>>>>>>>: " + text);
            if(text != null && !text.equals("") && checkStringResult101(text) == true){
                // LẤY ĐƯỢC DỮ LIỆU 101 TO DO
                Log.d("log_app",text);
                if(GlobalValue.jsonSimSelected != null){
                    try {
                        GlobalValue.jsonSimSelected.put("ussdRes1",text);
                        updateTkForSim(GlobalValue.jsonSimSelected);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    //Hàm định dạng tiền
    public String formatMoney(int money){
        final NumberFormat formatter = new DecimalFormat("###,###,###.##");
        String money_show = formatter.format(money);
        return  money_show;
    }

    /*ham check chuoi tra ve thoa man khi goi 101*/
    public boolean checkStringResult101(String text){
        if(text.contains("TK chinh=") || text.contains("TK goc la")){
            return true;
        }else {
            return false;
        }
    }

    public void  getInforStatisticsSum(){
        proBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Apis.URL_GET_INFOR_SUM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //TO DO
                        Log.d("app_tk",response.toString());
                        try {
                            JSONObject jsonRes = new JSONObject(response);
                            if(jsonRes!= null && jsonRes.optBoolean("s") == true){
                                //OK TO DO
                                if(jsonRes.optJSONObject("res")!= null){
                                    tvMsgError.setVisibility(View.INVISIBLE);
                                    liInforSum.setVisibility(View.VISIBLE);
                                    setValueTextForView(jsonRes.optJSONObject("res"));
                                }
                                proBar.setVisibility(View.INVISIBLE);
                            }else {
                                //FAILSE
                                if(jsonRes.optJSONObject("error")!=null){
                                    tvMsgError.setVisibility(View.VISIBLE);
                                    liInforSum.setVisibility(View.INVISIBLE);
                                    tvMsgError.setText(jsonRes.optJSONObject("error").optString("text"));
                                }
                                proBar.setVisibility(View.INVISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //ERROR KO LẤY ĐƯỢC DATA TỪ SERVER
                Log.d("app_tk",error.toString());
                tvMsgError.setVisibility(View.VISIBLE);
                liInforSum.setVisibility(View.INVISIBLE);
                proBar.setVisibility(View.INVISIBLE);
            }
        });
        stringRequest.setTag("APP_TK");
        requestQueue.add(stringRequest);
    }

    /* ham update tài khoản cho sim*/
    public void updateTkForSim(final JSONObject jsonParams){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("simNumber", jsonParams.optString("serial_number"));
            jsonObject.put("ussdRes1", jsonParams.optString("ussdRes1"));
            Log.d("log_app", "params: " + jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Apis.URL_UPDATE_TK_SIM, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        boolean status = response.optBoolean("s");
                        if(status == true){
                            //TO TO
                            Toast.makeText(MainActivity.this, "Cập nhật tài khoản sim thành công!", Toast.LENGTH_SHORT).show();
                        }else {
                            //FAILSE
                            if(response.optJSONObject("error") != null && response.optJSONObject("error").optString("text") != null){
                                Toast.makeText(MainActivity.this, response.optJSONObject("error").optString("text"), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //LỖI VOLLEY
                Toast.makeText(MainActivity.this, "Cập nhật tài khoản không thành công, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setTag("APP");
        requestQueue.add(jsonObjectRequest);
    }

    //Hàm set giá trị vào view khi gọi api get infor sum
    public void setValueTextForView(JSONObject jsonRes){
        tvSumMoneyToday.setText(formatMoney(jsonRes.optInt("sumOfToday")));
        tvSumSimToday.setText(formatMoney(jsonRes.optInt("countOfToday")));
        tvSumMoneyYesterday.setText(formatMoney(jsonRes.optInt("sumOfYesterday")));
        tvSumSimYesterday.setText(formatMoney(jsonRes.optInt("countOfYesterday")));
        tvSumMoneyThisMonth.setText(formatMoney(jsonRes.optInt("sumOfCurrentMonth")));
        tvSumSimThisMonth.setText(formatMoney(jsonRes.optInt("countOfCurrentMonth")));
        tvSumMoneyLastMoth.setText(formatMoney(jsonRes.optInt("sumOfPreviousMonth")));
        tvSumSimLastMonth.setText(formatMoney(jsonRes.optInt("countOfPreviousMonth")));
    }

    /*detect sim and json */
    public void detectSimAndCreateJsonData(){
        TelephonyManager telemamanger = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        GlobalValue.jsonDataSim = new JSONArray();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1){
            List<SubscriptionInfo> subscription = SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoList();
            JSONObject jsonSim = new JSONObject();
            for (int i = 0; i < subscription.size(); i++) {
                SubscriptionInfo info = subscription.get(i);
                // get serial number
                String serialNumber = info.getIccId();
                JSONObject jsonSim1 = new JSONObject();
                try {
                    jsonSim1.put("serial_number",serialNumber);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(i == 0){
                    if(info.getCarrierName().toString().toLowerCase().contains("Viettel".toLowerCase())){
                        try {
                            jsonSim1.put("code","VTEL");
                            jsonSim1.put("name","VIETTEL");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if(info.getCarrierName().toString().toLowerCase().contains("VINAPHONE".toLowerCase())){
                        try {
                            jsonSim1.put("code","GPC");
                            jsonSim1.put("name","VINAPHONE");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if(info.getCarrierName().toString().toLowerCase().contains("MOBIPHONE".toLowerCase())){
                        try {
                            jsonSim1.put("code","VMS");
                            jsonSim1.put("name","MOBIPHONE");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        jsonSim.put("sim1",jsonSim1);
                        DateFormat df1=new SimpleDateFormat("MM/yyyy");//foramt date
                        String date=df1.format(Calendar.getInstance().getTime());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    if(info.getCarrierName().toString().toLowerCase().contains("Viettel".toLowerCase())){
                        try {
                            jsonSim1.put("code","VTEL");
                            jsonSim1.put("name","VIETTEL");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if(info.getCarrierName().toString().toLowerCase().contains("VINAPHONE".toLowerCase())){
                        try {
                            jsonSim1.put("code","GPC");
                            jsonSim1.put("name","VINAPHONE");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if(info.getCarrierName().toString().toLowerCase().contains("MOBIPHONE".toLowerCase())){
                        try {
                            jsonSim1.put("code","VMS");
                            jsonSim1.put("name","MOBIPHONE");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        jsonSim.put("sim2",jsonSim1);
                        DateFormat df1=new SimpleDateFormat("MM/yyyy");//foramt date
                        String date=df1.format(Calendar.getInstance().getTime());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                GlobalValue.jsonDataSim.put(jsonSim1);

            }
        }else {
            JSONObject jsonSim = new JSONObject();
            Log.d("inForSIm", " Dattttaaaaaaaaaaaaaaaaa: " + telemamanger.getNetworkCountryIso());
            Log.d("inForSIm", " Dattttaaaaaaaaaaaaaaaaa: " + telemamanger.getNetworkOperatorName());
            Log.d("inForSIm", " Dattttaaaaaaaaaaaaaaaaa: " + telemamanger.getNetworkOperator());
            Log.d("inForSIm", " Dattttaaaaaaaaaaaaaaaaa: " + telemamanger.getSimSerialNumber());
            try {
                jsonSim.put("serial_number",telemamanger.getSimSerialNumber());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(telemamanger.getNetworkOperatorName().toString().toLowerCase().contains("Viettel".toLowerCase())){
                try {
                    jsonSim.put("code","VTEL");
                    jsonSim.put("name","VIETTEL");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(telemamanger.getNetworkOperatorName().toString().toLowerCase().contains("VINAPHONE".toLowerCase())){
                try {
                    jsonSim.put("code","GPC");
                    jsonSim.put("name","VINAPHONE");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(telemamanger.getNetworkOperatorName().toString().toLowerCase().contains("MOBIPHONE".toLowerCase())){
                try {
                    jsonSim.put("code","VMS");
                    jsonSim.put("name","MOBIPHONE");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            DateFormat df1=new SimpleDateFormat("MM/yyyy");//foramt date
            String date=df1.format(Calendar.getInstance().getTime());
            GlobalValue.jsonDataSim.put(jsonSim);
        }

        /*code chinh*/
        if(GlobalValue.jsonDataSim.length()>0) {
            GlobalValue.jsonSimSelected = GlobalValue.jsonDataSim.optJSONObject(0);
            Log.d("log_app",GlobalValue.jsonSimSelected.toString());
        }
    }
}
