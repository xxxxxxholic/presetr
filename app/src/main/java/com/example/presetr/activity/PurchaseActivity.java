package com.example.presetr.activity;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.presetr.R;
import com.example.presetr.view.SlideImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PurchaseActivity extends BaseActivity {

    @BindView(R.id.purchase_viewPager)
    ViewPager purchaseViewPager;
    @BindView(R.id.purchase_back)
    ImageView purchaseBack;

    private List<View> viewList = new ArrayList<>();
    private PurchaseViewPagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        ButterKnife.bind(this);
        initViewPager();
    }

    private void initViewPager() {
        View view = LayoutInflater.from(this).inflate(R.layout.purchase_view_pager_layout, null);
        SlideImageView imageView = view.findViewById(R.id.slider_image_view);
        imageView.setRenderImage(BitmapFactory.decodeResource(getResources(), R.drawable.cover));
        imageView.setFilter();
        viewList.add(view);
        View view_2 = LayoutInflater.from(this).inflate(R.layout.purchase_view_pager_layout, null);
        SlideImageView imageView_2 = view_2.findViewById(R.id.slider_image_view);
        imageView_2.setRenderImage(BitmapFactory.decodeResource(getResources(), R.drawable.cover_2));
        imageView_2.setFilter();
        viewList.add(view_2);
        adapter = new PurchaseViewPagerAdapter(viewList);
        purchaseViewPager.setAdapter(adapter);
        purchaseViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                View view_ = viewList.get(position);
                SlideImageView imageView_ = view_.findViewById(R.id.slider_image_view);
                if (imageView_.getIsFirstCreate()) imageView_.startAnimation();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick(R.id.purchase_back)
    public void clickBack() {
        finish();
    }
}

class PurchaseViewPagerAdapter extends PagerAdapter {

    private List<View> viewList;

    public PurchaseViewPagerAdapter(List<View> views) {
        viewList = views;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(viewList.get(position));
    }
}
