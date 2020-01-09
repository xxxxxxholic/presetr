package com.example.presetr.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ImageLoaderUtil {
    public static Bitmap getBitmapInFile(Context context,String name) {
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = context.openFileInput(name);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }
    public static Bitmap getBitmapByName(Context context ,String name){
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            Context mCon = context;
            is = context.getAssets().open(name);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(is!=null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static Drawable getDrawableByName(Context context,String name){
        InputStream is = null;
        Drawable drawable = null;
        Bitmap bitmap = null;
        try {
            is = context.getAssets().open(name);
            bitmap = BitmapFactory.decodeStream(is);
            drawable = new BitmapDrawable(context.getResources(),bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return drawable;
    }
}
