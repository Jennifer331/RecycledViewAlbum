package com.example.administrator.recyclerviewalbum;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by Lei Xiaoyue on 2015-11-11.
 */
public class SingleImageActivity extends Activity{
    private ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_single_image_view);
        mImageView = (ImageView)findViewById(R.id.imageview);

        String path = getIntent().getStringExtra(Image.PROJECTION_DATA + "");
        Bitmap bitmap = BitmapUtil.decodeBitmapFromFile(path, 1000, 1000);
        mImageView.setImageBitmap(bitmap);
    }
}
