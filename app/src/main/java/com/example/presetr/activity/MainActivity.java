package com.example.presetr.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.presetr.MyApplication;
import com.example.presetr.R;
import com.example.presetr.adapter.MainFilterAdapter;
import com.example.presetr.model.AdjustBean;
import com.example.presetr.model.FilterBean;
import com.example.presetr.model.FilterPackageBean;
import com.example.presetr.util.JSONUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.main_setting_view)
    ImageView mainSettingView;
    @BindView(R.id.main_purchase_view)
    ImageView mainPurchaseView;
    @BindView(R.id.main_recycle_view)
    RecyclerView mainRecycleView;
    @BindView(R.id.main_presets)
    TextView mainPresets;
    @BindView(R.id.main_darkroom)
    TextView mainDarkroom;

    private MainFilterAdapter mainFilterAdapter;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: "+ Environment.getExternalStorageDirectory().getAbsolutePath());
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        MyApplication.phone_width = point.x;
        MyApplication.phone_height = point.y;
        //解析滤镜包配置
        String[] paths = getJSON("filtersConfig");
        initFilterPackage(paths);
        String[] adjust_paths = getJSON("adjustConfig");
        initAdjustBeans(adjust_paths);
        //recyclerView设置
        LinearLayoutManager lManager = new LinearLayoutManager(this);
        lManager.setOrientation(LinearLayoutManager.VERTICAL);
        mainRecycleView.setLayoutManager(lManager);
        mainFilterAdapter = new MainFilterAdapter(MyApplication.filterPackages);
        mainRecycleView.setAdapter(mainFilterAdapter);
//        mainRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if(newState==RecyclerView.SCROLL_STATE_IDLE){
//                    Glide.with(recyclerView.getContext()).pauseAllRequests();
//                }
//                if(newState==RecyclerView.SCROLL_STATE_DRAGGING){
//                    Glide.with(recyclerView.getContext()).resumeRequests();
//                }
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });
        //申请权限
        if((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED)
        ||(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED)
        ||(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!=
                PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
            },1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if(grantResults.length<=0||grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    finish();
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }

    @OnClick({R.id.main_setting_view, R.id.main_purchase_view, R.id.main_darkroom})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_setting_view:
                clickSetting();
                break;
            case R.id.main_purchase_view:
                clickPurchase();
                break;
            case R.id.main_darkroom:
                clickDarkroom();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    private void clickDarkroom() {
        startActivity(DarkroomActivity.class);
    }


    public void clickSetting(){

    }
    public void clickPurchase(){
        startActivity(PurchaseActivity.class);
    }


    private String[] getJSON(String path){
        AssetManager assetManager = getAssets();
        String[] list = null;
        try {
            list = assetManager.list(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
    private void initFilterPackage(String[] paths) {
        FilterPackageBean bean = null;
        for(int i=0;i<paths.length;i++){
            String s = "filtersConfig/"+paths[i];
            String json = JSONUtil.getJsonString(this,s);
            bean = JSONUtil.parseJson(json);
            MyApplication.filterPackages.add(bean);
            MyApplication.filterPackagesMap.put(bean.getDisplayName(),bean);
        }
        int count = 0;
        for(int i=0;i<MyApplication.filterPackages.size();i++){
            String name = MyApplication.filterPackages.get(i).getName();
            String displayName = MyApplication.filterPackages.get(i).getDisplayName();
            List<FilterBean> list = MyApplication.filterPackages.get(i).getFilters();
            for(int j=0;j<list.size();j++){
                FilterBean filterBean = list.get(j);
                filterBean.setPosition(count);
                filterBean.setPackageName(name);
                filterBean.setDisplayName(displayName);
                MyApplication.filterBeans.add(filterBean);
                MyApplication.filterBeanMap.put(filterBean.getPreview(),filterBean);
                count++;
            }
        }
        List<String> li =  Arrays.asList(fileList());
        MyApplication.filters = new ArrayList<>(li);
    }
    private void initAdjustBeans(String[] paths){
        AdjustBean bean = null;
        Gson gson = new Gson();
        for(int i=0;i<paths.length;i++){
            String s = "adjustConfig/"+paths[i];
            String json = JSONUtil.getJsonString(this,s);
            bean = gson.fromJson(json,AdjustBean.class);
            MyApplication.adjustBeans.add(bean);
        }
    }


}
