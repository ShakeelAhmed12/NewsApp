package com.example.shakeel.newsapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Activity context, ArrayList<News> newsList) {
        super(context, 0, newsList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View listView = convertView;
        if(listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        News news = getItem(position);

        TextView titleTextView = listView.findViewById(R.id.news_title);
        TextView dateTextView = listView.findViewById(R.id.news_date);
        TextView sectionName = listView.findViewById(R.id.news_section);
        TextView authorName = listView.findViewById(R.id.news_author);

        titleTextView.setText(news.getTitle());
        dateTextView.setText(formatDate(news.getDate()));
        sectionName.setText(news.getSectionName());
        authorName.setText(news.getAuthor());

        return listView;
    }

    private String formatDate(String dateObject) {
        String jsonDate = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat jsonFormatter = new SimpleDateFormat(jsonDate, Locale.UK);

        try{
            Date parsedJsonDate = jsonFormatter.parse(dateObject);
            String finalDatePattern = "dd MMMM yyy";
            SimpleDateFormat finalDate = new SimpleDateFormat(finalDatePattern, Locale.UK);
            return finalDate.format(parsedJsonDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
