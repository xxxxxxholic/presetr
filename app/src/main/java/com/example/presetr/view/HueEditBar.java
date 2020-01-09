package com.example.presetr.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.presetr.R;
import com.example.presetr.activity.DiyActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HueEditBar extends ConstraintLayout implements View.OnTouchListener {

    @BindView(R.id.hue_edit_bar_slider_gray)
    ImageView hueEditBarSliderGray;
    @BindView(R.id.hue_edit_bar_slider_circle)
    ImageView hueEditBarSliderCircle;
    @BindView(R.id.hue_edit_bar_progress)
    TextView hueEditBarProgress;
    @BindView(R.id.hue_edit_bar_cancel)
    ImageView hueEditBarCancel;
    @BindView(R.id.hue_edit_bar_confirm)
    ImageView hueEditBarConfirm;
    private Context mContext;
    private DiyActivity activity;
    private int progress;
    private float last_x;
    private float dis;
    private float curr_dis;
    private OneWayEditBar.OnSlidingListener slidingListener;
    private OneWayEditBar.OnClickCancelListener clickCancelListener;
    private OneWayEditBar.OnClickDoneListener clickDoneListener;

    public void setSlidingListener(OneWayEditBar.OnSlidingListener slidingListener) {
        this.slidingListener = slidingListener;
    }

    public void setClickCancelListener(OneWayEditBar.OnClickCancelListener clickCancelListener) {
        this.clickCancelListener = clickCancelListener;
    }

    public void setClickDoneListener(OneWayEditBar.OnClickDoneListener clickDoneListener) {
        this.clickDoneListener = clickDoneListener;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        if (progress < 0) progress = 0;
        if (progress > 100) progress = 100;
        this.progress = progress;
        hueEditBarProgress.setText(progress + "");
        hueEditBarProgress.setTranslationX(
                (hueEditBarSliderGray.getWidth() - hueEditBarSliderCircle.getWidth()) / 100.0f * progress);
        hueEditBarSliderCircle.setTranslationX(
                (hueEditBarSliderGray.getWidth() - hueEditBarSliderCircle.getWidth()) / 100.0f * progress);
        curr_dis = (hueEditBarSliderGray.getWidth() - hueEditBarSliderCircle.getWidth()) / 100.0f * progress;
    }

    public HueEditBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        activity = (DiyActivity) context;
        View view = LayoutInflater.from(context).inflate(R.layout.adjust_hue_edit_bar, this);
        ButterKnife.bind(this, view);
        hueEditBarSliderCircle.setOnTouchListener(this);
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
        hueEditBarProgress.setText(progress + "");
        hueEditBarProgress.setTranslationX(
                (hueEditBarSliderGray.getWidth() - hueEditBarSliderCircle.getWidth()) / 100.0f * progress);
        hueEditBarSliderCircle.setTranslationX(
                (hueEditBarSliderGray.getWidth() - hueEditBarSliderCircle.getWidth()) / 100.0f * progress);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.hue_edit_bar_slider_circle) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    hueEditBarProgress.setVisibility(VISIBLE);
                    last_x = event.getRawX();
                    dis = curr_dis;
                    break;
                case MotionEvent.ACTION_MOVE:
                    float x = event.getRawX();
                    float change_dis = x - last_x;
                    if (dis + change_dis > (hueEditBarSliderGray.getRight() - hueEditBarSliderCircle.getRight())) {
                        change_dis = hueEditBarSliderGray.getRight() - hueEditBarSliderCircle.getRight() - dis;
                    }
                    if (dis + change_dis < hueEditBarSliderGray.getLeft() - hueEditBarSliderCircle.getLeft()) {
                        change_dis = hueEditBarSliderGray.getLeft() - hueEditBarSliderCircle.getLeft() - dis;
                    }
                    hueEditBarSliderCircle.setTranslationX(dis + change_dis);
                    hueEditBarProgress.setTranslationX(dis + change_dis);
                    float s = hueEditBarSliderCircle.getLeft() + hueEditBarSliderCircle.getTranslationX() - hueEditBarSliderGray.getLeft();
                    float t = hueEditBarSliderGray.getWidth() - hueEditBarSliderCircle.getWidth() * 1.0f;
                    progress = (int) ((s / t) * 100);
                    hueEditBarProgress.setText(progress + "");
                    curr_dis = dis + change_dis;

                    if (slidingListener != null)
                        slidingListener.onSliding(v, progress);
                    break;
                case MotionEvent.ACTION_UP:
                    hueEditBarProgress.setVisibility(INVISIBLE);
                    break;
            }
            return true;
        }
        return false;
    }

    @OnClick({R.id.hue_edit_bar_cancel, R.id.hue_edit_bar_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.hue_edit_bar_cancel:
                if (clickCancelListener != null)
                    clickCancelListener.onClickCancel(view);
                break;
            case R.id.hue_edit_bar_confirm:
                if (clickDoneListener != null)
                    clickDoneListener.onClickDone(view);
                break;
        }
    }

}

