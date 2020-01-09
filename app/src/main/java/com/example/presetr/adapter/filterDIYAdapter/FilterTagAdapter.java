package com.example.presetr.adapter.filterDIYAdapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.presetr.MyApplication;
import com.example.presetr.R;
import com.example.presetr.activity.DiyActivity;
import com.example.presetr.model.FilterPackageBean;
import com.example.presetr.view.FilterSelectLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterTagAdapter extends RecyclerView.Adapter<FilterTagAdapter.ViewHolder>{

    private List<String> tags;

    public FilterTagAdapter(List<String> tags) {
        this.tags = tags;
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.item_filter_diy_tag)
        TextView textView;
        @BindView(R.id.item_filter_diy_arrow)
        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            textView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition()-1;
            DiyActivity activity = (DiyActivity) v.getContext();
            if(position>=0){
                FilterPackageBean packageBean = MyApplication.filterPackages.get(position);
                int filterPosition = packageBean.getFilters().get(0).getPosition();
                RecyclerView recyclerView = activity.findViewById(R.id.filter_diy_filter_recycleView);
                recyclerView.scrollToPosition(filterPosition+1);
            }


        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_filter_diy_tag,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position==0){
            holder.textView.setText("All");
        }else {
            holder.textView.setText(tags.get(position-1));
        }
        if(position== FilterSelectLayout.TAG_POSITION){
            holder.view.setVisibility(View.VISIBLE);
            holder.textView.setTextColor(Color.parseColor("#6547FF"));
        }else {
            holder.view.setVisibility(View.INVISIBLE);
            holder.textView.setTextColor(Color.parseColor("#666666"));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
       if(payloads.isEmpty()){
           onBindViewHolder(holder,position);
       }else {
           if(position==FilterSelectLayout.TAG_POSITION){
               holder.view.setVisibility(View.VISIBLE);
               holder.textView.setTextColor(Color.parseColor("#6547FF"));
           }else {
               holder.view.setVisibility(View.INVISIBLE);
               holder.textView.setTextColor(Color.parseColor("#666666"));
           }
       }
    }

    @Override
    public int getItemCount() {
        return tags.size()+1;
    }

}
