package com.example.presetr.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.presetr.MyApplication;
import com.example.presetr.R;
import com.example.presetr.activity.MainActivity;
import com.example.presetr.activity.PurchaseActivity;
import com.example.presetr.adapter.MainFilterAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.main_setting_view)
    ImageView mainSettingView;
    @BindView(R.id.main_purchase_view)
    ImageView mainPurchaseView;
    @BindView(R.id.main_action_bar)
    ConstraintLayout mainActionBar;
    @BindView(R.id.main_recycle_view)
    RecyclerView mainRecycleView;

    private MainFilterAdapter mainFilterAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        //recyclerView设置
        LinearLayoutManager lManager = new LinearLayoutManager(this.getContext());
        lManager.setOrientation(LinearLayoutManager.VERTICAL);
        mainRecycleView.setLayoutManager(lManager);
        mainFilterAdapter = new MainFilterAdapter(MyApplication.filterPackages);
        mainRecycleView.setAdapter(mainFilterAdapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.main_setting_view, R.id.main_purchase_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_setting_view:
                clickSetting();
                break;
            case R.id.main_purchase_view:
                clickPurchase();
                break;
        }
    }
    public void clickSetting(){

    }
    public void clickPurchase(){
        ((MainActivity)this.getActivity()).startActivity(PurchaseActivity.class);
    }
}
