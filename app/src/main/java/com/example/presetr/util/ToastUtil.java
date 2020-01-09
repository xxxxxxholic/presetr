package com.example.presetr.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    public static void toast(Context context,String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }
}
