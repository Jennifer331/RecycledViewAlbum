package com.example.administrator.recyclerviewalbum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

public class ImageActivity extends Activity {
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageManager mImageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageManager = ImageManager.getInstance();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

//         mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mLayoutManager = new StaggeredGridLayoutManager(4, 1);
//         mLayoutManager = new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(mLayoutManager);

        int albumId = getIntent().getIntExtra(Album.ALBUM_ID + "",0);
        mAdapter = new ImageAdapter(this,albumId);
        mAdapter.setListener(new ImageAdapter.OnImageItemClickListener() {
            @Override
            public void onImageItemClick(View v, Image imageInfo) {
                Intent intent = new Intent(ImageActivity.this,SingleImageActivity.class);
                intent.putExtra(Image.PROJECTION_DATA+"",imageInfo.getImagePath());
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

    }

}
