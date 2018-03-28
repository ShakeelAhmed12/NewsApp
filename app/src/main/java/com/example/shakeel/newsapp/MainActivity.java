package com.example.shakeel.newsapp;


import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final String KEY = "53aa1415-2410-41cf-9f3f-bd9d403a0d60";
    private static final String URL = "http://content.guardianapis.com/search?";

    private NewsAdapter mAdapter;

    private String newURL;

    private boolean isConnected;

    private TextView emptyTextView;

    private static final int NEWS_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView newsList = findViewById(R.id.list);

        emptyTextView = findViewById(R.id.empty_view);
        final EditText searchQuery = findViewById(R.id.search_query);
        final ImageButton searchBttn = findViewById(R.id.search_icon);

        newsList.setEmptyView(emptyTextView);

        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        newsList.setAdapter(mAdapter);

        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                News news = mAdapter.getItem(i);
                Uri newsUri = Uri.parse(news.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(websiteIntent);
            }
        });

        searchBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!searchQuery.getText().toString().isEmpty()) {
                    Log.e("Test", searchQuery.getText().toString());
                    search(searchQuery.getText().toString());
                }else{
                    emptyTextView.setText(R.string.invalid_search);
                }
            }
        });

        if(isConnected){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        }else {
            emptyTextView.setVisibility(View.GONE);
            Toast.makeText(this, "No internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void search(String query){
        Uri baseUri = Uri.parse(URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("q",query);
        uriBuilder.appendQueryParameter("api-key",KEY);

        newURL = uriBuilder.toString();

        restartLoader();
    }

    private void restartLoader(){
        if(isConnected){
            getLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        }else {
            emptyTextView.setVisibility(View.GONE);
            Toast.makeText(this, "No internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this,newURL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsList) {
        emptyTextView.setText(R.string.not_found);

        mAdapter.clear();

        if(newsList != null && !newsList.isEmpty()){
            mAdapter.addAll(newsList);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
    }
}
