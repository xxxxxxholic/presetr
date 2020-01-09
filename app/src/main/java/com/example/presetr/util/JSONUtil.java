package com.example.presetr.util;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.presetr.model.FilterPackageBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JSONUtil {
    public static Gson gson = new Gson();
    public static String getJsonString(Context context,String path) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(path)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    public static FilterPackageBean parseJson(String s){
        FilterPackageBean filterPackage = gson.fromJson(s,FilterPackageBean.class);
        return filterPackage;
    }

    public static List<FilterPackageBean> parseJsonArray(String jsonStr){
        List<FilterPackageBean> list = new ArrayList<>(50);
        list = gson.fromJson(jsonStr,new TypeToken<List<FilterPackageBean>>(){}.getType());
        return list;
    }
}
