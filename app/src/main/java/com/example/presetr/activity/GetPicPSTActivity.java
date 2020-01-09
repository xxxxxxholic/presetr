package com.example.presetr.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.presetr.MyApplication;
import com.example.presetr.R;
import com.example.presetr.adapter.GetPicPSTAdapter;
import com.example.presetr.adapter.SelectFileAdapter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GetPicPSTActivity extends BaseActivity {
    public static final int REQUEST_TAKE_PHOTO = 1;
    private static final String TAG = "GetPicPSTActivity";
    @BindView(R.id.get_pic_pst_back)
    ImageView getPicPstBack;
    @BindView(R.id.get_pic_pst_source_text)
    TextView getPicPstSourceText;
    @BindView(R.id.get_pic_pst_select)
    ImageView getPicPstSelect;
    @BindView(R.id.get_pic_pst_recycleView)
    RecyclerView getPicPstRecycleView;
    @BindView(R.id.get_pic_pst_progress_bar)
    ProgressBar getPicPstProgressBar;
    @BindView(R.id.get_pic_select_recycleView)
    RecyclerView getPicSelectRecycleView;
    @BindView(R.id.get_pic_pst_mask)
    ImageView getPicPstMask;

    private HashMap<String, List<String>> mImageInfo;
    private List<String> parent_file_names;
    private GetPicPSTAdapter pic_adapter;
    private SelectFileAdapter selectFileAdapter;

    private int filter_position;
    private String currentPhotoPath;
    private String imageFileName;
    private File photoFile;
    private Uri photoURI;

    public String getCurrentPhotoPath() {
        return currentPhotoPath;
    }

    public void setCurrentPhotoPath(String currentPhotoPath) {
        this.currentPhotoPath = currentPhotoPath;
    }

    public int getFilter_position() {
        return filter_position;
    }

    public void setFilter_position(int filter_position) {
        this.filter_position = filter_position;
    }

    public List<String> getAll_pic() {
        return all_pic;
    }

    public void setAll_pic(List<String> all_pic) {
        this.all_pic = all_pic;
    }

    private List<String> all_pic;

    public ImageView getGetPicPstBack() {
        return getPicPstBack;
    }

    public void setGetPicPstBack(ImageView getPicPstBack) {
        this.getPicPstBack = getPicPstBack;
    }

    public TextView getGetPicPstSourceText() {
        return getPicPstSourceText;
    }

    public void setGetPicPstSourceText(TextView getPicPstSourceText) {
        this.getPicPstSourceText = getPicPstSourceText;
    }

    public ImageView getGetPicPstSelect() {
        return getPicPstSelect;
    }

    public void setGetPicPstSelect(ImageView getPicPstSelect) {
        this.getPicPstSelect = getPicPstSelect;
    }

    public RecyclerView getGetPicPstRecycleView() {
        return getPicPstRecycleView;
    }

    public void setGetPicPstRecycleView(RecyclerView getPicPstRecycleView) {
        this.getPicPstRecycleView = getPicPstRecycleView;
    }

    public ProgressBar getGetPicPstProgressBar() {
        return getPicPstProgressBar;
    }

    public void setGetPicPstProgressBar(ProgressBar getPicPstProgressBar) {
        this.getPicPstProgressBar = getPicPstProgressBar;
    }

    public RecyclerView getGetPicSelectRecycleView() {
        return getPicSelectRecycleView;
    }

    public void setGetPicSelectRecycleView(RecyclerView getPicSelectRecycleView) {
        this.getPicSelectRecycleView = getPicSelectRecycleView;
    }

    public ImageView getGetPicPstMask() {
        return getPicPstMask;
    }

    public void setGetPicPstMask(ImageView getPicPstMask) {
        this.getPicPstMask = getPicPstMask;
    }

    public HashMap<String, List<String>> getmImageInfo() {
        return mImageInfo;
    }

    public void setmImageInfo(HashMap<String, List<String>> mImageInfo) {
        this.mImageInfo = mImageInfo;
    }

    public List<String> getParent_file_names() {
        return parent_file_names;
    }

    public void setParent_file_names(List<String> parent_file_names) {
        this.parent_file_names = parent_file_names;
    }

    public GetPicPSTAdapter getPic_adapter() {
        return pic_adapter;
    }

    public void setPic_adapter(GetPicPSTAdapter pic_adapter) {
        this.pic_adapter = pic_adapter;
    }

    public SelectFileAdapter getSelectFileAdapter() {
        return selectFileAdapter;
    }

    public void setSelectFileAdapter(SelectFileAdapter selectFileAdapter) {
        this.selectFileAdapter = selectFileAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_pic_pst);
        ButterKnife.bind(this);
        filter_position = getIntent().getExtras().getInt("position");
        mImageInfo = new HashMap<>(30);
        parent_file_names = new ArrayList<>(20);
        GetImageTask task = new GetImageTask(this);
        task.execute();
    }

    private void savePhotoToSD(Bitmap bitmap) {
        Log.d(TAG, "将图片保存到指定位置。");
        //创建输出流缓冲区
        BufferedOutputStream os = null;
        try {
            //设置输出流
            os = new BufferedOutputStream(new FileOutputStream(photoFile));
            Log.d(TAG, "设置输出流。");
            //压缩图片，100表示不压缩
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            Log.d(TAG, "保存照片完成。");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    //不管是否出现异常，都要关闭流
                    os.flush();
                    os.close();
                    Log.d(TAG, "刷新、关闭流");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 更新系统图库
     */
    private void updateSystemGallery() {
        //把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(this.getContentResolver(),
                    currentPhotoPath, imageFileName, null);
            Log.d(TAG, "将图片文件插入系统图库。");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + currentPhotoPath)));
        Log.d(TAG, "通知系统图库更新。");
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.d(TAG, "createImageFile: "+storageDir.getAbsolutePath());
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        image.setWritable(true);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, GetPicPSTActivity.REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_TAKE_PHOTO:
                if(resultCode==RESULT_OK){
                    //galleryAddPic();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),
                                photoURI);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //savePhotoToSD(bitmap);
                    updateSystemGallery();
                    Bundle bundle = new Bundle();
                    bundle.putInt("position",getFilter_position());
                    bundle.putString("pic_filepath",currentPhotoPath);
                    startActivity(DiyActivity.class,bundle);
                }
                break;
        }
    }

//    private void galleryAddPic() {
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(currentPhotoPath);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
//    }
    @OnClick({R.id.get_pic_pst_back, R.id.get_pic_pst_source_text,R.id.get_pic_pst_mask})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.get_pic_pst_back:
                finish();
                break;
            case R.id.get_pic_pst_source_text:
                if(getPicSelectRecycleView.getVisibility()==View.INVISIBLE) {
                    getPicSelectRecycleView.setVisibility(View.VISIBLE);
                    getPicPstMask.setVisibility(View.VISIBLE);
                }else if(getPicSelectRecycleView.getVisibility()==View.VISIBLE){
                    getPicSelectRecycleView.setVisibility(View.INVISIBLE);
                    getPicPstMask.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.get_pic_pst_mask:
                getPicSelectRecycleView.setVisibility(View.INVISIBLE);
                getPicPstMask.setVisibility(View.INVISIBLE);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }


    class GetImageTask extends AsyncTask<Void, Void, HashMap<String, List<String>>> {
        GetPicPSTActivity mContext;

        public GetImageTask(GetPicPSTActivity mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected HashMap<String, List<String>> doInBackground(Void... voids) {
            Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver resolver = mContext.getContentResolver();
            //只查询jpeg和png的照片
            Cursor cursor = resolver.query(imageUri, null,
                    MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=?",
                    new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
            if (cursor == null) {
                return null;
            }
            while (cursor.moveToNext()) {
                //获取文件路径名
                String image_path = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
                //获取图片的父路径名
                String parent_path = new File(image_path).getParentFile().getName();
                if(!parent_file_names.contains(parent_path)){
                    parent_file_names.add(parent_path);
                }
                if (!mImageInfo.containsKey(parent_path)) {
                    List<String> child_list = new ArrayList<>();
                    child_list.add(image_path);
                    mImageInfo.put(parent_path, child_list);
                } else {
                    mImageInfo.get(parent_path).add(image_path);
                }
            }
            cursor.close();
            return null;
        }

        @Override
        protected void onPostExecute(HashMap<String, List<String>> stringListHashMap) {
            super.onPostExecute(stringListHashMap);
            getPicPstProgressBar.setVisibility(View.INVISIBLE);
            //设置recyclerView
            StaggeredGridLayoutManager sManager =
                    new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
            sManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
            getPicPstRecycleView.setLayoutManager(sManager);
            all_pic = new ArrayList<>(50);
            for(String s:parent_file_names){
                all_pic.addAll(mImageInfo.get(s));
            }
            pic_adapter = new GetPicPSTAdapter(all_pic);
            getPicPstRecycleView.setAdapter(pic_adapter);

            selectFileAdapter = new SelectFileAdapter(parent_file_names,mImageInfo);
            LinearLayoutManager lManager = new LinearLayoutManager(mContext);
            lManager.setOrientation(RecyclerView.VERTICAL);
            getPicSelectRecycleView.setLayoutManager(lManager);
            getPicSelectRecycleView.setAdapter(selectFileAdapter);
        }
    }

}
