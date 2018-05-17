package com.example.aiden.xtapp.adapder;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.aiden.xtapp.R;
import com.example.aiden.xtapp.entity.base.XtUpload;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StoryListAdapter extends RecyclerView.Adapter<StoryListAdapter.ViewHolder> {

    private final Activity activity;
    private final LayoutInflater inflater;
    private final List<XtUpload> xtUploadList = new ArrayList<>();

    public StoryListAdapter(@NonNull Activity activity) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
    }

    @NonNull
    public List<XtUpload> getXtUploadList() {
        return xtUploadList;
    }

    @Override
    public int getItemCount() {
        return xtUploadList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_story, parent, false), activity);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(xtUploadList.get(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;

//        @BindView(R.id.img_thumb)
//        ImageView imgThumb;

        private final Activity activity;

        private XtUpload xtUpload;

        ViewHolder(@NonNull View itemView, @NonNull Activity activity) {
            super(itemView);
            this.activity = activity;
            ButterKnife.bind(this, itemView);
        }

        void bind(@NonNull XtUpload xtUpload) {
            this.xtUpload = xtUpload;
            tvTitle.setText(xtUpload.getTestData());
//            GlideApp.with(activity).load(story.getImageList().get(0)).placeholder(R.drawable.image_placeholder).into(imgThumb);
        }

//        @OnClick(R.id.btn_item)
//        void onBtnItemClick() {
//            ToastUtils.with(activity).show(story.getTitle());
//        }

    }

}
