package com.example.administrator.recyclerviewalbum;

import android.support.v7.widget.RecyclerView;


/**
 * Created by Lei Xiaoyue on 2015-11-10.
 */
public class RecyclerViewTask {
    public int mPosition;
    public RecyclerView.ViewHolder mViewHolder;
    public ImageManager mImageManager;

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public RecyclerView.ViewHolder getViewHolder() {
        return mViewHolder;
    }

    public void setViewHolder(RecyclerView.ViewHolder mViewHolder) {
        this.mViewHolder = mViewHolder;
    }

    public ImageManager getImageManager() {
        return mImageManager;
    }

    public void setImageManager(ImageManager mImageManager) {
        this.mImageManager = mImageManager;
    }
}
