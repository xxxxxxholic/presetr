package com.example.presetr;

import android.app.Application;
import android.content.Context;

import com.example.presetr.model.AdjustBean;
import com.example.presetr.model.FilterBean;
import com.example.presetr.model.FilterPackageBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyApplication extends Application {
    //域名
    public static final String PRE_URL = "http://10.17.1.11/as/presetr_a/";
    public static final String RESOURCE = "filters/";

    public static final String COVER_PIC = "/coverPic/";
    public static final String LUT_PIC = "/LUTPic/";
    public static final String PREVIEW_PIC = "/previewPic/";
    public static final String THUMBNAIL_PIC = "/thumbnailPic/";

    //滤镜LUT文件所在项目的文件夹中已有的滤镜
    public static List<String> filters = new ArrayList<>(50);

    //滤镜包配置
    public static List<FilterPackageBean> filterPackages = new ArrayList<>(50);
    //key为displayName
    public static HashMap<String,FilterPackageBean> filterPackagesMap = new HashMap<>(50);

    //所有的滤镜
    public static List<FilterBean> filterBeans = new ArrayList<>(50);
    //key为preview
    public static HashMap<String,FilterBean> filterBeanMap = new HashMap<>(50);

    //画面调整配置
    public static List<AdjustBean> adjustBeans = new ArrayList<>(20);

    public static Context context;

    //设备的宽
    public static int phone_width;
    public static int phone_height;
    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
