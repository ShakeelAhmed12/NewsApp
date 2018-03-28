package com.example.shakeel.newsapp;

public class News {

    private String mTitle, mDate, mUrl, mAuthor, mSectionName;

    public News(String title, String date, String url, String author, String section){
        mTitle = title;
        mDate = date;
        mUrl = url;
        mAuthor = author;
        mSectionName = section;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDate() {
        return mDate;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getSectionName() {
        return mSectionName;
    }
}
