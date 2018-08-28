package com.example.computer.thongkesimcard.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.computer.thongkesimcard.R;
import com.example.computer.thongkesimcard.listener.IOnItemClickedListener;
import com.example.computer.thongkesimcard.listener.OnLoadMoreListener;
import com.example.computer.thongkesimcard.obj.Sim;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

/**
 * Created by along on 5/24/2017.
 */
public class SimAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<Sim> listSim;
    private IOnItemClickedListener listener;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    public SimAdapter(RecyclerView recyclerView, Context context, List<Sim> listSim, IOnItemClickedListener listener){
        this.context = context;
        this.listSim = listSim;
        this.listener = listener;
        Log.d("getItem", getItemCount()+"");

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return listSim.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sim,parent,false);
            return new DishViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading,parent,false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof DishViewHolder) {
            Sim sim = listSim.get(position);
            DishViewHolder dishViewHolder = (DishViewHolder) holder;
            if(sim != null){
                String text = sim.getName().substring(sim.getName().length() - 5);
                dishViewHolder.tvNameSim.setText("..." + text);
                dishViewHolder.tvSoDuNap.setText(formatMoney(sim.getMoneyNap()));
                dishViewHolder.tvSoDuMua.setText(formatMoney(sim.getMoneyMua()));
                dishViewHolder.tvDateNap.setText(sim.getDateNap());
                dishViewHolder.tvDateMua.setText(sim.getDateMua());
                dishViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onItemClicked(position,view);
                    }
                });
            }
        }else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return listSim == null ? 0 : listSim.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
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
