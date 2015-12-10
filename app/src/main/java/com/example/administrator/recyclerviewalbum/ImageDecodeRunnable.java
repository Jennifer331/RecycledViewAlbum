package com.example.administrator.recyclerviewalbum;


import android.graphics.Bitmap;

/**
 * Created by Lei Xiaoyue on 2015-11-05.
 */
public class ImageDecodeRunnable implements Runnable {
    private static final String TAG = "DecodeSample";
    private ImageTask mImageTask;
    public static final int REQUIRED_WIDTH = 500;
    public static final int REQUIRED_HEIGHT = 500;

    interface ImageDecodeTaskMethod {
        void decodeHandleDone(Bitmap bitmap);
    }

    public ImageDecodeRunnable(ImageTask imageTask) {
        mImageTask = imageTask;
    }

    public boolean hasImageTask() {
        return mImageTask == null ? false : true;
    }

    public ImageTask getImageTask() {
        return mImageTask;
    }

    @Override
    public void run() {
        // Moves the current Thread into the background
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

        Bitmap bitmap = BitmapUtil.decodeBitmapFromFile(mImageTask.getImagePath(), REQUIRED_WIDTH,
                REQUIRED_HEIGHT);

        mImageTask.decodeHandleDone(bitmap);
    }

}
