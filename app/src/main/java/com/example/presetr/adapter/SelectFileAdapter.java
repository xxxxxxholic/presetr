package com.example.presetr.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.presetr.R;
import com.example.presetr.activity.GetPicPSTActivity;
import com.example.presetr.view.RoundImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectFileAdapter extends RecyclerView.Adapter<SelectFileAdapter.ViewHolder> {
    List<String> parent_names;
    HashMap<String,List<String>> map;
    List<String> all_pic = new ArrayList<>(50);


    public SelectFileAdapter(List<String> parent_names, HashMap<String, List<String>> map) {
        this.parent_names = parent_names;
        this.map = map;
        for(String s: parent_names){
            all_pic.addAll(map.get(s));
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.select_layout)
        LinearLayout linearLayout;
        @BindView(R.id.item_select_pic_pic)
        RoundImageView imageView;
        @BindView(R.id.item_select_pic_file_name)
        TextView textView;
        @BindView(R.id.item_select_pic_file_count)
        TextView textView_count;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            GetPicPSTActivity activity = (GetPicPSTActivity) v.getContext();
            activity.getGetPicSelectRecycleView().setVisibility(View.INVISIBLE);
            activity.getGetPicPstMask().setVisibility(View.INVISIBLE);
            activity.getGetPicPstSourceText().setText(textView.getText().toString());
            if(textView.getText().toString().equals("All")){
                activity.setPic_adapter(new GetPicPSTAdapter(all_pic));
            }else {
                activity.setPic_adapter(new GetPicPSTAdapter(map.get(textView.getText().toString())));
            }
            StaggeredGridLayoutManager sManager =
                    new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
            sManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
            activity.getGetPicPstRecycleView().setLayoutManager(sManager);
            activity.getGetPicPstRecycleView().setAdapter(activity.getPic_adapter());

        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_select_pic_file,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position==0){
            Glide.with(holder.imageView.getContext())
                    .load(all_pic.get(0))
                    .placeholder(R.drawable.hint_buffer)
                    .into(holder.imageView);
            holder.textView.setText("All");
            holder.textView_count.setText("("+all_pic.size()+")");
        }else if(position>0){
            String name = parent_names.get(position-1);
            Glide.with(holder.imageView.getContext())
                    .load(map.get(name).get(0))
                    .placeholder(R.drawable.hint_buffer)
                    .into(holder.imageView);
            holder.textView.setText(name);
            holder.textView_count.setText("("+map.get(name).size()+")");
        }
    }

    @Override
    public int getItemCount() {
        return parent_names.size()+1;
    }

}
