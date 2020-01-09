package com.example.presetr.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.presetr.MyApplication;
import com.example.presetr.R;
import com.example.presetr.filter.GPUImageLookupFilter;
import com.example.presetr.model.FilterBean;
import com.example.presetr.util.BitmapUtil;
import com.example.presetr.util.ImageLoaderUtil;
import com.example.presetr.util.NetWorkUtil;
import com.example.presetr.util.OKHttpUtil;
import com.example.presetr.util.ToastUtil;
import com.example.presetr.view.AdjustSelectLayout;
import com.example.presetr.view.CropControllerView;
import com.example.presetr.view.EditSelectLayout;
import com.example.presetr.view.FilterEditBar;
import com.example.presetr.view.FilterSelectLayout;
import com.example.presetr.view.HueEditBar;
import com.example.presetr.view.OneWayEditBar;
import com.example.presetr.view.TempEditBar;
import com.example.presetr.view.TwoWayEditBar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

public class DiyActivity extends BaseActivity implements View.OnTouchListener {
    private static final String TAG = "DiyActivity";

    @BindView(R.id.diy_image)
    GPUImageView diyImage;
    @BindView(R.id.diy_cancel)
    ImageView diyCancel;
    @BindView(R.id.diy_done)
    ImageView diyDone;
    @BindView(R.id.diy_contrast)
    ImageView diyContrast;
    @BindView(R.id.diy_filter)
    ImageView diyFilter;
    @BindView(R.id.diy_overlay)
    ImageView diyOverlay;
    @BindView(R.id.diy_adjust)
    ImageView diyAdjust;
    @BindView(R.id.diy_edit)
    ImageView diyEdit;

    @BindView(R.id.diy_select_filter)
    ConstraintLayout diySelectFilter;

    @BindView(R.id.filter_diy_select_layout)
    LinearLayout filterDiySelectLayout;
    @BindView(R.id.diy_image_source)
    ImageView diyImageSource;

    @BindView(R.id.filter_diy_edit_bar)
    FilterEditBar filterDiyEditBar;

    @BindView(R.id.filter_select_custom_layout)
    FilterSelectLayout filterSelectCustomLayout;
    @BindView(R.id.adjust_select_custom_layout)
    AdjustSelectLayout adjustSelectCustomLayout;
    @BindView(R.id.adjust_one_way_edit_bar)
    OneWayEditBar adjustOneWayEditBar;
    @BindView(R.id.adjust_two_way_edit_bar)
    TwoWayEditBar adjustTwoWayEditBar;
    @BindView(R.id.adjust_temp_edit_bar)
    TempEditBar adjustTempEditBar;
    @BindView(R.id.adjust_hue_edit_bar)
    HueEditBar adjustHueEditBar;
    @BindView(R.id.crop_select_custom_layout)
    EditSelectLayout cropSelectCustomLayout;
    @BindView(R.id.cropControllerView)
    CropControllerView cropControllerView;


    private int filter_position = -1;
    private String pic_filepath;
    private Bitmap sourceBitmap;
    public static Bitmap cur_bitmap;
    private Bitmap squareBitmap;

    private String LUT;

    private GPUImageLookupFilter filter;
    private DownLoadHandler handler = new DownLoadHandler();


    public EditSelectLayout getCropSelectCustomLayout() {
        return cropSelectCustomLayout;
    }

    public void setCropSelectCustomLayout(EditSelectLayout cropSelectCustomLayout) {
        this.cropSelectCustomLayout = cropSelectCustomLayout;
    }

    public Bitmap getSquareBitmap() {
        return squareBitmap;
    }

    public void setSquareBitmap(Bitmap squareBitmap) {
        this.squareBitmap = squareBitmap;
    }

    public Bitmap getCur_bitmap() {
        return cur_bitmap;
    }

    public void setCur_bitmap(Bitmap bitmap) {
        this.cur_bitmap = bitmap;
    }

    public Bitmap getSourceBitmap() {
        return sourceBitmap;
    }

    public void setSourceBitmap(Bitmap sourceBitmap) {
        this.sourceBitmap = sourceBitmap;
    }

    public HueEditBar getAdjustHueEditBar() {
        return adjustHueEditBar;
    }

    public void setAdjustHueEditBar(HueEditBar adjustHueEditBar) {
        this.adjustHueEditBar = adjustHueEditBar;
    }

    public TempEditBar getAdjustTempEditBar() {
        return adjustTempEditBar;
    }

    public void setAdjustTempEditBar(TempEditBar adjustTempEditBar) {
        this.adjustTempEditBar = adjustTempEditBar;
    }

    public TwoWayEditBar getAdjustTwoWayEditBar() {
        return adjustTwoWayEditBar;
    }

    public void setAdjustTwoWayEditBar(TwoWayEditBar adjustTwoWayEditBar) {
        this.adjustTwoWayEditBar = adjustTwoWayEditBar;
    }

    public OneWayEditBar getAdjustOneWayEditBar() {
        return adjustOneWayEditBar;
    }

    public void setAdjustOneWayEditBar(OneWayEditBar adjustOneWayEditBar) {
        this.adjustOneWayEditBar = adjustOneWayEditBar;
    }

    public ImageView getDiyImageSource() {
        return diyImageSource;
    }

    public void setDiyImageSource(ImageView diyImageSource) {
        this.diyImageSource = diyImageSource;
    }

    public FilterSelectLayout getFilterSelectCustomLayout() {
        return filterSelectCustomLayout;
    }

    public void setFilterSelectCustomLayout(FilterSelectLayout filterSelectCustomLayout) {
        this.filterSelectCustomLayout = filterSelectCustomLayout;
    }

    public GPUImageView getDiyImage() {
        return diyImage;
    }

    public void setDiyImage(GPUImageView diyImage) {
        this.diyImage = diyImage;
    }

    public FilterEditBar getFilterDiyEditBar() {
        return filterDiyEditBar;
    }

    public void setFilterDiyEditBar(FilterEditBar filterDiyEditBar) {
        this.filterDiyEditBar = filterDiyEditBar;
    }

    public GPUImageLookupFilter getFilter() {
        return filter;
    }

    public void setFilter(GPUImageLookupFilter filter) {
        this.filter = filter;
    }

    public int getFilter_position() {
        return filter_position;
    }

    public void setFilter_position(int filter_position) {
        this.filter_position = filter_position;
    }

    public String getPic_filepath() {
        return pic_filepath;
    }

    public void setPic_filepath(String pic_filepath) {
        this.pic_filepath = pic_filepath;
    }

    public CropControllerView getCropControllerView() {
        return cropControllerView;
    }

    public void setCropControllerView(CropControllerView cropControllerView) {
        this.cropControllerView = cropControllerView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diy);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Log.d(TAG, "onCreate: "+diyImage.getWidth()+" "+diyImage.getHeight());
        if (intent != null) {
            Bundle bundle = getIntent().getExtras();
            filter_position = bundle.getInt("position");
            pic_filepath = bundle.getString("pic_filepath");
        }
        diyContrast.setOnTouchListener(this::onTouch);
        Bitmap bitmap = BitmapFactory.decodeFile(pic_filepath);

        sourceBitmap = BitmapUtil.compressBitmap2Window(
                bitmap,MyApplication.phone_width*1.0f,MyApplication.phone_height*4.0f/5,false);
        cur_bitmap = BitmapUtil.compressBitmap2Window(
                bitmap,MyApplication.phone_width*1.0f,MyApplication.phone_height*4.0f/5,true);
        diyImageSource.setImageBitmap(sourceBitmap);
        diyImage.setScaleType(GPUImage.ScaleType.CENTER_INSIDE);

        diyImage.setImage(cur_bitmap);
        filter = new GPUImageLookupFilter();
        if (filter_position >= 0) {
            FilterBean bean = MyApplication.filterBeans.get(filter_position);
            LUT = bean.getLut();
            if (!MyApplication.filters.contains(bean.getLut())) {
                download(bean);
            } else {
                filter.setBackOld(0);
                filter.setBitmap(ImageLoaderUtil.getBitmapInFile(MyApplication.getContext(), LUT));
                diyImage.setFilter(filter);
            }
        }
    }


    private void download(FilterBean bean) {
        if (!NetWorkUtil.isNetWorkAvailable()) {
            ToastUtil.toast(MyApplication.getContext(), "下载失败，请检查网络");
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = MyApplication.PRE_URL + MyApplication.RESOURCE + bean.getPackageName()
                            + MyApplication.LUT_PIC + bean.getLut();
                    InputStream in = OKHttpUtil.getInputStream(url);
                    saveImage(DiyActivity.this, in, bean.getLut());
                    MyApplication.filters.add(bean.getLut());
                    Message message = new Message();
                    message.what = 1;
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

    public void saveImage(Context context, InputStream in, String name) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(name, Context.MODE_PRIVATE);
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @OnClick({R.id.diy_cancel, R.id.diy_done, R.id.diy_filter, R.id.diy_overlay, R.id.diy_adjust, R.id.diy_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.diy_cancel:
                finish();
                break;
            case R.id.diy_done:
                Bundle bundle = new Bundle();
//                Bitmap bitmap = Bitmap.createBitmap(cur_bitmap);
//                byte[] buf;
//                buf = BitmapUtil.bitmap2Bytes(bitmap);
//                bundle.putByteArray("save_pic",buf);
                startActivity(SaveActivity.class,bundle);
                break;
            case R.id.diy_filter:
                filter.setBackOld(0);
                diyImage.setFilter(filter);
//                diyImage.getGPUImage().deleteImage();
//                diyImage.setImage(cur_bitmap);
//                cur_bitmap = diyImage.getGPUImage().getBitmapWithFilterApplied();
                filterDiySelectLayout.setVisibility(View.VISIBLE);
                adjustSelectCustomLayout.setVisibility(View.INVISIBLE);
                cropSelectCustomLayout.setVisibility(View.INVISIBLE);
                cropControllerView.setVisibility(View.INVISIBLE);
                break;
            case R.id.diy_overlay:
                break;
            case R.id.diy_adjust:
//                filter.setBackOld(1);
//                diyImage.setFilter(filter);
//                cur_bitmap = diyImage.getGPUImage().getBitmapWithFilterApplied();
//                diyImage.getGPUImage().deleteImage();
//                diyImage.setImage(cur_bitmap);
                adjustSelectCustomLayout.setVisibility(View.VISIBLE);
                filterDiySelectLayout.setVisibility(View.INVISIBLE);
                cropSelectCustomLayout.setVisibility(View.INVISIBLE);
                cropControllerView.setVisibility(View.INVISIBLE);
                break;
            case R.id.diy_edit:
//                cur_bitmap = diyImage.getGPUImage().getBitmapWithFilterApplied();
//                filter.setBackOld(1);
//                diyImage.setFilter(filter);
//                diyImage.getGPUImage().deleteImage();
//                diyImage.setImage(cur_bitmap);
                cropSelectCustomLayout.setVisibility(View.VISIBLE);
                filterDiySelectLayout.setVisibility(View.INVISIBLE);
                adjustSelectCustomLayout.setVisibility(View.INVISIBLE);
                cropControllerView.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.diy_contrast) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    diyImageSource.setVisibility(View.VISIBLE);
                    diyImage.setVisibility(View.INVISIBLE);
                    break;
                case MotionEvent.ACTION_UP:
                    diyImageSource.setVisibility(View.INVISIBLE);
                    diyImage.setVisibility(View.VISIBLE);
                    break;
            }
            return true;
        }
        return false;
    }


    private class DownLoadHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    filter.setBackOld(0);
                    filter.setBitmap(ImageLoaderUtil.getBitmapInFile(MyApplication.getContext(), LUT));
                    diyImage.setFilter(filter);
                    break;
            }
        }
    }
}
