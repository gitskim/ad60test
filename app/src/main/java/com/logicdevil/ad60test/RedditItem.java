package com.logicdevil.ad60test;

import android.graphics.Bitmap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * Created by suhyunkim on 1/5/15.
 */
public class RedditItem implements Serializable {

    String subredit;
    String author;
    String title;
    String score;
    String detail;
    boolean isSelftext = false;
    String afterTag;

    public String getAfterTag() {
        return afterTag;
    }

    public void setAfterTag(String afterTag) {
        this.afterTag = afterTag;
    }

    public boolean isSelftext() {
        return isSelftext;
    }

    public void setSelftext(boolean isSelftext) {
        this.isSelftext = isSelftext;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getSubredit() {
        return subredit;
    }

    public void setSubredit(String subredit) {
        this.subredit = subredit;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

}
