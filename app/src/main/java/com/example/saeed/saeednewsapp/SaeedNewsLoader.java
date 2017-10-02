package com.example.saeed.saeednewsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;


public class SaeedNewsLoader extends AsyncTaskLoader<List<SaeedNews>> {

    private String mUrl;

    public SaeedNewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<SaeedNews> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        List<SaeedNews> SaeedNews = Utils.fetchSaeedNewsData(mUrl);
        return SaeedNews;
    }
}
