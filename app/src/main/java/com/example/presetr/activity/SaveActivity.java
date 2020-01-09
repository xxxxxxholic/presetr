package com.example.presetr.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.presetr.MyApplication;
import com.example.presetr.R;
import com.example.presetr.util.BitmapUtil;
import com.example.presetr.view.SaveAlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SaveActivity extends BaseActivity {

    @BindView(R.id.save_back)
    ImageView saveBack;
    @BindView(R.id.save_to_main)
    ImageView saveToMain;
    @BindView(R.id.save_image_pic)
    ImageView saveImagePic;
    @BindView(R.id.save_done)
    ImageView saveDone;
    @BindView(R.id.share_ins)
    ImageView shareIns;
    @BindView(R.id.share_other)
    ImageView shareOther;

    SaveAlertDialog dialog;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        ButterKnife.bind(this);
//        byte[] buf = getIntent().getExtras().getByteArray("save_pic");
        bitmap = Bitmap.createBitmap(DiyActivity.cur_bitmap);
        saveImagePic.setImageBitmap(bitmap);
    }

    @OnClick({R.id.save_back, R.id.save_to_main, R.id.save_done, R.id.share_ins, R.id.share_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.save_back:
                finish();
                break;
            case R.id.save_to_main:
                startActivity(MainActivity.class);
                break;
            case R.id.save_done:
                SaveAlertDialog.Builder builder = new SaveAlertDialog.Builder(this);
                dialog = builder.setMessage("Save")
                        .setButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                BitmapUtil.saveImage(MyApplication.context,bitmap,"hello1.png");
                            }
                        }).create();
                dialog.show();
                break;
            case R.id.share_ins:
                break;
            case R.id.share_other:
                break;
        }
    }
}
