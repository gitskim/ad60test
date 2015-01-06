package com.logicdevil.ad60test;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by suhyunkim on 1/6/15.
 */
public class ThumbnailHolder {
//    public ArrayList<Drawable> thumbnailHolderList = new ArrayList<>();

    public Drawable getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Drawable thumbnail) {
        this.thumbnail = thumbnail;
//        this.thumbnailHolderList.add(thumbnail);
    }

    Drawable thumbnail;
}
