package com.example.administrator.recyclerviewalbum;

import android.provider.MediaStore;

/**
 * Created by Lei Xiaoyue on 2015-11-11.
 */
public class Image {
    private String mImagePath;
    private int mAlbumId;

    public final static String[] PROJECTION = new String[] {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.BUCKET_ID};
    public static final int PROJECTION_ID = 0;
    public static final int PROJECTION_DATA = 1;
    public static final int PROJECTION_TITLE = 2;
    public static final int PROJECTION_BUCKET_ID = 3;

    public Image(){

    }

    public Image(String imagePath,int albumId){
        mImagePath = imagePath;
        mAlbumId = albumId;
    }

    public int getAlbumId() {
        return mAlbumId;
    }

    public void setAlbumId(int mAlbumId) {
        this.mAlbumId = mAlbumId;
    }

    public String getImagePath() {
        return mImagePath;
    }

    public void setImagePath(String mImagePath) {
        this.mImagePath = mImagePath;
    }
}
