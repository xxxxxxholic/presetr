package com.example.presetr.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

public class SliderBarImageView extends AppCompatImageView {

    private OnTouchListener listener;

    public OnTouchListener getListener() {
        return listener;
    }

    public void setListener(OnTouchListener listener) {
        this.listener = listener;
    }

    public SliderBarImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        listener.onTouch(this,event);
        return true;
    }
}

