package com.example.presetr.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.presetr.MyApplication;

public class NetWorkUtil {
    public static boolean isNetWorkAvailable(){
        ConnectivityManager manager = (ConnectivityManager) MyApplication.context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isAvailable()) {
            return false;
        } else {
            return true;
        }
    }
}
