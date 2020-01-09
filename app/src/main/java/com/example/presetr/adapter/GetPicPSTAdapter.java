package com.example.presetr.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.presetr.MyApplication;
import com.example.presetr.R;
import com.example.presetr.activity.BaseActivity;
import com.example.presetr.activity.DiyActivity;
import com.example.presetr.activity.GetPicPSTActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GetPicPSTAdapter extends RecyclerView.Adapter<GetPicPSTAdapter.ViewHolder> {
    private static final String TAG = "GetPicPSTAdapter";

    private List<String> path;

    public GetPicPSTAdapter(List<String> path) {
        this.path = path;

    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.item_pic_pst_pic)
        ImageView pic;
        @BindView(R.id.item_pic_pst_filepath)
        TextView textView;
        @BindView(R.id.item_pic_pst_photo)
        ImageView toCamera;
        @BindView(R.id.item_pic_pst_photo_layout)
        ConstraintLayout toCameraLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            pic.setOnClickListener(this);
            toCameraLayout.setOnClickListener(this::onClick);
        }


        @Override
        public void onClick(View v) {
            GetPicPSTActivity activity = (GetPicPSTActivity) v.getContext();
            if(getAdapterPosition()==0){
                //照相机
                activity.dispatchTakePictureIntent();
            }else {
                Bundle bundle = new Bundle();
                bundle.putInt("position",activity.getFilter_position());
                bundle.putString("pic_filepath",textView.getText().toString());
                activity.startActivity(DiyActivity.class,bundle);
            }
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pic_pst,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String tag = (String) holder.pic.getTag();
        if(position==0){
            holder.pic.setVisibility(View.INVISIBLE);
            holder.toCameraLayout.setVisibility(View.VISIBLE);
            if(tag==null||tag.equals("phone")){
                Glide.with(holder.pic.getContext())
                        .load(R.drawable.icon_camera)
                        //.placeholder(R.drawable.hint_buffer)
                        //.override(MyApplication.phone_width/3)
                        .into(holder.toCamera);
            }else {
                holder.pic.setImageResource(R.drawable.hint_buffer);
                Glide.with(holder.pic.getContext())
                        .load(R.drawable.icon_camera)
                        //.placeholder(R.drawable.hint_buffer)
                        //.override(MyApplication.phone_width/3)
                        .into(holder.toCamera);
            }
            holder.pic.setTag("phone");
        }else if(position>0) {
            holder.pic.setVisibility(View.VISIBLE);
            holder.toCameraLayout.setVisibility(View.INVISIBLE);
            String filepath = path.get(position-1);
            holder.textView.setText(filepath);
            if(tag==null||tag.equals(filepath)){
                Glide.with(holder.pic.getContext())
                        .load(filepath)
                        //.placeholder(R.drawable.hint_buffer)
                        //.override(MyApplication.phone_width,MyApplication.phone_width)
                        .into(holder.pic);
            }else {
                holder.pic.setImageResource(R.drawable.hint_buffer);
                Glide.with(holder.pic.getContext())
                        .load(filepath)
                        //.placeholder(R.drawable.hint_buffer)
                        //.override(MyApplication.phone_width,MyApplication.phone_width)
                        .into(holder.pic);
            }
            holder.pic.setTag(filepath);
        }
    }

    @Override
    public int getItemCount() {
        return path.size()+1;
    }

}
