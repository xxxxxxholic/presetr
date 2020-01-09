package com.example.presetr.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.presetr.R;
import com.example.presetr.activity.DiyActivity;
import com.example.presetr.util.BitmapUtil;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RotateEditBar extends ConstraintLayout implements View.OnTouchListener {

    @BindView(R.id.rotate_edit_bar_slider_gray)
    ImageView rotateEditBarSliderGray;
    @BindView(R.id.rotate_edit_bar_slider_circle)
    ImageView rotateEditBarSliderCircle;
    @BindView(R.id.rotate_select_cancel)
    ImageView rotateSelectCancel;
    @BindView(R.id.rotate_select_done)
    ImageView rotateSelectDone;

    private Context mContext;
    private DiyActivity activity;
    private int progress;
    private float last_x;
    private float dis;
    private float curr_dis;
    private Bitmap cur_bitmap;



    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        if (progress < -45) progress = -45;
        if (progress > 45) progress = 45;
        this.progress = progress;
        float r = rotateEditBarSliderGray.getWidth() - rotateEditBarSliderCircle.getWidth();
        float tx = r * ((progress + 45) / 90.0f);
        rotateEditBarSliderCircle.setTranslationX(tx);
        curr_dis = tx;
    }

    public RotateEditBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        activity = (DiyActivity) context;
        View view = LayoutInflater.from(context).inflate(R.layout.rotate_edit_bar, this);
        ButterKnife.bind(this, view);
        rotateEditBarSliderCircle.setOnTouchListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rotateEditBarSliderCircle.setTranslationX(
                (rotateEditBarSliderGray.getWidth() - rotateEditBarSliderCircle.getWidth()) / 90.0f * ((progress + 45)));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.rotate_edit_bar_slider_circle) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    last_x = event.getRawX();
                    dis = curr_dis;
                    break;
                case MotionEvent.ACTION_MOVE:
                    float x = event.getRawX();
                    float change_dis = x - last_x;
                    if (dis + change_dis > (rotateEditBarSliderGray.getRight() - rotateEditBarSliderCircle.getRight())) {
                        change_dis = rotateEditBarSliderGray.getRight() - rotateEditBarSliderCircle.getRight() - dis;
                    }
                    if (dis + change_dis < rotateEditBarSliderGray.getLeft() - rotateEditBarSliderCircle.getLeft()) {
                        change_dis = rotateEditBarSliderGray.getLeft() - rotateEditBarSliderCircle.getLeft() - dis;
                    }
                    rotateEditBarSliderCircle.setTranslationX(dis + change_dis);
                    float s = rotateEditBarSliderCircle.getLeft() + rotateEditBarSliderCircle.getTranslationX() - rotateEditBarSliderGray.getLeft();
                    float t = rotateEditBarSliderGray.getWidth() - rotateEditBarSliderCircle.getWidth() * 1.0f;
                    progress = (int) ((s / t) * 90) - 45;
                    curr_dis = dis + change_dis;

//                    int ro_Rotation = activity.getCropSelectCustomLayout().getRo_Rotation();
                    activity.getCropSelectCustomLayout().setTileRotation(progress);

                    if(cur_bitmap==null) {
                        cur_bitmap = activity.getCur_bitmap();
                    }

                    Matrix matrix = new Matrix();
                    matrix.setRotate(progress);
                    Bitmap bitmap1 = Bitmap.createBitmap(cur_bitmap,0,0,cur_bitmap.getWidth(),cur_bitmap.getHeight(),matrix,false);

                    activity.getDiyImage().getGPUImage().deleteImage();
                    //activity.getDiyImage().setRotation(ro_Rotation+progress);
                    activity.getDiyImage().setImage(bitmap1);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return true;
        }
        return false;
    }

    @OnClick({R.id.rotate_select_cancel, R.id.rotate_select_done})
    public void onViewClicked(View view) {
        int ro_Rotation = activity.getCropSelectCustomLayout().getRo_Rotation();
        switch (view.getId()) {
            case R.id.rotate_select_cancel:
                this.setVisibility(INVISIBLE);
                activity.getCropSelectCustomLayout().setTileRotation(0);
                setProgress(0);
                activity.getDiyImage().getGPUImage().deleteImage();
                activity.getDiyImage().setImage(activity.getCur_bitmap());
                break;
            case R.id.rotate_select_done:
                this.setVisibility(INVISIBLE);
                activity.getCropSelectCustomLayout().setTileRotation(progress);
                Matrix matrix = new Matrix();
                matrix.setRotate(progress);
                Bitmap bitmap1 = Bitmap.createBitmap(cur_bitmap,0,0,cur_bitmap.getWidth(),cur_bitmap.getHeight(),matrix,false);
                activity.setCur_bitmap(bitmap1);//activity.getDiyImage().getGPUImage().getBitmapWithFilterApplied()
                break;
        }
    }
}
