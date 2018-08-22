package com.example.computer.thongkesimcard.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.computer.thongkesimcard.R;

public class ThongKeSimActivity extends AppCompatActivity implements View.OnClickListener{
    //Khai bao biến
    public ImageView imgBack;
    public Spinner spinnerOption;
    public RelativeLayout rltNhapSim;
    public LinearLayout liParent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tk_sim);
        getSupportActionBar().hide();
        //Tham chiếu
        imgBack = findViewById(R.id.img_back);
        rltNhapSim = findViewById(R.id.rlt_sim);
        liParent = findViewById(R.id.li_parent);
        liParent.removeView(rltNhapSim);
        spinnerOption = findViewById(R.id.spinner_option);
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
                }else {
                    liParent.removeView(rltNhapSim);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
        }
    }
}
