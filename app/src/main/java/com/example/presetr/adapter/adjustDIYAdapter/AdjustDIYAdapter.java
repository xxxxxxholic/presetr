package com.example.presetr.adapter.adjustDIYAdapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.presetr.R;
import com.example.presetr.activity.DiyActivity;
import com.example.presetr.filter.GPUImageBrightnessFilter;
import com.example.presetr.filter.GPUImageContrastFilter;
import com.example.presetr.filter.GPUImageFilterFactory;
import com.example.presetr.filter.GPUImageGrainFilter;
import com.example.presetr.filter.GPUImageHighlightShadowFilter;
import com.example.presetr.filter.GPUImageHueFilter;
import com.example.presetr.filter.GPUImageSaturationFilter;
import com.example.presetr.filter.GPUImageSharpenFilter;
import com.example.presetr.filter.GPUImageVignetteFilter;
import com.example.presetr.filter.GPUImageWhiteBalanceFilter;
import com.example.presetr.model.AdjustBean;
import com.example.presetr.view.AdjustSelectLayout;
import com.example.presetr.view.OneWayEditBar;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

public class AdjustDIYAdapter extends RecyclerView.Adapter<AdjustDIYAdapter.ViewHolder> {
    private List<AdjustBean> adjustBeans;

    private Set<Integer> changes;
    private String clickedColor = "#6547FF";
    private String noClickColor = "#FFFFFF";

    public AdjustDIYAdapter(List<AdjustBean> adjustBeans) {
        this.adjustBeans = adjustBeans;
        changes = new LinkedHashSet<>();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            OneWayEditBar.OnClickCancelListener, OneWayEditBar.OnClickDoneListener, OneWayEditBar.OnSlidingListener {
        @BindView(R.id.item_adjust_select_pic)
        ImageView adjust_select_pic;
        @BindView(R.id.item_adjust_select_name)
        TextView adjust_select_name;
        @BindView(R.id.item_adjust_select_is_click)
        TextView isClickable;
        @BindView(R.id.item_adjust_arrow)
        View arrow;

        DiyActivity activity;

        int progress = 0;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            adjust_select_pic.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View v) {
            activity = (DiyActivity) v.getContext();
//            activity.getDiyImage().getGPUImage().deleteImage();
//            activity.getDiyImage().setImage(activity.getCur_bitmap());
            //adjust_select_name.setTextColor(Color.parseColor(clickedColor));
            int last = AdjustSelectLayout.POSITION;
            AdjustSelectLayout.POSITION = getAdapterPosition();
            notifyItemChanged(last,1);
            notifyItemChanged(getAdapterPosition(),1);
            switch (adjust_select_name.getText().toString()){
                case "Vignette":
                case "Grain":
                    if(isClickable.getVisibility()==View.INVISIBLE){
                        GPUImageFilter filter = GPUImageFilterFactory.getFilter(getAdapterPosition());
                        activity.getDiyImage().setFilter(filter);
                        //0,1,2,5,6
                        switch (getAdapterPosition()){
                            case 5:
                                progress = (int) (((((GPUImageVignetteFilter)filter).getVignetteStart()-0.25)*100)/(0.7-0.25));
                                break;
                            case 6:
                                progress = (int) (((GPUImageGrainFilter)filter).getGrain()*5);
                                break;
                        }
                        isClickable.setVisibility(View.VISIBLE);
                    }else if(isClickable.getVisibility()==View.VISIBLE){
                        activity.getAdjustOneWayEditBar().setProgress(progress);
                        activity.getAdjustOneWayEditBar().setVisibility(View.VISIBLE);
                        activity.getAdjustOneWayEditBar().setClickCancelListener(this);
                        activity.getAdjustOneWayEditBar().setClickDoneListener(this);
                        activity.getAdjustOneWayEditBar().setSlidingListener(this);
                    }
                    break;
                case "Brightness": case "Contrast": case "Saturation":
                case "Sharpen": case "Shadows":
                case "Highlights":
                    activity = (DiyActivity) v.getContext();
                    if(isClickable.getVisibility()==View.INVISIBLE){
                        GPUImageFilter filter = GPUImageFilterFactory.getFilter(getAdapterPosition());
                        activity.getDiyImage().setFilter(filter);
                        //int progress = 0;
                        //4,7,8
                        switch (getAdapterPosition()){
                            case 0:
                                progress = (int) (((((GPUImageBrightnessFilter)filter).getBrightness()+0.25)*400)-100);
                                break;
                            case 1:
                                progress = (int) ((((GPUImageContrastFilter)filter).getContrast()-0.5)*200-100);
                                break;
                            case 2:
                                progress = (int) (((GPUImageSaturationFilter)filter).getSaturation()*100-100);
                                break;
                            case 4://-2.5-2.5
                                progress = (int) ((((GPUImageSharpenFilter)filter).getSharpness()+2.5f)/5*200-100);
                                break;
                            case 7://-0.5-0.5
                                progress = (int) ((((GPUImageHighlightShadowFilter)filter).shadows+0.5f)*200-100);
                                break;
                            case 8://-1-1
                                progress = (int) ((((GPUImageHighlightShadowFilter)filter).highlights+1.0f)/2*200-100);
                                break;
                        }
//                        activity.getAdjustTwoWayEditBar().setProgress(progress);
                        isClickable.setVisibility(View.VISIBLE);
                    }else if(isClickable.getVisibility()==View.VISIBLE){
                        activity.getAdjustTwoWayEditBar().setProgress(progress);
                        activity.getAdjustTwoWayEditBar().setVisibility(View.VISIBLE);
                        activity.getAdjustTwoWayEditBar().setClickCancelListener(this);
                        activity.getAdjustTwoWayEditBar().setClickDoneListener(this);
                        activity.getAdjustTwoWayEditBar().setSlidingListener(this);
                    }
                    break;
                case "Temp":
                    activity = (DiyActivity) v.getContext();
                    if(isClickable.getVisibility()==View.INVISIBLE){
                        GPUImageFilter filter = GPUImageFilterFactory.getFilter(getAdapterPosition());
                        activity.getDiyImage().setFilter(filter);
                        int progress = (int) ((((GPUImageWhiteBalanceFilter)filter).temperature+0.3f)/0.6f*200-100);
                        activity.getAdjustTempEditBar().setProgress(progress);
                        isClickable.setVisibility(View.VISIBLE);
                    }else if(isClickable.getVisibility()==View.VISIBLE){
                        activity.getAdjustTempEditBar().setVisibility(View.VISIBLE);
                        activity.getAdjustTempEditBar().setClickCancelListener(this);
                        activity.getAdjustTempEditBar().setClickDoneListener(this);
                        activity.getAdjustTempEditBar().setSlidingListener(this);
                    }
                    break;
                case "Hue":
                    activity = (DiyActivity) v.getContext();
                    if(isClickable.getVisibility()==View.INVISIBLE){
                        GPUImageFilter filter = GPUImageFilterFactory.getFilter(getAdapterPosition());
                        activity.getDiyImage().setFilter(filter);
                        int progress = (int) (((GPUImageHueFilter)filter).getHue()/360*100);
                        activity.getAdjustHueEditBar().setProgress(progress);
                        isClickable.setVisibility(View.VISIBLE);
                    }else if(isClickable.getVisibility()==View.VISIBLE){
                        activity.getAdjustHueEditBar().setVisibility(View.VISIBLE);
                        activity.getAdjustHueEditBar().setClickCancelListener(this);
                        activity.getAdjustHueEditBar().setClickDoneListener(this);
                        activity.getAdjustHueEditBar().setSlidingListener(this);
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + adjust_select_name.getText().toString());
            }
        }
        @Override
        public void onClickCancel(View v) {
            //取消该滤镜
            switch (adjust_select_name.getText().toString()){
                case "Brightness":
                    GPUImageBrightnessFilter brightnessFilter =((GPUImageBrightnessFilter)(activity.getDiyImage().getFilter()));
//                    if((progress+100.0f)/400-0.25f!=brightnessFilter.mDefault){
//                        arrow.setVisibility(View.VISIBLE);
//                    }else {
                        arrow.setVisibility(View.INVISIBLE);
//                    }
                    brightnessFilter.setBrightness(brightnessFilter.mDefault);
                    break;
                case "Contrast":
                    GPUImageContrastFilter contrastFilter =((GPUImageContrastFilter)(activity.getDiyImage().getFilter()));
                    arrow.setVisibility(View.INVISIBLE);
                    contrastFilter.setContrast(contrastFilter.mDefault);
                    break;
                case "Saturation":
                    GPUImageSaturationFilter saturationFilter = (GPUImageSaturationFilter) activity.getDiyImage().getFilter();
                    arrow.setVisibility(View.INVISIBLE);
                    saturationFilter.setSaturation(saturationFilter.mDefault);
                    break;
                case "Vignette":
                    GPUImageVignetteFilter vignetteFilter = (GPUImageVignetteFilter) activity.getDiyImage().getFilter();
//                    if((float)progress/100.0f*(0.70f-0.25f)+0.25f!=vignetteFilter.mDefault){
//                        arrow.setVisibility(View.VISIBLE);
//                    }else {
                        arrow.setVisibility(View.INVISIBLE);
//                    }
                    vignetteFilter.setVignetteStart(vignetteFilter.mDefault);
                    break;
                case "Grain":
                    GPUImageGrainFilter grainFilter = (GPUImageGrainFilter) activity.getDiyImage().getFilter();
//                    if((float)progress/100*20.0f!=grainFilter.mDefault){
//                        arrow.setVisibility(View.VISIBLE);
//                    }else {
                        arrow.setVisibility(View.INVISIBLE);
//                    }
                    grainFilter.setGrain(grainFilter.mDefault);
                    break;
                //next group
                case "Sharpen":
                    GPUImageSharpenFilter sharpenFilter = (GPUImageSharpenFilter) activity.getDiyImage().getFilter();
//                    if(((float)progress+100)/40-2.5f!=sharpenFilter.mDefault){
//                        arrow.setVisibility(View.VISIBLE);
//                    }else {
                        arrow.setVisibility(View.INVISIBLE);
//                    }
                    sharpenFilter.setSharpness(sharpenFilter.mDefault);
                    break;
                case "Shadows":
                    GPUImageHighlightShadowFilter shadowFilter = (GPUImageHighlightShadowFilter) activity.getDiyImage().getFilter();
//                    if(((float)progress+100)/200-0.5f!=shadowFilter.mDefault_shadows){
//                        arrow.setVisibility(View.VISIBLE);
//                    }else {
                        arrow.setVisibility(View.INVISIBLE);
//                    }
                    shadowFilter.setShadows(shadowFilter.mDefault_shadows);
                    break;
                case "Highlights":
                    GPUImageHighlightShadowFilter highlightShadowFilter = (GPUImageHighlightShadowFilter) activity.getDiyImage().getFilter();
//                    if(((float)progress+100)/100-1.0f!=highlightShadowFilter.mDefault_highlights){
//                        arrow.setVisibility(View.VISIBLE);
//                    }else {
                        arrow.setVisibility(View.INVISIBLE);
//                    }
                    highlightShadowFilter.setHighlights(highlightShadowFilter.mDefault_highlights);
                    break;
                case "Temp":
                    GPUImageWhiteBalanceFilter balanceFilter = (GPUImageWhiteBalanceFilter) activity.getDiyImage().getFilter();
//                    if(((float)progress+100)/200*0.6f-0.3f!=balanceFilter.mDefault){
//                        arrow.setVisibility(View.VISIBLE);
//                    }else {
                        arrow.setVisibility(View.INVISIBLE);
//                    }
                    balanceFilter.setTemperature(balanceFilter.mDefault);
                    break;
                case "Hue":
                    GPUImageHueFilter hueFilter = (GPUImageHueFilter)activity.getDiyImage().getFilter();
//                    if((float)progress/100*360!=hueFilter.mDefault){
//                        arrow.setVisibility(View.VISIBLE);
//                    }else {
                        arrow.setVisibility(View.INVISIBLE);
//                    }
                    hueFilter.setHue(hueFilter.mDefault);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + adjust_select_name.getText().toString());
            }
            activity.getFilter().setBackOld(1);
            activity.getDiyImage().setFilter(activity.getFilter());
            activity.getAdjustOneWayEditBar().setVisibility(View.INVISIBLE);
            activity.getAdjustTwoWayEditBar().setVisibility(View.INVISIBLE);
            activity.getAdjustTempEditBar().setVisibility(View.INVISIBLE);
            activity.getAdjustHueEditBar().setVisibility(View.INVISIBLE);
            isClickable.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onClickDone(View v) {
            //确认该滤镜
            activity.setCur_bitmap(activity.getDiyImage().getGPUImage().getBitmapWithFilterApplied());
            activity.getAdjustOneWayEditBar().setVisibility(View.INVISIBLE);
            activity.getAdjustTwoWayEditBar().setVisibility(View.INVISIBLE);
            activity.getAdjustTempEditBar().setVisibility(View.INVISIBLE);
            activity.getAdjustHueEditBar().setVisibility(View.INVISIBLE);
            isClickable.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onSliding(View v,int progress) {
            switch (adjust_select_name.getText().toString()){
                case "Brightness":
                    GPUImageBrightnessFilter brightnessFilter =((GPUImageBrightnessFilter)(activity.getDiyImage().getFilter()));
                    //brightnessFilter.setBrightness(((float)((progress+100)/400))+(-0.25f));
                    brightnessFilter.setBrightness((progress+100.0f)/400-0.25f);
                    activity.getDiyImage().setFilter(brightnessFilter);
                    if((progress+100.0f)/400-0.25f!=brightnessFilter.mDefault){
                        changes.add(getAdapterPosition());
                        arrow.setVisibility(View.VISIBLE);
                    }else {
                        arrow.setVisibility(View.INVISIBLE);
                    }
                    break;
                case "Contrast":
                    GPUImageContrastFilter contrastFilter =((GPUImageContrastFilter)(activity.getDiyImage().getFilter()));
                    //contrastFilter.setContrast(((float) (progress+100)/200)+0.5f);
                    contrastFilter.setContrast((progress+100.0f)/200+0.5f);
                    activity.getDiyImage().setFilter(contrastFilter);
                    if((progress+100.0f)/200+0.5f!=contrastFilter.mDefault){
                        changes.add(getAdapterPosition());
                        arrow.setVisibility(View.VISIBLE);
                    }else {
                        arrow.setVisibility(View.INVISIBLE);
                    }
                    break;
                case "Saturation":
                    GPUImageSaturationFilter saturationFilter = (GPUImageSaturationFilter) activity.getDiyImage().getFilter();
                    //saturationFilter.setSaturation((float)(progress+100)/100*2);
                    saturationFilter.setSaturation((progress+100.0f)/200);
                    activity.getDiyImage().setFilter(saturationFilter);
                    if((progress+100.0f)/200!=saturationFilter.mDefault){
                        changes.add(getAdapterPosition());
                        arrow.setVisibility(View.VISIBLE);
                    }else {
                        arrow.setVisibility(View.INVISIBLE);
                    }
                    break;
                case "Vignette":
                    GPUImageVignetteFilter vignetteFilter = (GPUImageVignetteFilter) activity.getDiyImage().getFilter();
                    vignetteFilter.setVignetteStart((float)progress/100*(0.7f-0.25f)+0.25f);
                    activity.getDiyImage().setFilter(vignetteFilter);
                    if((float)progress/100*(0.7f-0.25f)+0.25f!=vignetteFilter.mDefault){
                        changes.add(getAdapterPosition());
                        arrow.setVisibility(View.VISIBLE);
                    }else {
                        arrow.setVisibility(View.INVISIBLE);
                    }
                    break;
                case "Grain":
                    GPUImageGrainFilter grainFilter = (GPUImageGrainFilter) activity.getDiyImage().getFilter();
                    grainFilter.setGrain((float)progress/100*20.0f);
                    activity.getDiyImage().setFilter(grainFilter);
                    if((float)progress/100*20.0f!=grainFilter.mDefault){
                        changes.add(getAdapterPosition());
                        arrow.setVisibility(View.VISIBLE);
                    }else {
                        arrow.setVisibility(View.INVISIBLE);
                    }
                    break;
                //next group
                case "Sharpen":
                    GPUImageSharpenFilter sharpenFilter = (GPUImageSharpenFilter) activity.getDiyImage().getFilter();
                    sharpenFilter.setSharpness(((float)progress+100)/40-2.5f);
                    activity.getDiyImage().setFilter(sharpenFilter);
                    if(((float)progress+100)/40-2.5f!=sharpenFilter.mDefault){
                        changes.add(getAdapterPosition());
                        arrow.setVisibility(View.VISIBLE);
                    }else {
                        arrow.setVisibility(View.INVISIBLE);
                    }
                    break;
                case "Shadows":
                    GPUImageHighlightShadowFilter shadowFilter = (GPUImageHighlightShadowFilter) activity.getDiyImage().getFilter();
                    shadowFilter.setShadows(((float)progress+100)/200-0.5f);
                    activity.getDiyImage().setFilter(shadowFilter);
                    if(((float)progress+100)/200-0.5f!=shadowFilter.mDefault_shadows){
                        changes.add(getAdapterPosition());
                        arrow.setVisibility(View.VISIBLE);
                    }else {
                        arrow.setVisibility(View.INVISIBLE);
                    }
                    break;
                case "Highlights":
                    GPUImageHighlightShadowFilter highlightShadowFilter = (GPUImageHighlightShadowFilter) activity.getDiyImage().getFilter();
                    highlightShadowFilter.setHighlights(((float)progress+100)/100-1.0f);
                    activity.getDiyImage().setFilter(highlightShadowFilter);
                    if(((float)progress+100)/100-1.0f!=highlightShadowFilter.mDefault_highlights){
                        changes.add(getAdapterPosition());
                        arrow.setVisibility(View.VISIBLE);
                    }else {
                        arrow.setVisibility(View.INVISIBLE);
                    }
                    break;
                case "Temp":
                    GPUImageWhiteBalanceFilter balanceFilter = (GPUImageWhiteBalanceFilter) activity.getDiyImage().getFilter();
                    balanceFilter.temperature = ((float)progress+100)/200*0.6f-0.3f;
                    activity.getDiyImage().setFilter(balanceFilter);
                    if(((float)progress+100)/200*0.6f-0.3f!=balanceFilter.mDefault){
                        changes.add(getAdapterPosition());
                        arrow.setVisibility(View.VISIBLE);
                    }else {
                        arrow.setVisibility(View.INVISIBLE);
                    }
                    break;
                case "Hue":
                    GPUImageHueFilter hueFilter = (GPUImageHueFilter)activity.getDiyImage().getFilter();
                    hueFilter.setHue((float)progress/100*360);
                    activity.getDiyImage().setFilter(hueFilter);
                    if((float)progress/100*360!=hueFilter.mDefault){
                        changes.add(getAdapterPosition());
                        arrow.setVisibility(View.VISIBLE);
                    }else {
                        arrow.setVisibility(View.INVISIBLE);
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + adjust_select_name.getText().toString());
            }
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_adjust_select,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if(payloads.isEmpty()){
            onBindViewHolder(holder,position);
        }else {
            if(position == AdjustSelectLayout.POSITION){
                holder.adjust_select_name.setTextColor(Color.parseColor(clickedColor));
            }else {
                holder.adjust_select_name.setTextColor(Color.parseColor(noClickColor));
                holder.isClickable.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdjustBean bean = adjustBeans.get(position);
        holder.adjust_select_name.setText(bean.getName());
        Glide.with(holder.adjust_select_pic.getContext())
                .load("file:///android_asset/"+"adjustImage/"+bean.getFilename())
                .into(holder.adjust_select_pic);
        holder.arrow.setVisibility(View.INVISIBLE);
        if(position==AdjustSelectLayout.POSITION){
            holder.adjust_select_name.setTextColor(Color.parseColor(clickedColor));
        }else {
            holder.adjust_select_name.setTextColor(Color.parseColor(noClickColor));
        }
        if(changes.contains(position)){
            holder.arrow.setVisibility(View.VISIBLE);
        }else {
            holder.arrow.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return adjustBeans.size();
    }

}
