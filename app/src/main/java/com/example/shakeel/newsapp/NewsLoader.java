package com.example.shakeel.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private String mUrl;

    public NewsLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    public void onStartLoading(){
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if(mUrl == null){
            return null;
        }

        List<News> news = QueryUtil.fetchNews(mUrl);
        return news;
    }
}
