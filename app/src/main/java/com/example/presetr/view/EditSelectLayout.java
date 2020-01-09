package com.example.presetr.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.presetr.R;
import com.example.presetr.activity.DiyActivity;
import com.example.presetr.util.BitmapUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.co.cyberagent.android.gpuimage.util.Rotation;

public class EditSelectLayout extends LinearLayout {

    @BindView(R.id.crop_select_cancel)
    ImageView cropSelectCancel;
    @BindView(R.id.crop_free)
    TextView cropFree;
    @BindView(R.id.crop_11)
    TextView crop11;
    @BindView(R.id.crop_34)
    TextView crop34;
    @BindView(R.id.crop_43)
    TextView crop43;
    @BindView(R.id.crop_916)
    TextView crop916;
    @BindView(R.id.crop_169)
    TextView crop169;
    @BindView(R.id.crop_select_done)
    ImageView cropSelectDone;
    @BindView(R.id.crop_select_layout)
    LinearLayout cropSelectLayout;
    @BindView(R.id.rotate_select_cancel)
    ImageView rotateSelectCancel;
    @BindView(R.id.rotate_select_done)
    ImageView rotateSelectDone;
    @BindView(R.id.rotate_select_layout)
    LinearLayout rotateSelectLayout;
    @BindView(R.id.square_fit_icon)
    ImageView squareFitIcon;
    @BindView(R.id.square_fit_icon_name)
    TextView squareFitIconName;
    @BindView(R.id.tab_crop_icon)
    ImageView tabCropIcon;
    @BindView(R.id.tab_crop_icon_name)
    TextView tabCropIconName;
    @BindView(R.id.tab_tilt_icon)
    ImageView tabTiltIcon;
    @BindView(R.id.tab_tilt_icon_name)
    TextView tabTiltIconName;
    @BindView(R.id.tab_rotate_icon)
    ImageView tabRotateIcon;
    @BindView(R.id.tab_rotate_icon_name)
    TextView tabRotateIconName;
    @BindView(R.id.tab_mirror_icon)
    ImageView tabMirrorIcon;
    @BindView(R.id.tab_mirror_icon_name)
    TextView tabMirrorIconName;
    @BindView(R.id.rotate_edit_bar)
    RotateEditBar rotateEditBar;
    @BindView(R.id.square_fit_icon_layout)
    LinearLayout squareFitIconLayout;
    @BindView(R.id.tab_crop_icon_layout)
    LinearLayout tabCropIconLayout;
    @BindView(R.id.tab_tilt_icon_layout)
    LinearLayout tabTiltIconLayout;
    @BindView(R.id.tab_rotate_icon_layout)
    LinearLayout tabRotateIconLayout;
    @BindView(R.id.tab_mirror_icon_name_layout)
    LinearLayout tabMirrorIconNameLayout;

    private Context mContext;
    private DiyActivity activity;

    private boolean isFlip = false;
    private int ro_Rotation;
    private int tileRotation;

    private boolean isFirstPressSquare = false;

    public boolean getIsFlip() {
        return isFlip;
    }

    public void setFlip(boolean flip) {
        isFlip = flip;
    }

    public int getRo_Rotation() {
        return ro_Rotation;
    }

    public void setRo_Rotation(int ro_Rotation) {
        this.ro_Rotation = ro_Rotation;
    }

    public int getTileRotation() {
        return tileRotation;
    }

    public void setTileRotation(int tileRotation) {
        this.tileRotation = tileRotation;
    }

    public EditSelectLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        activity = (DiyActivity) context;
        View view = LayoutInflater.from(context).inflate(R.layout.crop_select_layout, this);
        ButterKnife.bind(this, view);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @OnClick({R.id.square_fit_icon, R.id.tab_crop_icon, R.id.tab_tilt_icon, R.id.tab_rotate_icon, R.id.tab_mirror_icon,
            R.id.crop_free, R.id.crop_11, R.id.crop_34, R.id.crop_43, R.id.crop_916, R.id.crop_169, R.id.crop_select_cancel, R.id.crop_select_done,
            R.id.square_fit_icon_layout, R.id.tab_crop_icon_layout, R.id.tab_tilt_icon_layout, R.id.tab_rotate_icon_layout, R.id.tab_mirror_icon_name_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.crop_select_cancel:
                activity.getCropControllerView().setVisibility(INVISIBLE);
                break;
            case R.id.crop_select_done:
                float left = activity.getCropControllerView().selectRect.left;
                float right = activity.getCropControllerView().selectRect.right;
                float top = activity.getCropControllerView().selectRect.top;
                float bottom = activity.getCropControllerView().selectRect.bottom;
                Bitmap bitmap = null;
                try {
                    bitmap = activity.getDiyImage().capture();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Bitmap shot_bitmap;
                shot_bitmap = Bitmap.createBitmap(bitmap, (int) left, (int) top, (int) (right - left), (int) (bottom - top));
                activity.setCur_bitmap(shot_bitmap);
                activity.getDiyImage().getGPUImage().deleteImage();
                activity.getDiyImage().setImage(shot_bitmap);
                activity.getCropControllerView().setVisibility(INVISIBLE);
                break;
            case R.id.square_fit_icon_layout:
            case R.id.square_fit_icon:
                clickSquareFit(view);
                break;
            case R.id.tab_crop_icon_layout:
            case R.id.tab_crop_icon:
                clickCrop(view);
                break;
            case R.id.tab_tilt_icon_layout:
            case R.id.tab_tilt_icon:
                clickTile(view);
                break;
            case R.id.tab_rotate_icon_layout:
            case R.id.tab_rotate_icon:
                clickRotate(view);
                break;
            case R.id.tab_mirror_icon_name_layout:
            case R.id.tab_mirror_icon:
                clickMirror(view);
                break;
            case R.id.crop_free:
                clickCropFree(view);
                break;
            case R.id.crop_11:
                clickCrop_11(view);
                break;
            case R.id.crop_34:
                clickCrop_34(view);
                break;
            case R.id.crop_43:
                clickCrop_43(view);
                break;
            case R.id.crop_916:
                clickCrop_916(view);
                break;
            case R.id.crop_169:
                clickCrop_169(view);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    private void clickCropFree(View view) {
        notifyRatioTextColorChange(cropFree);
        activity.getCropControllerView().setmShape("free");
        float w = activity.getCur_bitmap().getWidth();
        float h = activity.getCur_bitmap().getHeight();
        float vw = activity.getDiyImage().getWidth();
        float vh = activity.getDiyImage().getHeight();
        if (w >= h) {
            activity.getCropControllerView().setCrop_width(activity.getDiyImage().getWidth());
            activity.getCropControllerView().setCrop_height(1.0f * activity.getCropControllerView().getCrop_width()
                    * h / w);
            activity.getCropControllerView().setBitmap_w(activity.getDiyImage().getWidth());
            activity.getCropControllerView().setBitmap_h(1.0f * activity.getCropControllerView().getCrop_width()
                    * h / w);
        } else {
            if (w / h <= vw / vh) {
                activity.getCropControllerView().setCrop_height(activity.getDiyImage().getHeight());
                activity.getCropControllerView().setCrop_width(1.0f * activity.getCropControllerView().getCrop_height()
                        * w / h);
//                activity.getCropControllerView().setBitmap_h(activity.getDiyImage().getHeight());
//                activity.getCropControllerView().setBitmap_w(1.0f * activity.getCropControllerView().getCrop_height()
//                        * w / h);
            } else {
                activity.getCropControllerView().setCrop_width(vw);
                activity.getCropControllerView().setCrop_height(vw * h / w);
//                activity.getCropControllerView().setBitmap_h(vw);
//                activity.getCropControllerView().setBitmap_w(vw * h / w);
            }
        }
        activity.getCropControllerView().setVisibility(VISIBLE);
    }

    private void clickCrop_169(View view) {
        notifyRatioTextColorChange(crop169);
        activity.getCropControllerView().setmShape("16*9");
        float w = activity.getCur_bitmap().getWidth();
        float h = activity.getCur_bitmap().getHeight();
        float vw = activity.getDiyImage().getWidth();
        float vh = activity.getDiyImage().getHeight();
        if (w >= h) {
            if (w / h >= 16.0 / 9.0) {
                activity.getCropControllerView().setCrop_height(1.0f * h / w * activity.getDiyImage().getWidth());
                activity.getCropControllerView().setCrop_width(activity.getCropControllerView().getCrop_height() * 16 / 9);
            } else {
                activity.getCropControllerView().setCrop_width(activity.getDiyImage().getWidth());
                activity.getCropControllerView().setCrop_height(activity.getCropControllerView().getCrop_width() * 9 / 16);
            }
        } else {
            if (w / h <= vw / vh) {
                activity.getCropControllerView().setCrop_width(w * 1.0f / h * activity.getDiyImage().getHeight());
            } else {
                activity.getCropControllerView().setCrop_width(vw);
            }
            activity.getCropControllerView().setCrop_height(activity.getCropControllerView().getCrop_width() * 9 / 16);
        }
        activity.getCropControllerView().setVisibility(VISIBLE);
    }

    private void clickCrop_916(View view) {
        notifyRatioTextColorChange(crop916);
        activity.getCropControllerView().setmShape("9*16");
        float w = activity.getCur_bitmap().getWidth();
        float h = activity.getCur_bitmap().getHeight();
        float vw = activity.getDiyImage().getWidth();
        float vh = activity.getDiyImage().getHeight();
        if (w >= h) {
            activity.getCropControllerView().setCrop_width(activity.getDiyImage().getWidth());
            activity.getCropControllerView().setCrop_height(1.0f * activity.getCropControllerView().getCrop_width()
                    * h / w);
            activity.getCropControllerView().setCrop_width(activity.getCropControllerView().getCrop_height() * 9 / 16);
        } else {
            if (w / h <= vw / vh) {
                if (w / h <= 9.0 / 16.0) {
                    activity.getCropControllerView().setCrop_width(w * 1.0f / h * activity.getDiyImage().getHeight());
                    activity.getCropControllerView().setCrop_height(activity.getCropControllerView().getCrop_width() * 16 / 9);
                } else {
                    activity.getCropControllerView().setCrop_height(activity.getDiyImage().getHeight());
                    activity.getCropControllerView().setCrop_width(activity.getCropControllerView().getCrop_height() * 9 / 16);
                }
            } else {
                if (w / h <= 9.0 / 16.0) {
                    activity.getCropControllerView().setCrop_width(vw);
                    activity.getCropControllerView().setCrop_height(vw * 16 / 9);
                } else {
                    activity.getCropControllerView().setCrop_height(vw * h / w);
                    activity.getCropControllerView().setCrop_width(activity.getCropControllerView().getCrop_height() * 9 / 16);
                }
            }
        }
        activity.getCropControllerView().setVisibility(VISIBLE);
    }

    private void clickCrop_43(View view) {
        notifyRatioTextColorChange(crop43);
        activity.getCropControllerView().setmShape("4*3");
        float w = activity.getCur_bitmap().getWidth();
        float h = activity.getCur_bitmap().getHeight();
        float vw = activity.getDiyImage().getWidth();
        float vh = activity.getDiyImage().getHeight();
        if (w >= h) {
            if (w / h >= 4.0 / 3.0) {
                activity.getCropControllerView().setCrop_height((1.0f * h / w) * activity.getDiyImage().getWidth());
                activity.getCropControllerView().setCrop_width(activity.getCropControllerView().getCrop_height() * 4 / 3);
            } else {
                activity.getCropControllerView().setCrop_width(activity.getDiyImage().getWidth() * 1.0f);
                activity.getCropControllerView().setCrop_height(activity.getCropControllerView().getCrop_width() * 3.0f / 4.0f);
            }
        } else {
            if (w / h <= vw / vh) {
                activity.getCropControllerView().setCrop_width(w * 1.0f / h * activity.getDiyImage().getHeight());
            } else {
                activity.getCropControllerView().setCrop_width(vw);
            }
            activity.getCropControllerView().setCrop_height(activity.getCropControllerView().getCrop_width() * 3 / 4);
        }
        activity.getCropControllerView().setVisibility(VISIBLE);
    }

    private void clickCrop_34(View view) {
        notifyRatioTextColorChange(crop34);
        activity.getCropControllerView().setmShape("3*4");
        float w = activity.getCur_bitmap().getWidth();
        float h = activity.getCur_bitmap().getHeight();
        float vw = activity.getDiyImage().getWidth();
        float vh = activity.getDiyImage().getHeight();
        if (w >= h) {
            activity.getCropControllerView().setCrop_width(activity.getDiyImage().getWidth());
            activity.getCropControllerView().setCrop_height(1.0f * activity.getCropControllerView().getCrop_width()
                    * h / w);
            activity.getCropControllerView().setCrop_width(activity.getCropControllerView().getCrop_height() * 3 / 4);
        } else {
            if (w / h <= vw / vh) {
                if (w / h <= 3.0 / 4.0) {
                    activity.getCropControllerView().setCrop_width(w * 1.0f / h * activity.getDiyImage().getHeight());
                    activity.getCropControllerView().setCrop_height(activity.getCropControllerView().getCrop_width() * 4 / 3);
                } else {
                    activity.getCropControllerView().setCrop_height(activity.getDiyImage().getHeight());
                    activity.getCropControllerView().setCrop_width(activity.getCropControllerView().getCrop_height() * 3 / 4);
                }
            } else {
                if (w / h <= 3.0 / 4.0) {
                    activity.getCropControllerView().setCrop_width(vw);
                    activity.getCropControllerView().setCrop_height(vw * 4 / 3);
                } else {
                    activity.getCropControllerView().setCrop_height(vw * h / w);
                    activity.getCropControllerView().setCrop_width(activity.getCropControllerView().getCrop_height() * 3 / 4);
                }
            }
        }
        activity.getCropControllerView().setVisibility(VISIBLE);
    }

    private void clickCrop_11(View view) {
        notifyRatioTextColorChange(crop11);
        activity.getCropControllerView().setmShape("1*1");
        float w = activity.getCur_bitmap().getWidth();
        float h = activity.getCur_bitmap().getHeight();
        float vw = activity.getDiyImage().getWidth();
        float vh = activity.getDiyImage().getHeight();
        if (w >= h) {
            activity.getCropControllerView().setCrop_width(activity.getDiyImage().getWidth());
            activity.getCropControllerView().setCrop_height(1.0f * activity.getCropControllerView().getCrop_width()
                    * h / w);
            activity.getCropControllerView().setCrop_width(activity.getCropControllerView().getCrop_height());
        } else {
            if (w / h <= vw / vh) {
                activity.getCropControllerView().setCrop_height(activity.getDiyImage().getHeight());
                activity.getCropControllerView().setCrop_width(1.0f * activity.getCropControllerView().getCrop_height()
                        * w / h);
                activity.getCropControllerView().setCrop_height(activity.getCropControllerView().getCrop_width());
            } else {
                activity.getCropControllerView().setCrop_width(activity.getDiyImage().getWidth());
                activity.getCropControllerView().setCrop_height(activity.getDiyImage().getWidth());
            }
        }
        activity.getCropControllerView().setVisibility(VISIBLE);
    }

    private void clickTile(View view) {
        notifyEditTextColorChange(tabTiltIconName);
        activity.getFilter().setBackOld(1);
        activity.getDiyImage().setFilter(activity.getFilter());
        cropSelectLayout.setVisibility(INVISIBLE);
        rotateSelectLayout.setVisibility(VISIBLE);
        rotateEditBar.setVisibility(VISIBLE);
        activity.getCropControllerView().setVisibility(INVISIBLE);
        rotateEditBar.setProgress(tileRotation);
    }

    private void clickCrop(View view) {
        notifyEditTextColorChange(tabCropIconName);
        activity.getFilter().setBackOld(1);
        activity.getDiyImage().setFilter(activity.getFilter());
        cropSelectLayout.setVisibility(VISIBLE);
        rotateSelectLayout.setVisibility(INVISIBLE);
//        activity.getCropControllerView().setVisibility(INVISIBLE);
        float w = activity.getCur_bitmap().getWidth();
        float h = activity.getCur_bitmap().getHeight();
        float vw = activity.getDiyImage().getWidth();
        float vh = activity.getDiyImage().getHeight();
        if (w >= h) {
            activity.getCropControllerView().setBitmap_w(activity.getDiyImage().getWidth());
            activity.getCropControllerView().setBitmap_h(1.0f * activity.getCropControllerView().getBitmap_w()
                    * h / w);
        } else {
            if (w / h <= vw / vh) {
                activity.getCropControllerView().setBitmap_h(activity.getDiyImage().getHeight());
                activity.getCropControllerView().setBitmap_w(1.0f * activity.getCropControllerView().getBitmap_h()
                        * w / h);
            } else {
                activity.getCropControllerView().setBitmap_w(activity.getDiyImage().getWidth());
                activity.getCropControllerView().setBitmap_h(1.0f * activity.getCropControllerView().getBitmap_w()
                        * h / w);
            }
        }
    }

    private void clickRotate(View view) {
        notifyEditTextColorChange(tabRotateIconName);
        cropSelectLayout.setVisibility(INVISIBLE);
        rotateSelectLayout.setVisibility(INVISIBLE);
        activity.getCropControllerView().setVisibility(INVISIBLE);
        //ro_Rotation = (ro_Rotation + 90) % 360;
        ro_Rotation = 90;
//        activity.getDiyImage().getGPUImage().setRotation(Rotation.fromInt(ro_Rotation), isFlip, false);
        Bitmap cur_bitmap = activity.getCur_bitmap();
        Matrix matrix = new Matrix();
        matrix.setRotate(ro_Rotation);
        Bitmap bitmap1 = Bitmap.createBitmap(cur_bitmap, 0, 0, cur_bitmap.getWidth(), cur_bitmap.getHeight(), matrix, false);
        activity.setCur_bitmap(bitmap1);
        activity.getDiyImage().getGPUImage().deleteImage();
        activity.getDiyImage().setImage(activity.getCur_bitmap());
//        activity.getDiyImage().requestRender();
    }

    private void clickMirror(View view) {
        notifyEditTextColorChange(tabMirrorIconName);
        cropSelectLayout.setVisibility(INVISIBLE);
        rotateSelectLayout.setVisibility(INVISIBLE);
        activity.getCropControllerView().setVisibility(INVISIBLE);
        if (isFlip == false) {
            //isFlip = true;

            activity.getDiyImage().getGPUImage().setRotation(Rotation.NORMAL, !isFlip, false);
            activity.setCur_bitmap(activity.getDiyImage().getGPUImage().getBitmapWithFilterApplied());
            activity.getDiyImage().getGPUImage().setRotation(Rotation.NORMAL, false, false);
        } else {
//            isFlip = false;
//            activity.getDiyImage().getGPUImage().setRotation(Rotation.fromInt(ro_Rotation), isFlip, false);
//            activity.setCur_bitmap(activity.getDiyImage().getGPUImage().getBitmapWithFilterApplied());
            //activity.getDiyImage().getGPUImage().setRotation(Rotation.fromInt(ro_Rotation),true,false);
        }
//        activity.setCur_bitmap(activity.getDiyImage().getGPUImage().getBitmapWithFilterApplied());
        activity.getDiyImage().getGPUImage().deleteImage();
        activity.getDiyImage().setImage(activity.getCur_bitmap());
        activity.getFilter().setBackOld(1);
        activity.getDiyImage().setFilter(activity.getFilter());
//        activity.getDiyImage().requestRender();
    }

    private void clickSquareFit(View view) {
        notifyEditTextColorChange(squareFitIconName);
        cropSelectLayout.setVisibility(INVISIBLE);
        rotateSelectLayout.setVisibility(INVISIBLE);
        activity.getCropControllerView().setVisibility(INVISIBLE);
        Bitmap targetBitmap = activity.getCur_bitmap();
        if (isFirstPressSquare == false) {
            isFirstPressSquare = true;
        }
        Bitmap newBitmap = BitmapUtil.squareFill(targetBitmap);
        activity.setSquareBitmap(targetBitmap);
        activity.setCur_bitmap(newBitmap);
        activity.getDiyImage().getGPUImage().deleteImage();
        activity.getDiyImage().setImage(newBitmap);
    }

    private void notifyEditTextColorChange(TextView textView) {
        squareFitIconName.setTextColor(Color.parseColor("#666666"));
        tabCropIconName.setTextColor(Color.parseColor("#666666"));
        tabTiltIconName.setTextColor(Color.parseColor("#666666"));
        tabRotateIconName.setTextColor(Color.parseColor("#666666"));
        tabMirrorIconName.setTextColor(Color.parseColor("#666666"));
        textView.setTextColor(Color.parseColor("#6547FF"));
    }

    private void notifyRatioTextColorChange(TextView textView) {
        crop11.setTextColor(Color.parseColor("#FFFFFF"));
        cropFree.setTextColor(Color.parseColor("#FFFFFF"));
        crop34.setTextColor(Color.parseColor("#FFFFFF"));
        crop43.setTextColor(Color.parseColor("#FFFFFF"));
        crop916.setTextColor(Color.parseColor("#FFFFFF"));
        crop169.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setTextColor(Color.parseColor("#6547FF"));
    }

}
