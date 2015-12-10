package com.example.administrator.recyclerviewalbum;

import android.graphics.Bitmap;

/**
 * Created by Lei Xiaoyue on 2015-11-05.
 */
public class ImageCompositionRunnable implements Runnable {
    private static final String TAG = "CompositionSample";
    private CompositionTask mCompositionTask;
    public static final int COVER_WIDTH = 500;
    public static final int COVER_HEIGHT = 500;
    public static final int COVER_MERGE_PIC_NUM = 3;

    interface ImageCompositionTaskMethod {
        void handleCompositionDone(Bitmap bitmap);
    }

    public ImageCompositionRunnable(CompositionTask compositionTask) {
        mCompositionTask = compositionTask;
    }

    public boolean hasCompositionTask() {
        return mCompositionTask == null ? false : true;
    }

    public CompositionTask getImageTask() {
        return mCompositionTask;
    }

    @Override
    public void run() {
        int size = mCompositionTask.getImagePaths().size();
        Bitmap[] bitmaps = new Bitmap[size > COVER_MERGE_PIC_NUM ? COVER_MERGE_PIC_NUM : size];
        for (int i = 0; i < size && i < COVER_MERGE_PIC_NUM; i++) {
            bitmaps[i] = BitmapUtil.decodeBitmapFromFile(mCompositionTask.getImagePaths().get(i),
                    COVER_WIDTH, COVER_HEIGHT);
        }

        Bitmap cover = BitmapUtil.pileUpBitmaps(bitmaps, COVER_WIDTH, COVER_HEIGHT);
        mCompositionTask.handleCompositionDone(cover);
    }

}
