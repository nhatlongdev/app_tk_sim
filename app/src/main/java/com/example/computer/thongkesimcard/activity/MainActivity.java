package com.example.computer.thongkesimcard.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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

import com.example.computer.thongkesimcard.R;
import com.example.computer.thongkesimcard.configs.GlobalValue;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

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
        btnTkSim.setOnClickListener(this);
        btnUpdateSim.setOnClickListener(this);

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
                btnYes = dialogUpdateSim.findViewById(R.id.btn_yes);
                btnNo = dialogUpdateSim.findViewById(R.id.btn_no);
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
            }
        }
    };

    /*ham check chuoi tra ve thoa man dieu kien*/
    public boolean checkStrinhResult(String text){
        if(text.contains("TK chinh=") || text.contains("Nap the thanh cong (Successful refill)") || text.contains("da duoc su dung")
                || text.contains("The nap khong dung") || text.contains("TK goc la") || text.contains("Tai khoan cua Quy khach la")
                || text.contains("so the cao khong hop le")){
            return true;
        }else {
            return false;
        }
    }

    /*ham check chuoi tra ve thoa man khi goi 101*/
    public boolean checkStringResult101(String text){
        if(text.contains("TK chinh=") || text.contains("TK goc la")){
            return true;
        }else {
            return false;
        }
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
