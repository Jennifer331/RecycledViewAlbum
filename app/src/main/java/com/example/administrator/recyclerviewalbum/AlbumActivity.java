package com.example.administrator.recyclerviewalbum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

public class AlbumActivity extends Activity {
    private RecyclerView mRecyclerView;
    private AlbumAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageManager mImageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageManager = ImageManager.getInstance();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

//        mLayoutManager = new LinearLayoutManager(this);
//        mLayoutManager = new StaggeredGridLayoutManager(4, 1);
        mLayoutManager = new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new AlbumAdapter(this);
        mAdapter.setOnRecycledViewItemClickListener(new AlbumAdapter.OnAlbumItemClickListener(){
            @Override
            public void onAlbumItemClick(View v, int tag) {
                Toast.makeText(AlbumActivity.this,String.valueOf(tag),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AlbumActivity.this,ImageActivity.class);
                intent.putExtra(Album.ALBUM_ID + "",tag);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

    }

}
