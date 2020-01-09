package com.example.presetr.adapter.filterDIYAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.presetr.MyApplication;
import com.example.presetr.R;
import com.example.presetr.activity.BaseActivity;
import com.example.presetr.activity.DiyActivity;
import com.example.presetr.activity.PurchaseActivity;
import com.example.presetr.model.FilterBean;
import com.example.presetr.model.FilterPackageBean;
import com.example.presetr.util.ImageLoaderUtil;
import com.example.presetr.util.NetWorkUtil;
import com.example.presetr.util.OKHttpUtil;
import com.example.presetr.util.ToastUtil;
import com.example.presetr.view.FilterSelectLayout;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterPicAdapter extends RecyclerView.Adapter<FilterPicAdapter.ViewHolder> {
    private List<FilterBean> beans;

    public FilterPicAdapter(List<FilterBean> beans) {
        this.beans = beans;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private DownLoadHandler handler = new DownLoadHandler();
        private final int SUCCESS = 1;
        private DiyActivity activity;

        @BindView(R.id.item_filter_pic_pic)
        ImageView filter_pic;
        @BindView(R.id.item_filter_pic_name)
        TextView pic_name;
        @BindView(R.id.item_filter_pic_is_vip)
        ImageView pic_is_vip;
        @BindView(R.id.item_filter_pic_download)
        ImageView pic_download;
        @BindView(R.id.item_filter_pic_processBar)
        ProgressBar progressBar;
        @BindView(R.id.item_filter_pic_name_invisible)
        TextView pic_name_all;
        @BindView(R.id.item_filter_pic_select)
        ImageView select_image;
        @BindView(R.id.item_filter_layout)
        ConstraintLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            activity = (DiyActivity) itemView.getContext();
            pic_is_vip.setOnClickListener(this);
            pic_download.setOnClickListener(this);
            filter_pic.setOnClickListener(this);
            select_image.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.item_filter_pic_is_vip:
                    DiyActivity activity = (DiyActivity) v.getContext();
                    activity.startActivity(PurchaseActivity.class);
                    break;
                case R.id.item_filter_pic_pic:
                    if(pic_download.getVisibility()==View.INVISIBLE) {
                        clickFilterPic(v);
                    }else if(pic_download.getVisibility()==View.VISIBLE){
                        clickDownLoad(v);
                    }
                    break;
                case R.id.item_filter_pic_download:
                    clickDownLoad(v);
                    break;
                case R.id.item_filter_pic_select:
                    clickSelect(v);
                    break;
                default:
                    break;
            }
        }

        private void clickSelect(View v) {
            if(getAdapterPosition()!=0) {
                activity.getFilterDiyEditBar().setVisibility(View.VISIBLE);
                activity.getFilter().setBackOld(0);
                activity.setFilter(activity.getFilter());
                Bitmap bm = activity.getDiyImage().getGPUImage().getBitmapWithFilterApplied();
                activity.getFilterDiyEditBar().setProgress((int) (activity.getFilter().intensity*100));
            }
        }

        private void clickFilterPic(View v) {
            if(getAdapterPosition()!=0) {
                //换滤镜
                Bitmap oldFilter = activity.getFilter().getBitmap();
                if(oldFilter!=null){
                    if(oldFilter.isRecycled()==false){
                        oldFilter.recycle();
                    }
                }
                activity.getFilter().setBackOld(0);
                FilterBean bean = MyApplication.filterBeans.get(getAdapterPosition()-1);
                activity.getFilter().setBitmap(ImageLoaderUtil.getBitmapInFile(MyApplication.context,bean.getLut()));
                activity.getDiyImage().setFilter(activity.getFilter());
                //更新adapter
                int last = FilterSelectLayout.PIC_POSITION;
                FilterSelectLayout.PIC_POSITION = getAdapterPosition();
                notifyItemChanged(last, 1);
                notifyItemChanged(getAdapterPosition(), 1);
            }else {
                activity.getFilter().setBackOld(1);
                activity.getDiyImage().setFilter(activity.getFilter());
                int last = FilterSelectLayout.PIC_POSITION;
                FilterSelectLayout.PIC_POSITION = getAdapterPosition();
                notifyItemChanged(last, 1);
                notifyItemChanged(getAdapterPosition(), 1);
            }
        }

        private void clickDownLoad(View v) {
            if(!NetWorkUtil.isNetWorkAvailable()){
                ToastUtil.toast(MyApplication.getContext(),"下载失败，请检查网络");
            }else {
                pic_download.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FilterBean bean = MyApplication.filterBeans.get(getAdapterPosition()-1);
                        String url = MyApplication.PRE_URL+MyApplication.RESOURCE+bean.getPackageName()
                                +MyApplication.LUT_PIC+bean.getLut();
                        InputStream in = OKHttpUtil.getInputStream(url);
                        saveImage(v.getContext(),in,bean.getLut());
                        MyApplication.filters.add(bean.getLut());
                        Message message = new Message();
                        message.what = SUCCESS;
                        handler.sendMessage(message);
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
        public void saveImage(Context context, InputStream in,String name){
            FileOutputStream fos = null;
            try {
                fos = context.openFileOutput(name,Context.MODE_PRIVATE);
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }finally {
                if(fos!=null){
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private class DownLoadHandler extends Handler {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        ToastUtil.toast(MyApplication.getContext(),"下载完成！");
                        break;
                }
            }
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_filter_diy_pic,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            if (position != 0) {
                if (position == FilterSelectLayout.PIC_POSITION) {
                    holder.select_image.setVisibility(View.VISIBLE);
                } else {
                    holder.select_image.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position==0){
            holder.pic_download.setVisibility(View.INVISIBLE);
            holder.pic_is_vip.setVisibility(View.INVISIBLE);
            holder.pic_name.setVisibility(View.INVISIBLE);
            holder.select_image.setVisibility(View.INVISIBLE);
//            holder.select_image.setScaleType(ImageView.ScaleType.FIT_START);
//            holder.select_image.setImageResource(R.drawable.hint_o_ing);
            Glide.with(holder.filter_pic.getContext())
                    .load(R.drawable.hint_o_ing)
                    .into(holder.filter_pic);
        }else {
            FilterBean bean = beans.get(position-1);
            FilterPackageBean packageBean = MyApplication.filterPackagesMap.get(bean.getDisplayName());
            holder.pic_download.setVisibility(View.VISIBLE);
            holder.pic_is_vip.setVisibility(View.VISIBLE);
            holder.pic_name.setVisibility(View.VISIBLE);

            if(packageBean.getIsCharged()==0){
                holder.pic_is_vip.setVisibility(View.INVISIBLE);
            }else {
                holder.pic_is_vip.setVisibility(View.VISIBLE);
            }
            if(MyApplication.filters.contains(bean.getLut())){
                holder.pic_download.setVisibility(View.INVISIBLE);
            }else {
                holder.pic_download.setVisibility(View.VISIBLE);
            }
            String name = bean.getLut();
            if(name.contains(".png")){
                name = name.replace(".png","");
            }
            if(name.contains(".jpg")){
                name = name.replace(".jpg","");
            }
            holder.pic_name.setText(name);
            String url  = MyApplication.PRE_URL+MyApplication.RESOURCE
                    +bean.getPackageName()+MyApplication.THUMBNAIL_PIC+bean.getThumbnail();
            Glide.with(holder.filter_pic.getContext())
                    .load(url)
                    .into(holder.filter_pic);
            holder.pic_name_all.setText(bean.getPreview());

            if(position==FilterSelectLayout.PIC_POSITION){
                holder.select_image.setVisibility(View.VISIBLE);
            }else {
                holder.select_image.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return beans.size()+1;
    }


}
