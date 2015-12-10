package com.example.administrator.recyclerviewalbum;

import java.util.List;

import android.graphics.Bitmap;

/**
 * Created by Lei Xiaoyue on 2015-11-05.
 */
public class CompositionTask extends RecyclerViewTask implements ImageCompositionRunnable.ImageCompositionTaskMethod {
    private List<String> mImagePaths;
    private Bitmap mBitmap;

    public CompositionTask() {
        this(0,null,null);
    }

    public CompositionTask(int position,List<String> imagePaths,AlbumAdapter.ViewHolder viewHolder) {
        mPosition = position;
        mViewHolder = viewHolder;
        mImagePaths = imagePaths;
        mImageManager = ImageManager.getInstance();
    }

    public List<String> getImagePaths() {
        return mImagePaths;
    }

    public void setImagePaths(List<String> mImagePaths) {
        this.mImagePaths = mImagePaths;
    }

    public AlbumAdapter.ViewHolder getViewHolder() {
        return (AlbumAdapter.ViewHolder)mViewHolder;
    }

    public boolean hasBitmap() {
        return mBitmap == null ? false : true;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    @Override
    public void handleCompositionDone(Bitmap bitmap) {
        mImageManager.handleCompositionDone(this,bitmap);
    }
}
