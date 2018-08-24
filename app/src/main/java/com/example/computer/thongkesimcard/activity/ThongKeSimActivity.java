package com.example.computer.thongkesimcard.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.computer.thongkesimcard.R;
import com.example.computer.thongkesimcard.adapter.ContactSimAdapter;
import com.example.computer.thongkesimcard.adapter.SimAdapter;
import com.example.computer.thongkesimcard.configs.Apis;
import com.example.computer.thongkesimcard.configs.GlobalValue;
import com.example.computer.thongkesimcard.listener.IOnItemClickedListener;
import com.example.computer.thongkesimcard.listener.OnLoadMoreListener;
import com.example.computer.thongkesimcard.modelmanager.RequestQueueSingleton;
import com.example.computer.thongkesimcard.obj.Sim;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThongKeSimActivity extends AppCompatActivity implements View.OnClickListener{
    //Khai bao biến
    public ImageView imgBack;
    public Spinner spinnerOption;
    public RelativeLayout rltNhapSim;
    public LinearLayout liParent;
    public Button btnSearch;
    public RecyclerView rcvListSim;
    public SimAdapter simAdapter;
    public JSONArray jsonData;
    public AutoCompleteTextView edtSim;
    public ProgressBar proBar;
    public List<Sim> listSim;
    public TextView tvSumSim;
    /*mảng option lọc danh sách sim*/
    public String arr[]={
            "Tất cả",
            "Sim đang nạp tiền",
            "Sim sẵn sàng mua IAP",
            "Sim đã mua IAP",
            "Sim chưa nạp tiền",
            "Sim đang bị khóa",
            "Xem chi tiết một sim",
    };

    // queue request server
    public RequestQueue requestQueue;

    public boolean isLoadMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tk_sim);
        getSupportActionBar().hide();
        listSim = new ArrayList<>();
        GlobalValue.arrSuggestNameSim = new ArrayList<>();
        requestQueue = RequestQueueSingleton.getInstance(this).getRequestQueue();
        //Tham chiếu
        imgBack = findViewById(R.id.img_back);
        rltNhapSim = findViewById(R.id.rlt_sim);
        liParent = findViewById(R.id.li_parent);
        liParent.removeView(rltNhapSim);
        spinnerOption = findViewById(R.id.spinner_option);
        btnSearch = findViewById(R.id.tv_search);
        edtSim = findViewById(R.id.edt_input_sim);
        proBar = findViewById(R.id.pr_bar);
        tvSumSim = findViewById(R.id.tv_sum_sim);
        jsonData = new JSONArray();
        //Gán Data source (arr) vào Adapter
        ArrayAdapter<String> adapter=new ArrayAdapter<String>
                (
                       this,
                        android.R.layout.simple_spinner_item,
                        arr
                );
        //phải gọi lệnh này để hiển thị danh sách cho Spinner
        adapter.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);
        //Thiết lập adapter cho Spinner
        spinnerOption.setAdapter(adapter);
        spinnerOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 6 ){
                    liParent.addView(rltNhapSim, 1);
                    edtSim = findViewById(R.id.edt_input_sim);
//                    //cập nhật list suggest
//                    setDataForSuggestAutoComplete();
                    try {
                        GlobalValue.jsonSimSelected.put("code",i+"");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    liParent.removeView(rltNhapSim);
                    if(GlobalValue.jsonSimSelected != null){
                        if(i == 0){
                            try {
                                GlobalValue.jsonSimSelected.put("code","-1");
                                GlobalValue.jsonSimSelected.put("text","");
                                GlobalValue.jsonSimSelected.put("page",1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            try {
                                GlobalValue.jsonSimSelected.put("code",i+"");
                                GlobalValue.jsonSimSelected.put("text","");
                                GlobalValue.jsonSimSelected.put("page",1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //RECYCLERVIEW
        rcvListSim = findViewById(R.id.rcv_list_sim);

        //GOI API FILTER
        filterSim(GlobalValue.jsonSimSelected);

        imgBack.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_search:
                if(GlobalValue.jsonSimSelected!= null){
                    try {
                        GlobalValue.jsonSimSelected.put("page",1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(jsonData != null){
                        jsonData = new JSONArray();
                        //notifi data
                        refreshListView();
                    }
                    if(!GlobalValue.jsonSimSelected.optString("code").equals("6")){
                        //GOI API FILTER
                        filterSim(GlobalValue.jsonSimSelected);
                    }else {
                        //kiểm tra xem định dạng nhập vào ô text đã đúng chưa nếu đúng thì mới thực hiện tìm kiếm
                        if(!checkInputText(edtSim.getText().toString()).equals("ok")){
                            Toast.makeText(this, checkInputText(edtSim.getText().toString()), Toast.LENGTH_SHORT).show();
                        }else {
                            String text = edtSim.getText().toString().substring(edtSim.getText().toString().length() - 5);
                            try {
                                GlobalValue.jsonSimSelected.put("text",text);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //GOI API FILTER
                            filterSim(GlobalValue.jsonSimSelected);
                        }
                    }
                }
                break;
        }
    }

    //Ham xu ly notifi list view
    public void refreshListView(){
        Log.d("log_app","jsonData: " + jsonData.toString());
        Log.d("log_app","jsonData.length: " + jsonData.length());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ThongKeSimActivity.this,LinearLayoutManager.VERTICAL,false);
        rcvListSim.setLayoutManager(linearLayoutManager);
        simAdapter = new SimAdapter(rcvListSim,this,jsonData, new IOnItemClickedListener() {
            @Override
            public void onItemClicked(int position, View view) {

            }
        });
        rcvListSim.setAdapter(simAdapter);
        simAdapter.notifyDataSetChanged();

        simAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if(GlobalValue.jsonSimSelected.optString("code").equals("-1")){
                    isLoadMore = true;
                    jsonData.put(null);
                    simAdapter.notifyItemInserted(jsonData.length() - 1);
                    try {
                        GlobalValue.jsonSimSelected.put("page",GlobalValue.jsonSimSelected.optInt("page")+1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Goi APi lay data
                    filterSim(GlobalValue.jsonSimSelected);
                }
            }
        });
    }

    //Hàm set suggest auto
    public void setDataForSuggestAutoComplete(){
        //set suggest cho autoComplete
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, GlobalValue.arrSuggestNameSim);
        edtSim.setThreshold(1);
        edtSim.setAdapter(arrayAdapter);
    }

    //Hàm kiểm tra định dạng nhập vào đã đúng chưa
    public String checkInputText(String text){
        String result = "ok";
        if(text.equals("")){
            result = "Bạn chưa nhập số sim cần tra cứu";
        }else {
            Pattern pattern = Pattern.compile("^[0-9]*$");
            Matcher matcher = pattern.matcher(text);
            if(!matcher.matches()){
                result = "Định dạng sim không đúng, vui lòng thử lại";
            }else if(text.length() > 20 && text.length() <5){
                result = "Độ dài chuỗi không hợp lệ, vui lòng kiểm tra lại";
            }
        }
        return result;
    }

    /* ham update tài khoản cho sim*/
    public void filterSim(final JSONObject jsonParams){
        if(isLoadMore != true){
            proBar.setVisibility(View.VISIBLE);
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("simNumber", jsonParams.optString("serial_number"));
            jsonObject.put("code", jsonParams.optString("code"));
            jsonObject.put("text", jsonParams.optString("text"));
            jsonObject.put("page", jsonParams.optInt("page"));
            jsonObject.put("limit", 20);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("log_app","PARAMS FILTER: " + jsonObject.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Apis.URL_FILTER_SIM, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        proBar.setVisibility(View.INVISIBLE);
                        boolean status = response.optBoolean("s");
                        if(status == true){
                            //TO TO
                            Log.d("log_app","FILTER SIM: " + response.toString());
                            if(response.optJSONObject("res") != null){
                                //set so luong sim cho text
                                tvSumSim.setText(response.optJSONObject("res").optInt("count")+"");

                                if(GlobalValue.jsonSimSelected.optString("code").equals("6") && response.optJSONObject("res").optInt("count") == 0){
                                    Toast.makeText(ThongKeSimActivity.this, "Không tìm thấy sim, vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
                                }
                                if(isLoadMore == true){
                                    jsonData.remove(jsonData.length()-1);
                                    simAdapter.notifyItemRemoved(jsonData.length());
                                    if(response.optJSONObject("res").optJSONArray("sims").length()>0){
                                        String jsonTam = jsonData.toString().replace("[","");
                                        String jsonDataTong = jsonTam.replace("]","");
                                        String jsonTamNew = response.optJSONObject("res").optJSONArray("sims").toString().replace("[","");
                                        String jsonDataLoadMore = jsonTamNew.replace("]","");
                                        String jsonDataTongNew = "[" + jsonDataTong + "," + jsonDataLoadMore +"]";
                                        try {
                                            jsonData = new JSONArray(jsonDataTongNew);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        //notifi data
                                        refreshListView();

                                        simAdapter.setLoaded();
                                        Log.d("log_app","JSON DATA: " + jsonData.toString());
                                        Log.d("log_app","CHay vao day: " + jsonData.length());
                                    }else {
                                        Log.d("log_app","HET DU LIEU" + jsonData.length());
                                    }

                                }else {
                                    try {
                                        Log.d("log_app","Data: " + response.optJSONObject("res").optJSONArray("sims").toString());
                                        jsonData = new JSONArray(response.optJSONObject("res").optJSONArray("sims").toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if(jsonData != null){
//                                        if(GlobalValue.arrSuggestNameSim.size() == 0){
//                                            pushElementToArrSuggest(jsonData);
//                                        }
                                        //notifi data
                                        refreshListView();
                                    }
                                }
                                isLoadMore = false;
                            }else {
                                tvSumSim.setText("0");
                            }
                        }else {
                            //FAILSE
                            if(response.optJSONObject("error") != null && response.optJSONObject("error").optString("text") != null){
                                Toast.makeText(ThongKeSimActivity.this, response.optJSONObject("error").optString("text"), Toast.LENGTH_SHORT).show();
                            }
                            tvSumSim.setText("0");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                proBar.setVisibility(View.INVISIBLE);
                //LỖI VOLLEY
                Toast.makeText(ThongKeSimActivity.this, "Không tải được dữ liệu, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                tvSumSim.setText("0");
            }
        });
        jsonObjectRequest.setTag("APP");
        requestQueue.add(jsonObjectRequest);
    }

    //Hàm push element name sim to arr suggest
    public void pushElementToArrSuggest(JSONArray jsonArr){
        GlobalValue.arrSuggestNameSim.clear();
        for (int i=0; i<jsonArr.length(); i++){
            GlobalValue.arrSuggestNameSim.add(jsonArr.optJSONObject(i).optString("simNumber"));
        }
    }
}
