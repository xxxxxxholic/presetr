package com.example.presetr.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.presetr.R;

public class SaveAlertDialog extends Dialog {
    public SaveAlertDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder{
        private View mLayout;
        private TextView mMessage;
        private ImageView ok_image;
        private SaveAlertDialog mDialog;
        private View.OnClickListener mListener;
        public Builder(Context context){
            mDialog = new SaveAlertDialog(context, R.style.Theme_AppCompat_Dialog);
            LayoutInflater inflater
                    = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mLayout = inflater.inflate(R.layout.alert_dialog,null,false);
            mDialog.addContentView(mLayout,new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            mMessage = mLayout.findViewById(R.id.file_path_name);
            ok_image = mLayout.findViewById(R.id.save_ok);
        }
        public Builder setMessage(String message){
            mMessage.setText(message);
            return this;
        }
        public Builder setButton(View.OnClickListener listener){
            mListener = listener;
            return this;
        }
        public SaveAlertDialog create(){
            ok_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    mListener.onClick(v);
                }
            });
            mDialog.setContentView(mLayout);
            mDialog.setCancelable(true);
            mDialog.setCanceledOnTouchOutside(true);
            return mDialog;
        }
    }
}
