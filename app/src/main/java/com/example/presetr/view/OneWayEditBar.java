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

//亮度/Brightness、
// 对比度/Contrast、饱和度/Saturation
// 暗角/Vignette、噪点/Grain、
public class OneWayEditBar extends ConstraintLayout implements View.OnTouchListener {

    @BindView(R.id.filter_edit_bar_slider_gray)
    ImageView filterEditBarSliderGray;
    @BindView(R.id.filter_edit_bar_slider_circle)
    ImageView filterEditBarSliderCircle;
    @BindView(R.id.filter_edit_bar_progress)
    TextView filterEditBarProgress;
    @BindView(R.id.filter_edit_bar_cancel)
    ImageView filterEditBarCancel;
    @BindView(R.id.filter_edit_bar_confirm)
    ImageView filterEditBarConfirm;

    private Context mContext;
    private DiyActivity activity;
    private int progress;
    private float last_x;
    private float dis;
    private float curr_dis;
    private OnSlidingListener slidingListener;
    private OnClickCancelListener clickCancelListener;
    private OnClickDoneListener clickDoneListener;

    public void setSlidingListener(OnSlidingListener slidingListener) {
        this.slidingListener = slidingListener;
    }

    public void setClickCancelListener(OnClickCancelListener clickCancelListener) {
        this.clickCancelListener = clickCancelListener;
    }

    public void setClickDoneListener(OnClickDoneListener clickDoneListener) {
        this.clickDoneListener = clickDoneListener;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        if (progress < 0) progress = 0;
        if (progress > 100) progress = 100;
        this.progress = progress;
        filterEditBarProgress.setText(progress + "");
        filterEditBarProgress.setTranslationX(
                (filterEditBarSliderGray.getWidth() - filterEditBarSliderCircle.getWidth()) / 100.0f * progress);
        filterEditBarSliderCircle.setTranslationX(
                (filterEditBarSliderGray.getWidth() - filterEditBarSliderCircle.getWidth()) / 100.0f * progress);
        curr_dis = (filterEditBarSliderGray.getWidth() - filterEditBarSliderCircle.getWidth()) / 100.0f * progress;
    }

    public OneWayEditBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        activity = (DiyActivity) context;
        View view = LayoutInflater.from(context).inflate(R.layout.filter_edit_bar, this);
        ButterKnife.bind(this, view);
        filterEditBarSliderCircle.setOnTouchListener(this);
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
        filterEditBarProgress.setText(progress + "");
        filterEditBarProgress.setTranslationX(
                (filterEditBarSliderGray.getWidth() - filterEditBarSliderCircle.getWidth()) / 100.0f * progress);
        filterEditBarSliderCircle.setTranslationX(
                (filterEditBarSliderGray.getWidth() - filterEditBarSliderCircle.getWidth()) / 100.0f * progress);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.filter_edit_bar_slider_circle) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    filterEditBarProgress.setVisibility(VISIBLE);
                    last_x = event.getRawX();
                    dis = curr_dis;
                    break;
                case MotionEvent.ACTION_MOVE:
                    float x = event.getRawX();
                    float change_dis = x - last_x;
                    if (dis + change_dis > (filterEditBarSliderGray.getRight() - filterEditBarSliderCircle.getRight())) {
                        change_dis = filterEditBarSliderGray.getRight() - filterEditBarSliderCircle.getRight() - dis;
                    }
                    if (dis + change_dis < filterEditBarSliderGray.getLeft() - filterEditBarSliderCircle.getLeft()) {
                        change_dis = filterEditBarSliderGray.getLeft() - filterEditBarSliderCircle.getLeft() - dis;
                    }
                    filterEditBarSliderCircle.setTranslationX(dis + change_dis);
                    filterEditBarProgress.setTranslationX(dis + change_dis);
                    float s = filterEditBarSliderCircle.getLeft() + filterEditBarSliderCircle.getTranslationX() - filterEditBarSliderGray.getLeft();
                    float t = filterEditBarSliderGray.getWidth() - filterEditBarSliderCircle.getWidth() * 1.0f;
                    progress = (int) ((s / t) * 100);
                    filterEditBarProgress.setText(progress + "");
                    curr_dis = dis + change_dis;

                    if(slidingListener!=null)
                        slidingListener.onSliding(v,progress);
                    break;
                case MotionEvent.ACTION_UP:
                    filterEditBarProgress.setVisibility(INVISIBLE);
                    break;
            }
            return true;
        }
        return false;
    }

    @OnClick({R.id.filter_edit_bar_cancel, R.id.filter_edit_bar_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.filter_edit_bar_cancel:
                if(clickCancelListener!=null)
                    clickCancelListener.onClickCancel(view);
                break;
            case R.id.filter_edit_bar_confirm:
                if(clickDoneListener!=null)
                    clickDoneListener.onClickDone(view);
                break;
        }
    }

    public interface OnSlidingListener{
        void onSliding(View v,int progress);
    }
    public interface OnClickCancelListener{
        void onClickCancel(View v);
    }
    public interface OnClickDoneListener{
        void onClickDone(View v);
    }
}
