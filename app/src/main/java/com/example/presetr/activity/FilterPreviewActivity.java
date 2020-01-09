package com.example.presetr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.presetr.MyApplication;
import com.example.presetr.R;
import com.example.presetr.adapter.PreviewFilterAdapter;
import com.example.presetr.model.FilterPackageBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterPreviewActivity extends BaseActivity {

    private static final String TAG = "FilterPreviewActivity";

    @BindView(R.id.filter_preview_back)
    ImageView filterPreviewBack;
    @BindView(R.id.filter_preview_recycleView)
    RecyclerView filterPreviewRecycleView;
    @BindView(R.id.filter_preview_unlock)
    ImageView filterPreviewUnlock;
    @BindView(R.id.filter_preview_toVIP)
    ImageView filterPreviewToVIP;
    @BindView(R.id.filter_preview_unlock_text)
    TextView filterPreviewUnlockText;
    @BindView(R.id.filter_preview_price_text)
    TextView filterPreviewPriceText;
    @BindView(R.id.filter_preview_unlock_all_text)
    TextView filterPreviewUnlockAllText;
    @BindView(R.id.filter_preview_vip_price_text)
    TextView filterPreviewVipPriceText;

    private FilterPackageBean bean;
    private PreviewFilterAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_preview);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("filter_name");
        bean = MyApplication.filterPackagesMap.get(name);
        init(bean);
    }

    private void init(FilterPackageBean bean) {
        if(bean.getIsCharged()==0){
            filterPreviewUnlock.setVisibility(View.INVISIBLE);
            filterPreviewToVIP.setVisibility(View.INVISIBLE);
            filterPreviewVipPriceText.setVisibility(View.INVISIBLE);
            filterPreviewUnlockAllText.setVisibility(View.INVISIBLE);
        }else {
            filterPreviewUnlockText.setText("Unlock " + bean.getDisplayName());
            filterPreviewPriceText.setText("$ "+bean.getPrice());
        }
        LinearLayoutManager lManager = new LinearLayoutManager(this);
        lManager.setOrientation(LinearLayoutManager.VERTICAL);
        lManager.setItemPrefetchEnabled(true);
        filterPreviewRecycleView.setHasFixedSize(true);
        filterPreviewRecycleView.setItemViewCacheSize(15);
        filterPreviewRecycleView.setLayoutManager(lManager);
        adapter = new PreviewFilterAdapter(bean);
        filterPreviewRecycleView.setAdapter(adapter);
    }

    @OnClick({ R.id.filter_preview_back, R.id.filter_preview_unlock, R.id.filter_preview_toVIP})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.filter_preview_back:
                finish();
                break;
            case R.id.filter_preview_unlock:
                break;
            case R.id.filter_preview_toVIP:
                startActivity(PurchaseActivity.class);
                break;
        }
    }
}
