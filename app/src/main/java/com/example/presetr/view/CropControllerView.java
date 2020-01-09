package com.example.presetr.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.presetr.R;
import com.example.presetr.util.DegreesUtil;

public class CropControllerView extends View {
    private static final String TAG = "CropControllerView";

    private float minEdge;
    private Context mContext;

    private String mShape;//free 1*1 3*4 4*3 9*16 16*9
    private float crop_width;
    private float crop_height;
    private int view_width;
    private int view_height;
    private float bitmap_w;
    private float bitmap_h;

    private float maxLeft;
    private float maxRight;
    private float maxTop;
    private float maxBottom;

    private Bitmap left_top_pic;
    private Bitmap right_top_pic ;
    private Bitmap left_bottom_pic;
    private Bitmap right_bottom_pic;
    private int pic_w;
    private int pic_h;

    public RectF selectRect = new RectF();
    private float select_top;
    private float select_left;
    private float select_bottom;
    private float select_right;


    private Paint maskPaint;
    private Paint selectPaint;
    private Paint linePaint;

    float last_x = 0.0f;
    float last_y = 0.0f;
    final float limit = 80.0f;
    float distance;
    float centerX;
    float centerY;

    public String getmShape() {
        return mShape;
    }

    public void setmShape(String mShape) {
        this.mShape = mShape;
        requestLayout();
        invalidate();
    }

    public float getCrop_width() {
        return crop_width;
    }

    public void setCrop_width(float crop_width) {
        this.crop_width = crop_width;
        requestLayout();
        invalidate();
    }

    public float getCrop_height() {
        return crop_height;
    }

    public void setCrop_height(float crop_height) {
        this.crop_height = crop_height;
        requestLayout();
        invalidate();
    }

    public float getBitmap_w() {
        return bitmap_w;
    }

    public void setBitmap_w(float bitmap_w) {
        this.bitmap_w = bitmap_w;
        maxLeft = (view_width - bitmap_w)/2;
        maxRight = maxLeft + bitmap_w;

    }

    public float getBitmap_h() {
        return bitmap_h;
    }

    public void setBitmap_h(float bitmap_h) {
        this.bitmap_h = bitmap_h;
        maxTop = (view_height - bitmap_h)/2;
        maxBottom = maxTop + bitmap_h;
    }

    public CropControllerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "CropControllerView: "+"构造器");
        mContext = context;

        int maskColor = Color.parseColor("#80000000");
        int selectColor = Color.parseColor("#00FFFFFF");
        int lineColor = Color.parseColor("#FFFFFF");

        maskPaint = new Paint();maskPaint.setAntiAlias(true);maskPaint.setColor(maskColor);
        selectPaint = new Paint();selectPaint.setAntiAlias(true);selectPaint.setColor(selectColor);
        linePaint = new Paint();linePaint.setAntiAlias(true);linePaint.setColor(lineColor);

        left_top_pic = BitmapFactory.decodeResource(context.getResources(),R.drawable.crop_top_left);
        right_top_pic = BitmapFactory.decodeResource(context.getResources(),R.drawable.crop_top_right);
        left_bottom_pic = BitmapFactory.decodeResource(context.getResources(),R.drawable.crop_bottom_left);
        right_bottom_pic = BitmapFactory.decodeResource(context.getResources(),R.drawable.crop_bottom_right);

        pic_h = left_bottom_pic.getHeight();
        pic_w = left_bottom_pic.getWidth();

        minEdge = (pic_h+pic_w)*1.5f;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d(TAG, "onLayout: ");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure: ");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        view_height = MeasureSpec.getSize(heightMeasureSpec);
        view_width = MeasureSpec.getSize(widthMeasureSpec);
        select_left = selectRect.left = (view_width-crop_width)/2;
        select_top = selectRect.top = (view_height-crop_height)/2;
        select_right = selectRect.right = selectRect.left + crop_width;
        select_bottom = selectRect.bottom = selectRect.top + crop_height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw: "+selectRect.toString());
        super.onDraw(canvas);
        canvas.drawRect(selectRect,selectPaint);

        drawMask(canvas);
        drawBitmap(canvas);
        drawLines(canvas);
    }
    private void drawMask(Canvas canvas){
        RectF top_mask = new RectF(0,0,view_width,selectRect.top);
        canvas.drawRect(top_mask,maskPaint);
        RectF bottom_mask = new RectF(0,selectRect.bottom,view_width,view_height);
        canvas.drawRect(bottom_mask,maskPaint);
        RectF left_mask = new RectF(0,selectRect.top,
                selectRect.left,selectRect.bottom);
        canvas.drawRect(left_mask,maskPaint);
        RectF right_mask = new RectF(selectRect.right,selectRect.top,
                view_width,selectRect.bottom);
        canvas.drawRect(right_mask,maskPaint);
    }
    private void drawBitmap(Canvas canvas){
        canvas.drawBitmap(left_top_pic,selectRect.left,selectRect.top,null);
        canvas.drawBitmap(right_top_pic,selectRect.right-pic_w
                ,selectRect.top,null);
        canvas.drawBitmap(left_bottom_pic,selectRect.left,
                selectRect.bottom-pic_h,null);
        canvas.drawBitmap(right_bottom_pic,selectRect.right-pic_w,
                selectRect.bottom-pic_h,null);
    }
    private void drawLines(Canvas canvas){
        canvas.drawLine(selectRect.left,selectRect.top,
                selectRect.left,selectRect.bottom,linePaint);
        canvas.drawLine(selectRect.right,selectRect.top,
                selectRect.right,selectRect.bottom,linePaint);
        canvas.drawLine(selectRect.left,selectRect.top,
                selectRect.right,selectRect.top,linePaint);
        canvas.drawLine(selectRect.left,selectRect.bottom,
                selectRect.right,selectRect.bottom,linePaint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getPointerCount()==1){
            return singleFingueHandler(event);
        }else if(event.getPointerCount()==2){
            return doubleFingueHandler(event);
        }
        return false;
    }

    private boolean doubleFingueHandler( MotionEvent event) {
        switch (event.getAction()&MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_POINTER_DOWN:
                if(event.getPointerCount()==2) {
                    distance = DegreesUtil.getDistanceOfFingue(event);
                    centerX = (select_left + select_right) / 2;
                    centerY = (select_bottom + select_top) / 2;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float delta_dis = DegreesUtil.getDistanceOfFingue(event)/distance;
                doubleFHandlerHelper(delta_dis);
                invalidate();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                select_left =  selectRect.left;
                select_right =  selectRect.right;
                select_top =  selectRect.top;
                select_bottom =  selectRect.bottom;
                break;
        }
        return true;
    }
    private void doubleFHandlerHelper(float delta_dis){
        if(!((selectRect.right>=maxRight||selectRect.left<=maxLeft||selectRect.top<=maxTop||selectRect.bottom>=maxBottom)
                &&delta_dis>1)) {
            if(!(((selectRect.bottom-selectRect.top)<=minEdge&&delta_dis<1)||((selectRect.right-selectRect.left)<=minEdge)&&delta_dis<1)) {
                float bottom = (select_bottom - centerY) * delta_dis + centerY;
                selectRect.bottom = (bottom >= maxBottom) ? maxBottom : bottom;
                float right = (select_right - centerX) * delta_dis + centerX;
                selectRect.right = (right >= maxRight) ? maxRight : right;
                float left = (select_left - centerX) * delta_dis + centerX;
                selectRect.left = (left <= maxLeft) ? maxLeft : left;
                float top = (select_top - centerY) * delta_dis + centerY;
                selectRect.top = (top <= maxTop) ? maxTop : top;
            }
        }
    }

    private boolean singleFingueHandler( MotionEvent event) {
        switch (event.getAction()&MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                last_x = event.getX();
                last_y = event.getY();
                if(!(last_x>=selectRect.left&&last_x<=selectRect.right
                        &&last_y>=selectRect.top&&last_y<=selectRect.bottom)){
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float dis_x = event.getX() - last_x;
                float dis_y = event.getY() - last_y;
                if (last_x >= select_left && last_x <= select_left + limit
                        && last_y >= select_top && last_y <= select_top + limit) {//左上角
                        editLeftTop(dis_x,dis_y);
                    } else if (last_x >= select_right - limit && last_x <= select_right
                            && last_y >= select_top && last_y <= select_top + limit) {//右上角
                        editRightTop(dis_x,dis_y);
                    } else if (last_x >= select_left && last_x <= select_left + limit
                            && last_y >= select_bottom - limit && last_y <= select_bottom) {//左下角
                        editLeftBottom(dis_x,dis_y);
                    } else if (last_x >= select_right - limit && last_x <= select_right
                            && last_y >= select_bottom - limit && last_y <= select_bottom) {//右下角
                        editRightBottom(dis_x,dis_y);
                    } else if (last_x >= select_left + pic_w && last_x <= select_right - pic_w
                            && last_y >= select_top + pic_h && last_y <= select_bottom - pic_h) {//单指拖动
                        moveSingleFin(dis_x,dis_y);
                   }
                this.invalidate();
                break;
            case MotionEvent.ACTION_UP:
                select_left =  selectRect.left;
                select_right =  selectRect.right;
                select_top =  selectRect.top;
                select_bottom =  selectRect.bottom;
                break;
        }
        return true;
    }

    private void moveSingleFin(float dis_x, float dis_y) {
        if(dis_x<=0&&dis_y<=0){
            selectRect.left = (select_left + dis_x)<=maxLeft?maxLeft:(select_left + dis_x);
            selectRect.top = (select_top + dis_y)<=maxTop?maxTop:(select_top + dis_y);
            selectRect.right = selectRect.left+(select_right-select_left);
            selectRect.bottom = selectRect.top + (select_bottom - select_top);
        }else if(dis_x<=0&&dis_y>=0){
            selectRect.left = (select_left + dis_x)<=maxLeft?maxLeft:(select_left + dis_x);
            selectRect.bottom = (select_bottom + dis_y)>=maxBottom?maxBottom:(select_bottom + dis_y);
            selectRect.right = selectRect.left+(select_right-select_left);
            selectRect.top = selectRect.bottom - (select_bottom - select_top);
        }else if(dis_x>=0&&dis_y<=0){
            selectRect.right = (select_right + dis_x)>=maxRight?maxRight:(select_right + dis_x);
            selectRect.top = (select_top + dis_y)<=maxTop?maxTop:(select_top + dis_y);
            selectRect.left = selectRect.right - (select_right-select_left);
            selectRect.bottom = selectRect.top + (select_bottom - select_top);
        }else if(dis_x>=0&&dis_y>=0){
            selectRect.right = (select_right + dis_x)>=maxRight?maxRight:(select_right + dis_x);
            selectRect.bottom = (select_bottom + dis_y)>=maxBottom?maxBottom:(select_bottom + dis_y);
            selectRect.left = selectRect.right - (select_right-select_left);
            selectRect.top = selectRect.bottom - (select_bottom - select_top);
        }
    }

    private void editRightBottom(float dis_x, float dis_y) {
        switch (mShape){
            case "free":
                if(!(((selectRect.bottom-selectRect.top)<=minEdge)&&dis_y<0))
                    selectRect.bottom = (select_bottom + dis_y)>=maxBottom?maxBottom:(select_bottom + dis_y);
                if(!(((selectRect.right-selectRect.left)<=minEdge)&&dis_x<0))
                    selectRect.right = (select_right + dis_x)>=maxRight?maxRight:(select_right + dis_x);
                break;
            case "1*1":
                editRightBottomHelper(dis_x,dis_y,1.0f,1.0f);
                break;
            case "3*4":
                if(!((selectRect.right-selectRect.left<=minEdge&&dis_x<0)))
                    editRightBottomHelper(dis_x,dis_y,3.0f,4.0f);
                break;
            case "4*3":
                if(!(((selectRect.bottom-selectRect.top)<=minEdge&&dis_y<0)))
                    editRightBottomHelper(dis_x,dis_y,4.0f,3.0f);
                break;
            case "9*16":
                if(!((selectRect.right-selectRect.left<=minEdge&&dis_x<0)))
                    editRightBottomHelper(dis_x,dis_y,9.0f,16.0f);
                break;
            case "16*9":
                if(!((selectRect.bottom-selectRect.top<=minEdge&&dis_y<0)))
                    editRightBottomHelper(dis_x,dis_y,16.0f,9.0f);
                break;
        }
    }
    private void editRightBottomHelper(float dis_x,float dis_y,float w,float h){
        if(bitmap_w>=bitmap_h){
            if(!(((selectRect.bottom-selectRect.top)<=minEdge&&dis_y<0))&&(selectRect.right<=maxRight)) {
                selectRect.bottom = (select_bottom + dis_y);
                if (selectRect.bottom >= maxBottom)
                    selectRect.bottom = maxBottom;
                selectRect.right = select_left + (selectRect.bottom - select_top) * w / h;
            }
        }else {
            if(!((selectRect.right-selectRect.left<=minEdge&&dis_x<0))&&(selectRect.bottom<=maxBottom)) {
                selectRect.right = select_right + dis_x;
                if (selectRect.right >= maxRight)
                    selectRect.right = maxRight;
                selectRect.bottom = select_top + ((selectRect.right - select_left) * h / w);
            }
        }
    }

    private void editLeftBottom( float dis_x, float dis_y) {
        switch (mShape){
            case "free":
                if(!(((selectRect.bottom-selectRect.top)<=minEdge)&&dis_y<0))
                    selectRect.bottom = (select_bottom + dis_y)>=maxBottom?maxBottom:(select_bottom + dis_y);
                if(!(((selectRect.right-selectRect.left)<=minEdge)&&dis_x>0))
                    selectRect.left = (select_left + dis_x)<=maxLeft?maxLeft:(select_left + dis_x);
                break;
            case "1*1":
                editLeftBottomHelper(dis_x,dis_y,1.0f,1.0f);
                break;
            case "3*4":
                if(!((selectRect.right-selectRect.left<=minEdge&&dis_x>0)))
                    editLeftBottomHelper(dis_x,dis_y,3.0f,4.0f);
                break;
            case "4*3":
                if(!(((selectRect.bottom-selectRect.top)<=minEdge&&dis_y<0)))
                    editLeftBottomHelper(dis_x,dis_y,4.0f,3.0f);
                break;
            case "9*16":
                if(!((selectRect.right-selectRect.left<=minEdge&&dis_x>0)))
                    editLeftBottomHelper(dis_x,dis_y,9.0f,16.0f);
                break;
            case "16*9":
                if(!((selectRect.bottom-selectRect.top<=minEdge&&dis_y<0)))
                    editLeftBottomHelper(dis_x,dis_y,16.0f,9.0f);
                break;
        }
    }
    private void editLeftBottomHelper(float dis_x,float dis_y,float w,float h){
        if(bitmap_w>=bitmap_h){
            if(!(((selectRect.bottom-selectRect.top)<=minEdge&&dis_y<0))&&(selectRect.left>=maxLeft)) {
                selectRect.bottom = (select_bottom + dis_y);
                if (selectRect.bottom >= maxBottom)
                    selectRect.bottom = maxBottom;
                selectRect.left = select_right - (selectRect.bottom - select_top) * w / h;
            }
        }else {
            if(!((selectRect.right-selectRect.left<=minEdge&&dis_x>0))&&(selectRect.bottom<=maxBottom)) {
                selectRect.left = select_left + dis_x;
                if (selectRect.left <= maxLeft)
                    selectRect.left = maxLeft;
                selectRect.bottom = select_top + ((select_right - selectRect.left) * h / w);
            }
        }
    }

    private void editRightTop(float dis_x, float dis_y) {
        switch (mShape){
            case "free":
                if(!(((selectRect.bottom-selectRect.top)<=minEdge)&&dis_y>0))
                    selectRect.top = (select_top + dis_y)<=maxTop?maxTop:(select_top + dis_y);
                if(!(((selectRect.right-selectRect.left)<=minEdge)&&dis_x<0))
                    selectRect.right = (select_right + dis_x)>=maxRight?maxRight:(select_right + dis_x);
                break;
            case "1*1":
                editRightTopHelper(dis_x,dis_y,1.0f,1.0f);
                break;
            case "3*4":
                if(!((selectRect.right-selectRect.left<=minEdge&&dis_x<0)))
                    editRightTopHelper(dis_x,dis_y,3.0f,4.0f);
                break;
            case "4*3":
                if(!((selectRect.bottom-selectRect.top<=minEdge&&dis_y>0)))
                    editRightTopHelper(dis_x,dis_y,4.0f,3.0f);
                break;
            case "9*16":
                if(!((selectRect.right-selectRect.left<=minEdge&&dis_x<0)))
                    editRightTopHelper(dis_x,dis_y,9.0f,16.0f);
                break;
            case "16*9":
                if(!((selectRect.bottom-selectRect.top<=minEdge&&dis_y>0)))
                   editRightTopHelper(dis_x,dis_y,16.0f,9.0f);
                break;
        }
    }
    private void editRightTopHelper(float dis_x,float dis_y,float w,float h){
        if(bitmap_w>=bitmap_h){
            if(!(((selectRect.bottom-selectRect.top)<=minEdge&&dis_y>0))&&(selectRect.right<=maxRight)) {
                selectRect.top = (select_top + dis_y);
                if (selectRect.top <= maxTop)
                    selectRect.top = maxTop;
                selectRect.right = select_left + (select_bottom - selectRect.top) * w / h;
            }
        }else {
            if(!((selectRect.right-selectRect.left<=minEdge&&dis_x<0))&&(selectRect.top>=maxTop)) {
                selectRect.right = select_right + dis_x;
                if (selectRect.right >= maxRight)
                    selectRect.right = maxRight;
                selectRect.top = select_bottom - ((selectRect.right - select_left) * h / w);
            }
        }
    }

    private void editLeftTop( float dis_x, float dis_y){
        switch (mShape){
            case "free":
                if(!(((selectRect.bottom-selectRect.top)<=minEdge)&&dis_y>0))
                    selectRect.top = (select_top + dis_y)<=maxTop?maxTop:(select_top + dis_y);
                if(!(((selectRect.right-selectRect.left)<=minEdge)&&dis_x>0))
                    selectRect.left = (select_left + dis_x)<=maxLeft?maxLeft:(select_left + dis_x);
                break;
            case "1*1":
                editLeftTopHelper(dis_x,dis_y,1.0f,1.0f);
                break;
            case "3*4":
                if(!((selectRect.right-selectRect.left<=minEdge&&dis_x>0)))
                    editLeftTopHelper(dis_x,dis_y,3.0f,4.0f);
                break;
            case "4*3":
                if(!((selectRect.bottom-selectRect.top<=minEdge&&dis_y>0)))
                    editLeftTopHelper(dis_x,dis_y,4.0f,3.0f);
                break;
            case "9*16":
                if(!((selectRect.right-selectRect.left<=minEdge&&dis_x>0)))
                    editLeftTopHelper(dis_x,dis_y,9.0f,16.0f);
                break;
            case "16*9":
                if(!((selectRect.bottom-selectRect.top<=minEdge&&dis_y>0)))
                    editLeftTopHelper(dis_x,dis_y,16.0f,9.0f);
                break;
        }
    }
    private void editLeftTopHelper(float dis_x,float dis_y,float w,float h){
        if(bitmap_w>=bitmap_h){
            if(!(((selectRect.bottom-selectRect.top)<=minEdge&&dis_y>0))&&(selectRect.left>=maxLeft)) {
                selectRect.top = (select_top + dis_y);
                if (selectRect.top <= maxTop)
                    selectRect.top = maxTop;
                selectRect.left = select_right - (select_bottom - selectRect.top) * w / h;
            }
        }else {
            if(!((selectRect.right-selectRect.left<=minEdge&&dis_x>0))&&(selectRect.top>=maxTop)) {
                selectRect.left = select_left + dis_x;
                if (selectRect.left <= maxLeft)
                    selectRect.left = maxLeft;
                selectRect.top = select_bottom - ((select_right - selectRect.left) * h / w);
            }
        }
    }

}
