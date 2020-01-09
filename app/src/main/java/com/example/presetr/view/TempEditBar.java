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

public class TempEditBar extends ConstraintLayout implements View.OnTouchListener {

    @BindView(R.id.temp_edit_bar_slider_gray)
    ImageView tempEditBarSliderGray;
    @BindView(R.id.temp_edit_bar_slider_circle)
    ImageView tempEditBarSliderCircle;
    @BindView(R.id.temp_edit_bar_progress)
    TextView tempEditBarProgress;
    @BindView(R.id.temp_edit_bar_cancel)
    ImageView tempEditBarCancel;
    @BindView(R.id.temp_edit_bar_confirm)
    ImageView tempEditBarConfirm;


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
        if (progress < -100) progress = -100;
        if (progress > 100) progress = 100;
        this.progress = progress;
        tempEditBarProgress.setText(progress + "");
        float r = tempEditBarSliderGray.getWidth() - tempEditBarSliderCircle.getWidth();
        float tx = r * ((progress + 100) / 200.0f);
        tempEditBarProgress.setTranslationX(tx);
        tempEditBarSliderCircle.setTranslationX(tx);
        curr_dis = tx;
    }


    public TempEditBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        activity = (DiyActivity) context;
        View view = LayoutInflater.from(context).inflate(R.layout.adjust_temp_edit_bar, this);
        ButterKnife.bind(this, view);
        tempEditBarSliderCircle.setOnTouchListener(this);
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
        tempEditBarProgress.setText(progress + "");
        tempEditBarProgress.setTranslationX(
                (tempEditBarSliderGray.getWidth() - tempEditBarSliderCircle.getWidth()) / 200.0f * ((progress + 100)));
        tempEditBarSliderCircle.setTranslationX(
                (tempEditBarSliderGray.getWidth() - tempEditBarSliderCircle.getWidth()) / 200.0f * ((progress + 100)));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.temp_edit_bar_slider_circle) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    tempEditBarProgress.setVisibility(VISIBLE);
                    last_x = event.getRawX();
                    dis = curr_dis;
                    break;
                case MotionEvent.ACTION_MOVE:
                    float x = event.getRawX();
                    float change_dis = x - last_x;
                    if (dis + change_dis > (tempEditBarSliderGray.getRight() - tempEditBarSliderCircle.getRight())) {
                        change_dis = tempEditBarSliderGray.getRight() - tempEditBarSliderCircle.getRight() - dis;
                    }
                    if (dis + change_dis < tempEditBarSliderGray.getLeft() - tempEditBarSliderCircle.getLeft()) {
                        change_dis = tempEditBarSliderGray.getLeft() - tempEditBarSliderCircle.getLeft() - dis;
                    }
                    tempEditBarSliderCircle.setTranslationX(dis + change_dis);
                    tempEditBarProgress.setTranslationX(dis + change_dis);
                    float s = tempEditBarSliderCircle.getLeft() + tempEditBarSliderCircle.getTranslationX() - tempEditBarSliderGray.getLeft();
                    float t = tempEditBarSliderGray.getWidth() - tempEditBarSliderCircle.getWidth() * 1.0f;
                    progress = (int) ((s / t) * 200) - 100;
                    tempEditBarProgress.setText(progress + "");
                    curr_dis = dis + change_dis;

                    if (slidingListener != null)
                        slidingListener.onSliding(v, progress);
                    break;
                case MotionEvent.ACTION_UP:
                    tempEditBarProgress.setVisibility(INVISIBLE);
                    break;
            }
            return true;
        }
        return false;
    }

    @OnClick({R.id.temp_edit_bar_cancel, R.id.temp_edit_bar_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.temp_edit_bar_cancel:
                if (clickCancelListener != null)
                    clickCancelListener.onClickCancel(view);
                break;
            case R.id.temp_edit_bar_confirm:
                if (clickDoneListener != null)
                    clickDoneListener.onClickDone(view);
                break;
        }
    }
}
