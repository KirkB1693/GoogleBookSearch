package com.example.android.googlebooksearch;

import android.graphics.drawable.Drawable;

public class Book {

    private String mTitle;
    private Drawable mThumbnail;
    private String mDescription;
    private String mAuthor;
    private String mUrl;
    private float mRating;

    public Book (String title, Drawable thumbnail, String description, String author, String url, float rating) {
        mTitle = title;
        mThumbnail = thumbnail;
        mDescription = description;
        mAuthor = author;
        mUrl = url;
        mRating = rating;
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

    public float getRating() {
        return mRating;
    }

    @Override
    public String toString() {
        return "Book {"+
                "mTitle = "+ mTitle +
                ", mThumbnail = "+ mThumbnail +
                ", mDescription =" + mDescription +
                ", mAuthor ="+ mAuthor +
                ", mUrl ="+ mUrl +
                ", mRating ="+ mRating +
                "}";
    }
}
