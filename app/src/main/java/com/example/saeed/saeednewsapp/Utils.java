package com.example.saeed.saeednewsapp;

import android.text.TextUtils;
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
import java.util.ArrayList;
import java.util.List;


public class Utils {

    static final String RESPONSE = "response";
    static final String RESULTS = "results";
    static final String NEWS_TITLE = "webTitle";
    static final String SECTION = "sectionName";
    static final String DATE = "webPublicationDate";
    static final String NEWS_URL = "webUrl";
    private static final String LOG_TAG = Utils.class.getSimpleName();


    private Utils() {
    }

    public static List<SaeedNews> fetchSaeedNewsData(String requestUrl) {

        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {

            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<SaeedNews> SaeedNews = extractFeatureFromJson(jsonResponse);
        return SaeedNews;
    }


    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {

            url = new URL(stringUrl);
        } catch (MalformedURLException e) {

            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {

                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Saeed News JSON results.", e);
        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<SaeedNews> extractFeatureFromJson(String SaeedNewsJSON) {

        if (TextUtils.isEmpty(SaeedNewsJSON)) {
            return null;
        }

        List<SaeedNews> SaeedNews = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(SaeedNewsJSON);
            JSONObject responseNewsObject = baseJsonResponse.getJSONObject(RESPONSE);
            JSONArray resultsNewsArray = responseNewsObject.getJSONArray(RESULTS);

            for (int i = 0; i < resultsNewsArray.length(); i++) {

                JSONObject currentSaeedNews = resultsNewsArray.getJSONObject(i);
                String title = "N/A";
                if (currentSaeedNews.has(NEWS_TITLE)) {
                    title = currentSaeedNews.getString(NEWS_TITLE);
                }

                String section = "N/A";
                if (currentSaeedNews.has(SECTION)) {
                    section = currentSaeedNews.getString(SECTION);
                }
                String date = "N/A";
                if (currentSaeedNews.has(DATE)) {
                    date = currentSaeedNews.getString(DATE);
                }
                String newsUrl = "N/A";
                if (currentSaeedNews.has(NEWS_URL)) {
                    newsUrl = currentSaeedNews.getString(NEWS_URL);
                }
                SaeedNews newsSaeedNews = new SaeedNews(title, section, date, newsUrl);
                SaeedNews.add(newsSaeedNews);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the Saeed News JSON results", e);
        }
        return SaeedNews;
    }
}
