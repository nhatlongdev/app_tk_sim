package com.example.computer.thongkesimcard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.computer.thongkesimcard.R;
import com.example.computer.thongkesimcard.listener.IOnItemClickedListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
/**
 * Created by along on 5/24/2017.
 */
public class SimAdapter extends RecyclerView.Adapter<SimAdapter.DishViewHolder>{

    private Context context;
    private JSONArray jsonData;
    private IOnItemClickedListener listener;

    public SimAdapter(Context context, JSONArray jsonData, IOnItemClickedListener listener){
        this.context = context;
        this.jsonData = jsonData;
        this.listener = listener;
    }

    @Override
    public DishViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sim,parent,false);
        return new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DishViewHolder holder, final int position) {
        JSONObject jsonObject = jsonData.optJSONObject(position);
        if(jsonObject != null){
            String text = jsonObject.optString("simNumber").substring(jsonObject.optString("simNumber").length() - 5);
            holder.tvNameSim.setText("..." + text);
            holder.tvSoDuNap.setText(formatMoney(jsonObject.optInt("accountBalance")));
            holder.tvSoDuMua.setText(formatMoney(jsonObject.optInt("clientAccountBalance")));
            holder.tvDateNap.setText(jsonObject.optString("lastUpdated"));
            holder.tvDateMua.setText(jsonObject.optString("clientUpdateDate"));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClicked(position,view);
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return jsonData.length();
    }

    public static class DishViewHolder extends RecyclerView.ViewHolder{
       private TextView tvNameSim, tvSoDuNap, tvSoDuMua, tvDateNap, tvDateMua;
        public DishViewHolder(View itemView) {
            super(itemView);
            tvNameSim = itemView.findViewById(R.id.tv_name_sim);
            tvSoDuNap = itemView.findViewById(R.id.tv_so_du_nap);
            tvSoDuMua = itemView.findViewById(R.id.tv_so_du_mua);
            tvDateNap = itemView.findViewById(R.id.tv_date_nap);
            tvDateMua = itemView.findViewById(R.id.tv_date_mua);
        }
    }

    //Hàm định dạng tiền
    public String formatMoney(int money){
        final NumberFormat formatter = new DecimalFormat("###,###,###.##");
        String money_show = formatter.format(money);
        return  money_show;
    }
}
