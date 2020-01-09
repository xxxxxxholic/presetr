package com.example.presetr.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.presetr.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DarkRoomFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.darkroom_setting_view)
    ImageView darkroomSettingView;
    @BindView(R.id.main_purchase_view)
    ImageView mainPurchaseView;
    @BindView(R.id.darkroom_action_bar)
    ConstraintLayout darkroomActionBar;
    @BindView(R.id.darkroom_recycle_view)
    RecyclerView darkroomRecycleView;
    @BindView(R.id.darkroom_photos_hint)
    ImageView darkroomPhotosHint;
    @BindView(R.id.darkroom_no_photos_text)
    TextView darkroomNoPhotosText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_darkroom, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.darkroom_setting_view, R.id.main_purchase_view, R.id.darkroom_action_bar, R.id.darkroom_recycle_view, R.id.darkroom_photos_hint, R.id.darkroom_no_photos_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.darkroom_setting_view:
                break;
            case R.id.main_purchase_view:
                break;
            case R.id.darkroom_action_bar:
                break;
            case R.id.darkroom_recycle_view:
                break;
            case R.id.darkroom_photos_hint:
                break;
            case R.id.darkroom_no_photos_text:
                break;
        }
    }
}
