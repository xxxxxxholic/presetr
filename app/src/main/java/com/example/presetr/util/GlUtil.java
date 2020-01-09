package com.example.presetr.util;

import com.example.presetr.MyApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GlUtil {
    public static String getStringFromRaw(int resId,boolean is){
        InputStream inputStream = MyApplication.context.getResources().openRawResource(resId);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
