package com.example.presetr.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.presetr.MyApplication;
import com.example.presetr.R;
import com.example.presetr.activity.BaseActivity;
import com.example.presetr.activity.FilterPreviewActivity;
import com.example.presetr.model.FilterPackageBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFilterAdapter extends RecyclerView.Adapter<MainFilterAdapter.ViewHolder> {
    private static final String TAG = "MainFilterAdapter";

    private List<FilterPackageBean> list;

    public MainFilterAdapter(List<FilterPackageBean> list) {
        this.list = list;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.item_filter_name)
        TextView filter_name;
        @BindView(R.id.item_filter_tag)
        TextView filter_tag;
        @BindView(R.id.item_filter_purchase)
        ImageView filter_purchase;
        @BindView(R.id.item_filter_pic)
        ImageView filter_pic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            filter_pic.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            BaseActivity activity = (BaseActivity) v.getContext();
            Bundle bundle = new Bundle();
            bundle.putString("filter_name",filter_name.getText().toString());
            activity.startActivity(FilterPreviewActivity.class,bundle);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter_main,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: "+position);
        FilterPackageBean bean = list.get(position);
        holder.filter_name.setText(bean.getDisplayName());
        holder.filter_tag.setText(bean.getFilters().size()+" "+bean.getDisplayName()+" "+"Presets");
        String tag = (String) holder.filter_pic.getTag();
        //holder.filter_pic.setImageResource(R.drawable.ic_launcher_foreground);
        if(tag==null||tag.equals(bean.getName())) {
            if(position>=0&&position<4) {
                Glide.with(holder.filter_pic.getContext())
                        .load("file:///android_asset/" + "previewPic/" + bean.getName() + "/" + "cover.png")
                        .placeholder(R.drawable.hint_buffer)
                        .into(holder.filter_pic);
            }else{
                String url = MyApplication.PRE_URL+MyApplication.RESOURCE+bean.getName()+MyApplication.COVER_PIC+bean.getCover();
                Glide.with(holder.filter_pic.getContext())
                        .load(url)
                        .placeholder(R.drawable.hint_buffer)
                        .into(holder.filter_pic);
            }
        }else {
            if(position>=0&&position<4){
                holder.filter_pic.setImageResource(R.drawable.hint_buffer);
                Glide.with(holder.filter_pic.getContext())
                        .load("file:///android_asset/" + "previewPic/" + bean.getName() + "/" + "cover.png")
                        .placeholder(R.drawable.hint_buffer)
                        .into(holder.filter_pic);
            }else {
                holder.filter_pic.setImageResource(R.drawable.hint_buffer);
                Glide.with(holder.filter_pic.getContext())
                        .load(MyApplication.PRE_URL+
                                MyApplication.RESOURCE+bean.getName()+MyApplication.COVER_PIC+bean.getCover())
                        .placeholder(R.drawable.hint_buffer)
                        .into(holder.filter_pic);
            }
        }
        holder.filter_pic.setTag(bean.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
