package com.example.presetr.adapter;

import android.os.Bundle;
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
import com.example.presetr.activity.DiyActivity;
import com.example.presetr.activity.GetPicPSTActivity;
import com.example.presetr.model.FilterBean;
import com.example.presetr.model.FilterPackageBean;
import com.example.presetr.view.RoundImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviewFilterAdapter extends RecyclerView.Adapter<PreviewFilterAdapter.ViewHolder> {
    private static final String TAG = "PreviewFilterAdapter";

    private FilterPackageBean bean;

    public PreviewFilterAdapter(FilterPackageBean bean) {
        this.bean = bean;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.item_filter_preview_pic)
        ImageView preview_pic;
        @BindView(R.id.item_filter_preview_name)
        TextView preview_pic_name;
        @BindView(R.id.item_filter_preview_tag)
        TextView filter_tag;
        @BindView(R.id.item_filter_preview_content)
        TextView filter_content;
        @BindView(R.id.item_filterpackage_name)
        TextView filterPackageName;
        @BindView(R.id.item_filter_preview_round_pic)
        RoundImageView round_pic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            round_pic.setOnClickListener(this);
            preview_pic.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.item_filter_preview_round_pic:
                case R.id.item_filter_preview_pic:
                    BaseActivity activity = (BaseActivity) v.getContext();
                    FilterBean filterBean = MyApplication.filterBeanMap.get(preview_pic_name.getText().toString());
                    Bundle bundle = new Bundle();
                    bundle.putInt("position",filterBean.getPosition());
                    activity.startActivity(GetPicPSTActivity.class,bundle);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + v.getId());
            }
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter_preview,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FilterBean filterBean = bean.getFilters().get(position);
        if(position==0){
            holder.filter_tag.setVisibility(View.VISIBLE);
            holder.filter_content.setVisibility(View.VISIBLE);
            holder.filterPackageName.setVisibility(View.VISIBLE);
            holder.preview_pic.setVisibility(View.VISIBLE);
            holder.round_pic.setVisibility(View.INVISIBLE);
            holder.filter_tag.setText(bean.getFilters().size()+" "+bean.getDisplayName()+" Presets");
            holder.filter_content.setText(bean.getDescription());
            holder.filterPackageName.setText(bean.getDisplayName());

            String tag = (String) holder.preview_pic.getTag();
            //holder.preview_pic.setImageResource(R.drawable.hint_buffer);
            if(tag==null||tag.equals(String.valueOf(filterBean.getOrder()))) {
                String url = MyApplication.PRE_URL+MyApplication.RESOURCE+bean.getName()+MyApplication.PREVIEW_PIC+filterBean.getPreview();
                Glide.with(holder.preview_pic.getContext())
                        .load(url)
                        .placeholder(R.drawable.hint_buffer)
                        .into(holder.preview_pic);
            }else {
                //holder.preview_pic.setImageResource(R.drawable.hint_buffer);
                String url = MyApplication.PRE_URL+MyApplication.RESOURCE+bean.getName()+MyApplication.PREVIEW_PIC+filterBean.getPreview();
                Glide.with(holder.preview_pic.getContext())
                        .load(url)
                        .placeholder(R.drawable.hint_buffer)
                        .into(holder.preview_pic);
            }
            holder.preview_pic_name.setText(filterBean.getPreview());
            holder.preview_pic.setTag(String.valueOf(filterBean.getOrder()));
        }else {
            holder.round_pic.setVisibility(View.VISIBLE);
            holder.preview_pic.setVisibility(View.INVISIBLE);
            holder.filter_tag.setVisibility(View.GONE);
            holder.filter_content.setVisibility(View.GONE);
            holder.filterPackageName.setVisibility(View.GONE);

            String tag = (String) holder.round_pic.getTag();
            holder.round_pic.setImageResource(R.drawable.hint_buffer);
            if(tag==null||tag.equals(String.valueOf(filterBean.getOrder()))) {
                String url = MyApplication.PRE_URL+MyApplication.RESOURCE+bean.getName()+MyApplication.PREVIEW_PIC+filterBean.getPreview();
                Glide.with(holder.round_pic.getContext())
                        .load(url)
                        .placeholder(R.drawable.hint_buffer)
                        .into(holder.round_pic);
            }else {
                holder.round_pic.setImageResource(R.drawable.hint_buffer);
                String url = MyApplication.PRE_URL+MyApplication.RESOURCE+bean.getName()+MyApplication.PREVIEW_PIC+filterBean.getPreview();
                Glide.with(holder.round_pic.getContext())
                        .load(url)
                        .placeholder(R.drawable.hint_buffer)
                        .into(holder.round_pic);
            }
            holder.preview_pic_name.setText(filterBean.getPreview());
            holder.round_pic.setTag(String.valueOf(filterBean.getOrder()));
        }

    }

    @Override
    public int getItemCount() {
        return bean.getFilters().size();
    }

}
