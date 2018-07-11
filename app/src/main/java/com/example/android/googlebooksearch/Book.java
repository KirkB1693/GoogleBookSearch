package com.example.android.googlebooksearch;

import android.graphics.drawable.Drawable;

public class Book {

    private String mTitle;
    private Drawable mThumbnail;
    private String mDescription;
    private String mAuthor;
    private String mUrl;

    public Book (String title, Drawable thumbnail, String description, String author, String url) {
        mTitle = title;
        mThumbnail = thumbnail;
        mDescription = description;
        mAuthor = author;
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public Drawable getThumbnail() {
        return mThumbnail;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getUrl() {
        return mUrl;
    }

    @Override
    public String toString() {
        return "Book {"+
                "mTitle = "+ mTitle +
                ", mThumbnail = "+ mThumbnail +
                ", mDescription =" + mDescription +
                ", mAuthor ="+ mAuthor +
                ", mUrl ="+ mUrl +
                "}";
    }
}
