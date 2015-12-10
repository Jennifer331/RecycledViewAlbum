package com.example.administrator.recyclerviewalbum;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Lei Xiaoyue on 2015-11-02.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>
        implements View.OnClickListener {
    private static final String TAG = "ImageDecode";

    private Context mContext;
    private List<Image> mData;

    private ImageManager mImageManager;
    private OnImageItemClickListener mListener;

    public interface OnImageItemClickListener {
        void onImageItemClick(View v, Image imageInfo);
    }

    public ImageAdapter(Context context, int albumId) {
        mContext = context;
        mData = new ArrayList<Image>();
        mImageManager = ImageManager.getInstance();
        loadData(albumId);
        // setHasStableIds(true);
    }

    public void setListener(OnImageItemClickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mViewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cast_view,
                parent, false);
        View cardView = mViewHolder.findViewById(R.id.card_view);
        cardView.setOnClickListener(this);
        ImageAdapter.ViewHolder vh = new ViewHolder((CardView) cardView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.v(TAG, "in onBindViewHolder " + position);
        holder.itemView.setTag(mData.get(position));
        mImageManager.loadImage(position, mData.get(position).getImagePath(), holder);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View v) {
        mListener.onImageItemClick(v,(Image)v.getTag());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ViewHolder(CardView view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageview);
        }

    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        Log.v(TAG, "in onViewRecycled " + holder.getLayoutPosition());
        mImageManager.cancelTask(holder);
    }

    private void loadData(final int albumId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cur = null;
                ContentResolver contentResolver = mContext.getContentResolver();
                String where = MediaStore.Images.Media.BUCKET_ID + "=?";
                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                cur = contentResolver.query(uri, Image.PROJECTION, where, new String[] { albumId + "" },
                        MediaStore.Images.Media.DATE_ADDED);
                if (cur != null && cur.moveToLast()) {
                    do {
                        String data = cur.getString(Image.PROJECTION_DATA);
                        int albumId = cur.getInt(Image.PROJECTION_BUCKET_ID);
                        if (data != null) {
                            mData.add(new Image(data,albumId));
                        }
                    } while (cur.moveToPrevious());
                }

                uri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
                cur = contentResolver.query(uri, Image.PROJECTION, null, null,
                        MediaStore.Images.Media.TITLE);
                if (cur != null && cur.moveToFirst()) {
                    while (cur.moveToNext()) {
                        String data = cur.getString(Image.PROJECTION_DATA);
                        int albumId = cur.getInt(Image.PROJECTION_BUCKET_ID);
                        if (data != null) {
                            mData.add(new Image(data,albumId));
                        }
                    }
                }
            }
        }).run();

    }
}