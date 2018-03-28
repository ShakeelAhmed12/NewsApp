package com.example.shakeel.newsapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QueryUtil {

    private static final String TAG = QueryUtil.class.getSimpleName();

    private QueryUtil(){

    }

    public static List<News> extractFeatureFromJSON(String newsJSON){

        List<News> newsList = new ArrayList<>();

        try{
            JSONObject jsonResponse = new JSONObject(newsJSON);

            JSONObject response = jsonResponse.getJSONObject("response");

            JSONArray result = response.getJSONArray("results");

            for(int i = 0; i < result.length(); i++){
                JSONObject currentNews = result.getJSONObject(i);

                String title = currentNews.getString("webTitle");
                String date = currentNews.getString("webPublicationDate");
                String url = currentNews.getString("webUrl");
                String section = currentNews.getString("sectionName");

                JSONArray tagsArray = currentNews.getJSONArray("tags");
                String author = "";

                if(tagsArray.length() == 0){
                    author = null;
                } else{
                        JSONObject authorObject = tagsArray.getJSONObject(i);
                        author = authorObject.getString("webTitle");
                    }
                newsList.add(new News(title, date, url, section, author));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsList;
    }

    public static List<News> fetchNews(String requestURL){
        URL url = createUrl(requestURL);

        String jsonResponse = null;

        try{
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(TAG, "Problem making http request", e);
        }

        List<News> news = extractFeatureFromJSON(jsonResponse);
        return news;
    }

    private static URL createUrl(String stringUrl){
        URL url = null;

        try{
            url = new URL(stringUrl);
        }catch (MalformedURLException e){
            Log.e(TAG, "Problem building url", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";

        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
                Log.e(TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(TAG, "Problem retrieving the earthquak JSON results",e);
        }finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();

        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();

            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
