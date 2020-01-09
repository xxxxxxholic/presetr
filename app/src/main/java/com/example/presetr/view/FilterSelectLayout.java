package com.example.presetr.view;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.presetr.MyApplication;
import com.example.presetr.R;
import com.example.presetr.activity.DiyActivity;
import com.example.presetr.adapter.filterDIYAdapter.FilterPicAdapter;
import com.example.presetr.adapter.filterDIYAdapter.FilterTagAdapter;
import com.example.presetr.model.FilterBean;
import com.example.presetr.model.FilterPackageBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterSelectLayout extends LinearLayout {

    private static final String TAG = "FilterSelectLayout";
    public static int TAG_POSITION = 0;
    public static int PIC_POSITION = 0;

    @BindView(R.id.filter_diy_tags_recycleView)
    RecyclerView filterDiyTagsRecycleView;
    @BindView(R.id.filter_diy_filter_recycleView)
    RecyclerView filterDiyFilterRecycleView;
    @BindView(R.id.filter_diy_select_layout)
    LinearLayout filterDiySelectLayout;


    private int filter_position;
    private FilterTagAdapter tagAdapter;
    private List<String> tags = new ArrayList<>();
    private FilterPicAdapter picAdapter;

    public FilterPicAdapter getPicAdapter() {
        return picAdapter;
    }

    public void setPicAdapter(FilterPicAdapter picAdapter) {
        this.picAdapter = picAdapter;
    }

    public FilterSelectLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.filter_diy_layout, this);
        ButterKnife.bind(this,view);
        initAdapter(context);
    }
    private void initAdapter(Context context) {
        DiyActivity activity = (DiyActivity) context;
        Bundle bundle = activity.getIntent().getExtras();
        filter_position = bundle.getInt("position");
        PIC_POSITION = filter_position+1;
        for (FilterPackageBean bean : MyApplication.filterPackages) {
            tags.add(bean.getName());
        }
        tagAdapter = new FilterTagAdapter(tags);
        LinearLayoutManager lManager = new LinearLayoutManager(context);
        lManager.setOrientation(RecyclerView.HORIZONTAL);
        filterDiyTagsRecycleView.setHasFixedSize(true);
        filterDiyTagsRecycleView.setItemAnimator(null);
        filterDiyTagsRecycleView.setLayoutManager(lManager);
        filterDiyTagsRecycleView.setAdapter(tagAdapter);

        picAdapter = new FilterPicAdapter(MyApplication.filterBeans);
        LinearLayoutManager lManager_2 = new LinearLayoutManager(context);
        lManager_2.setOrientation(RecyclerView.HORIZONTAL);
        filterDiyFilterRecycleView.setItemAnimator(null);
        filterDiyFilterRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                if(manager instanceof LinearLayoutManager){
                    int lastVisibleItemPosition = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
                    //根据lastVisibleItemPosition算出属于第几个滤镜包
                    FilterBean bean = MyApplication.filterBeans.get(lastVisibleItemPosition-1);
                    String packageDisplayName = bean.getDisplayName();
                    FilterPackageBean packageBean = MyApplication.filterPackagesMap.get(packageDisplayName);
                    int position = MyApplication.filterPackages.indexOf(packageBean);
                    filterDiyTagsRecycleView.scrollToPosition(position+1);
                    int last = TAG_POSITION;
                    TAG_POSITION = position+1;
                    tagAdapter.notifyItemChanged(last,1);
                    tagAdapter.notifyItemChanged(position+1);
                }
            }
        });
        filterDiyFilterRecycleView.setLayoutManager(lManager_2);
        filterDiyFilterRecycleView.setHasFixedSize(true);
        filterDiyFilterRecycleView.setAdapter(picAdapter);
        filterDiyFilterRecycleView.scrollToPosition(filter_position+1);
    }

}
