package com.example.administrator.recyclerviewalbum;

import android.graphics.Bitmap;

/**
 * Created by Lei Xiaoyue on 2015-11-05.
 */
public class ImageTask extends RecyclerViewTask implements ImageDecodeRunnable.ImageDecodeTaskMethod {
    private String mImagePath;
    private Bitmap mBitmap;

    public ImageTask() {
        this(0, null, null);
    }

    public ImageTask(int position, String imagePath, ImageAdapter.ViewHolder viewHolder) {
        mPosition = position;
        mImagePath = imagePath;
        mViewHolder = viewHolder;
        mImageManager = ImageManager.getInstance();
    }

    @Override
    public void decodeHandleDone( Bitmap bitmap) {
        mImageManager.handleDecodeDone(this, bitmap);
    }

    public String getImagePath() {
        return mImagePath;
    }

    public ImageAdapter.ViewHolder getViewHolder() {
        return (ImageAdapter.ViewHolder)mViewHolder;
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
}
