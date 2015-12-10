package com.example.administrator.recyclerviewalbum;

import java.util.ArrayList;
import java.util.List;

import android.provider.MediaStore;

/**
 * Created by Lei Xiaoyue on 2015-11-10.
 */
public class Album {
    private int albumId;
    private String albumName;
    private List<String> profileImages;

    public static final String[] PROJECTION = new String[]{
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    };

    public static final String[] PROFILE_PROJECTION = new String[]{
            MediaStore.Images.Media.DATA
    };

    public static final int ALBUM_ID = 0;
    public static final int ALBUM_NAME = 1;

    public static final int ALBUM_DATA = 0;

    public Album(){

    }

    public Album(int albumId,String albumName){
        this.albumId = albumId;
        this.albumName = albumName;
        profileImages = new ArrayList<String>();
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public List<String> getProfileImages() {
        return profileImages;
    }

    public void setProfileImages(List<String> profileImages) {
        this.profileImages = profileImages;
    }

    public void addProfileImage(String profileImage){
        if(profileImage != null) {
            this.profileImages.add(profileImage);
        }
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
}
