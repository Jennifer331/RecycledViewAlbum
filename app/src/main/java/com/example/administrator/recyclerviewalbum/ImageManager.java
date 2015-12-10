package com.example.administrator.recyclerviewalbum;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;

/**
 * Created by Lei Xiaoyue on 2015-11-05.
 */
public class ImageManager {
    private static final String TAG = "ImageDecodeCancelled";
    // Sets the amount of time an idle thread will wati for a task before
    // terminating
    private static final int KEEP_ALIVE_TIME = 1;

    // Handler message state
    private static final int START_DECODE = 0;
    private static final int DECODE_DONE = 1;
    private static final int COMPOSITE_DONE = 2;

    // the size of memory
    private static final int DEFAULT_MEM_SIZE = 1024 * 1024 * 5;// 5MB

    // Sets the Time Unit to seconds
    private static final TimeUnit KEEP_ALICE_TIME_UNIT;

    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    private final BlockingQueue<Runnable> mDecodeWorkQueue;

    private final ThreadPoolExecutor mDecodeThreadPool;

    // An Object that manages the Messages in the Thread
    private static Handler mHandler;

    private LruCache<String, Bitmap> mMemoryCache;

    // A single instance of ImageManager,used to implement the singleton pattern
    private static ImageManager mInstance = null;

    // A static block that sets class fields
    static {
        // The time unit for "keep alive" is in seconds
        KEEP_ALICE_TIME_UNIT = TimeUnit.SECONDS;

        // Creates a single static instance of ImageManager
        mInstance = new ImageManager();
    }

    public static ImageManager getInstance() {
        return mInstance;
    }

    private ImageManager() {
        mDecodeWorkQueue = new LinkedBlockingQueue<Runnable>();
        mDecodeThreadPool = new ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES,
                KEEP_ALIVE_TIME, KEEP_ALICE_TIME_UNIT, mDecodeWorkQueue);

        mMemoryCache = new LruCache<String, Bitmap>(DEFAULT_MEM_SIZE) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case START_DECODE: {
                        break;
                    }
                    case DECODE_DONE: {
                        ImageTask task = (ImageTask) msg.obj;
                        ImageAdapter.ViewHolder viewHolder = task.getViewHolder();
                        if (task.getPosition() == viewHolder.getLayoutPosition()) {
                            ImageView imageView = viewHolder.imageView;
                            if (task.hasBitmap()) {
                                imageView.setImageBitmap(task.getBitmap());
                                imageView.invalidate();
                            }
                        }
                        break;
                    }
                    case COMPOSITE_DONE:{
                        CompositionTask task = (CompositionTask) msg.obj;
                        AlbumAdapter.ViewHolder viewHolder = task.getViewHolder();
                        if (task.getPosition() == viewHolder.getLayoutPosition()) {
                            ImageView imageView = viewHolder.imageView;
                            if (task.hasBitmap()) {
                                imageView.setImageBitmap(task.getBitmap());
                                imageView.invalidate();
                            }
                        }
                        break;
                    }
                }
            }
        };
    }

    public static void cancelTask(ImageAdapter.ViewHolder viewHolder) {
        if (viewHolder == null || viewHolder.imageView == null) {
            return;
        }
        int cancelPosition = viewHolder.getLayoutPosition();

        ImageDecodeRunnable[] runnableArray = new ImageDecodeRunnable[mInstance.mDecodeWorkQueue
                .size()];
        mInstance.mDecodeWorkQueue.toArray(runnableArray);
        synchronized (mInstance) {
            for (ImageDecodeRunnable runnable : runnableArray) {
                if (runnable != null && runnable.getImageTask().getPosition() == cancelPosition) {
                    mInstance.mDecodeThreadPool.remove(runnable);
                }
            }
        }
    }

    public static void cancelAll() {
        ImageDecodeRunnable[] runnableArray = new ImageDecodeRunnable[mInstance.mDecodeWorkQueue
                .size()];
        mInstance.mDecodeWorkQueue.toArray(runnableArray);
        synchronized (mInstance) {
            for (ImageDecodeRunnable runnable : runnableArray) {
                mInstance.mDecodeThreadPool.remove(runnable);
            }
        }
    }

    public void loadImage(int position, String imagePath, ImageAdapter.ViewHolder viewHolder) {
        ImageView imageView = viewHolder.imageView;
        // find the image in to memory first
        if (mMemoryCache != null) {
            Bitmap bitmap = mMemoryCache.get(imagePath);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                return;
            }
        }

        // not in memory,start loading
        final ImageTask task = new ImageTask(position, imagePath, viewHolder);
        Resources resources = imageView.getResources();
        AsyncDrawable asyncDrawable = new AsyncDrawable(resources,
                BitmapFactory.decodeResource(resources, R.drawable.empty_photo), task);
        // set the placeholder of the loading image
        imageView.setImageDrawable(asyncDrawable);
        // add the task to the thread pool for execution
        mInstance.mDecodeThreadPool.execute(new ImageDecodeRunnable(task));

    }

    public void compositeImages(int position, List<String> imagePaths,
            AlbumAdapter.ViewHolder viewHolder) {
        ImageView imageView = viewHolder.imageView;
        final CompositionTask task = new CompositionTask(position, imagePaths, viewHolder);
        Resources resources = imageView.getResources();
        AsyncDrawable asyncDrawable = new AsyncDrawable(resources,
                BitmapFactory.decodeResource(resources, R.drawable.empty_photo), task);
        // set the placeholder of the loading image
        imageView.setImageDrawable(asyncDrawable);
        // add the task to the thread pool for execution
        mInstance.mDecodeThreadPool.execute(new ImageCompositionRunnable(task));
    }

    public void handleDecodeDone(ImageTask task, Bitmap bitmap) {

        // add the bitmap to the memory cache
        if (mMemoryCache != null && bitmap != null) {
            synchronized (mMemoryCache) {
                if (mMemoryCache.get(task.getImagePath()) == null) {
                    mMemoryCache.put(task.getImagePath(), bitmap);
                }
            }
        }

        // refresh the view
        ImageView imageView = task.getViewHolder().imageView;
        if (bitmap != null && imageView != null) {
            task.setBitmap(bitmap);
            Message comleteMessage = mHandler.obtainMessage(DECODE_DONE, task);
            comleteMessage.sendToTarget();
        }
    }

    public void handleCompositionDone(CompositionTask task, Bitmap bitmap) {
        ImageView imageView = task.getViewHolder().imageView;
        if (bitmap != null && imageView != null) {
            task.setBitmap(bitmap);
            Message comleteMessage = mHandler.obtainMessage(COMPOSITE_DONE, task);
            comleteMessage.sendToTarget();
        }
    }

    /**
     * A custom Drawable that will be attached to the imageView while the work
     * is in progress. Contains a reference to the actual worker task, so that
     * it can be stopped if a new binding is required, and makes sure that only
     * the last started worker process can bind its result, independently of the
     * finish order.
     */
    private static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<RecyclerViewTask> mTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, RecyclerViewTask task) {
            super(res, bitmap);
            mTaskReference = new WeakReference<RecyclerViewTask>(task);
        }

        public RecyclerViewTask getTaskReference() {
            return mTaskReference.get();
        }
    }

}
