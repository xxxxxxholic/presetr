package com.example.presetr.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.example.presetr.MyApplication;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitmapUtil {
    public static Bitmap compressBitmap2Window(Bitmap sourceBitmap,float vw,float vh,boolean isRecycle){
        float w = sourceBitmap.getWidth();
        float h = sourceBitmap.getHeight();
        float new_h;
        float new_w;
        if(w/h<=vw/vh){
            new_h = vh;
            new_w = new_h*w/h;
        }else {
            new_w = vw;
            new_h = new_w*h/w;
        }
        return changeSize(sourceBitmap,(int)new_w,(int)new_h,isRecycle);
    }
    public static void saveImage(Context context, Bitmap bitmap, String name){
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(name,Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static byte[] bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap squareFill(Bitmap bitmap_org){
       //saveImage(MyApplication.context,bitmap_org,"kkkkkk.png");
        int pixel = bitmap_org.getPixel(bitmap_org.getWidth()-1,bitmap_org.getHeight()-1);
         int a = Color.alpha(pixel);
        int r = Color.red(pixel);
        int g = Color.green(pixel);
        int b = Color.blue(pixel);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Color color = Color.valueOf(r,g,b);
        }
        //获取原始宽高，并获取宽高中较大的
        int nWidth = bitmap_org.getWidth();
        int nHeight = bitmap_org.getHeight();
        int nMax = Math.max(nWidth,nHeight);
        //新建一个正方形的bitmap
        Bitmap  bitmap = Bitmap.createBitmap( nMax, nMax, Bitmap.Config.ARGB_8888);
        float left = 0;
        float top = 0;
        if ( nWidth >= nHeight) {
            int nLen= nWidth - nHeight ;
            top =  (nLen / 2.0f) ;
        }
        else{
            int nLen=  nHeight - nWidth  ;
            left =  (nLen / 2.0f) ;
        }
        Canvas canvas = new Canvas( bitmap );


        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawBitmap( bitmap_org,  left ,  top, paint );
        canvas.drawColor(Color.WHITE,PorterDuff.Mode.DST_OVER);
        return bitmap;

    }
    public static Bitmap shotView(View v){
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(),v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }
    public static Bitmap editBitmapByBitmap(Bitmap dest,Bitmap src,PorterDuff.Mode mode){
        Bitmap bitmap = Bitmap.createBitmap(dest.getWidth(),dest.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(mode));
        canvas.drawBitmap(dest,0,0,null);
        canvas.drawBitmap(src,(dest.getWidth()-src.getWidth())/2,(dest.getHeight()-src.getHeight())/2,paint);
        return bitmap;
    }
    public static Bitmap editBitmapByBitmap(Bitmap dest,Bitmap src){
        Bitmap bitmap = Bitmap.createBitmap(dest.getWidth(),dest.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        canvas.drawBitmap(dest,0,0,null);
        canvas.drawBitmap(src,(dest.getWidth()-src.getWidth())/2,(dest.getHeight()-src.getHeight())/2,paint);
        return bitmap;
    }
    public static Bitmap editBitmapByBitmap(Bitmap dest,Bitmap src,PorterDuff.Mode mode,int x,int y){

        Bitmap bitmap = Bitmap.createBitmap(dest.getWidth(),dest.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(mode));
        canvas.drawBitmap(dest,0,0,null);
        canvas.drawBitmap(src,x,y,paint);
        return bitmap;
    }

    public static Bitmap changeSize(Bitmap bitmap,int n_w,int n_h,boolean isRecycle){

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleX =((float) n_w)/w;
        float scaleY = ((float) n_h)/h;
        matrix.setScale(scaleX,scaleY);
        Bitmap nBitmap =Bitmap.createBitmap(bitmap,0,0,w,h,matrix,true);
        if(isRecycle) {
            if (!bitmap.isRecycled() && bitmap != nBitmap) {
                bitmap.recycle();
            }
        }
        return nBitmap;
    }

}
