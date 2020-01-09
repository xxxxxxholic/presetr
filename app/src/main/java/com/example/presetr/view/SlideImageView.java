package com.example.presetr.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.presetr.R;
import com.example.presetr.filter.GPUImageLookupFilterForSliderImage;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

public class SlideImageView extends ConstraintLayout implements View.OnTouchListener {
    @BindView(R.id.render_image)
    GPUImageView renderImage;
    @BindView(R.id.slider)
    SliderBarImageView slider;

    private int vw;
    private float slider_x;
    private Context mContext;
    private GPUImageLookupFilterForSliderImage filter;
    private float last_x;
    private Bitmap mBitmap;

    private volatile boolean isFirstCreate;

    public boolean getIsFirstCreate() {
        return isFirstCreate;
    }

    public void setIsFirstCreate(boolean firstCreate) {
        isFirstCreate = firstCreate;
    }

    public void setRenderImage(Bitmap bitmap) {
        renderImage.setImage(bitmap);
        mBitmap = bitmap;
    }

    public void setFilter() {
        //this.filter = filter;
        this.filter = new GPUImageLookupFilterForSliderImage();
        this.filter.setBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.co01));
        renderImage.setFilter(filter);
    }

    public SlideImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.slider_image_view_layout, this);
        ButterKnife.bind(this, view);
        slider.setListener(this::onTouch);
        isFirstCreate = true;
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void startAnimation(){
        if(isFirstCreate){
            ObjectAnimator animator = ObjectAnimator.ofFloat(
                    slider,"translationX",vw/5.0f
            );
            animator.setDuration(2000);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float translate_x = slider.getTranslationX();
                    filter.setDivide_x((translate_x + slider.getWidth() / 2) * mBitmap.getWidth() / vw / mBitmap.getWidth());
                    renderImage.setFilter(filter);
                }
            });
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isFirstCreate = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        vw = MeasureSpec.getSize(widthMeasureSpec);
        slider_x = 4 * vw / 5.0f;
        filter.setDivide_x((slider_x + 45 / 2.0f) * mBitmap.getWidth() / vw / mBitmap.getWidth());
        renderImage.setFilter(filter);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        slider.setTranslationX(slider_x);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v.getId()==R.id.slider) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    last_x = event.getRawX();
                    if (last_x < -slider.getWidth() / 2.0f) last_x = -slider.getWidth() / 2.0f;
                    if (last_x > vw - slider.getWidth()) last_x = vw - slider.getWidth();
                    slider.setTranslationX(last_x);
                    filter.setDivide_x((last_x + slider.getWidth() / 2) * mBitmap.getWidth() / vw / mBitmap.getWidth());
                    renderImage.setFilter(filter);
                    break;
                case MotionEvent.ACTION_UP:
                    slider_x = last_x;
                    break;
            }
            return true;
        }
        return false;
    }
}
