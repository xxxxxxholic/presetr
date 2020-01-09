package com.example.presetr.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.presetr.MyApplication;
import com.example.presetr.R;
import com.example.presetr.adapter.adjustDIYAdapter.AdjustDIYAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdjustSelectLayout extends LinearLayout {
    @BindView(R.id.adjust_select_recycleView)
    RecyclerView adjustSelectRecycleView;

    private Context mContext;
    private AdjustDIYAdapter adjustDIYAdapter;
    public static int POSITION = -1;

    public AdjustSelectLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.adjust_select_layout, this);
        ButterKnife.bind(this, view);
        LinearLayoutManager lManager = new LinearLayoutManager(context);
        lManager.setOrientation(RecyclerView.HORIZONTAL);
        adjustSelectRecycleView.setHasFixedSize(true);
        adjustSelectRecycleView.setLayoutManager(lManager);
        adjustDIYAdapter = new AdjustDIYAdapter(MyApplication.adjustBeans);
        adjustSelectRecycleView.setAdapter(adjustDIYAdapter);

    }
}
