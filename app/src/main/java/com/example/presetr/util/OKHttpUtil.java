package com.example.presetr.util;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OKHttpUtil {
    private static OkHttpClient client = new OkHttpClient();
    public static InputStream getInputStream(String url){
        Request request = new Request.Builder()
                .url(url).build();
        InputStream inputStream = null;
        try {
            inputStream = client.newCall(request).execute()
                    .body().byteStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }
}
