package com.example.saeed.saeednewsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SaeedNewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<SaeedNews>> {

    private static final String LOG_TAG = SaeedNewsActivity.class.getName();
    private static final String GUARDIAN_REQUEST_URL = "http://content.guardianapis.com/search?&=";
    private static final int NEWS_LOADER_ID = 1;
    private static final String API_KEY = "api-key";
    private static final String KEY = "59d0bf63-14c5-4f77-b80d-cbd12411469f";
    public ProgressBar progressBar;
    private SaeedNewsAdapter newsAdapter;
    private TextView emptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saeed_news);

        ListView newsListView = (ListView) findViewById(R.id.list_view);
        emptyStateTextView = (TextView) findViewById(R.id.empty_text_view);
        newsListView.setEmptyView(emptyStateTextView);

        newsAdapter = new SaeedNewsAdapter(this, new ArrayList<SaeedNews>());
        newsListView.setAdapter(newsAdapter);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                SaeedNews currentSaeedNews = newsAdapter.getItem(position);
                Uri SaeedNewsUri = Uri.parse(currentSaeedNews.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, SaeedNewsUri);
                websiteIntent.setData(SaeedNewsUri);
                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.e(LOG_TAG, "There is an internet connection.");

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            Log.e(LOG_TAG, "There is no internet connection.");
            View progressBar = findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);

            emptyStateTextView.setText(R.string.no_internet_connection);
            emptyStateTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Loader<List<SaeedNews>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String searchSection = sharedPreferences.getString(
                getString(R.string.settings_search_by_news_key),
                getString(R.string.settings_news_label));

        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", searchSection);
        uriBuilder.appendQueryParameter(API_KEY, KEY);

        return new SaeedNewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<SaeedNews>> loader, List<SaeedNews> SaeedNews) {

        View progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        newsAdapter.clear();

        if (SaeedNews != null && !SaeedNews.isEmpty()) {
            newsAdapter.addAll(SaeedNews);

            if (SaeedNews.isEmpty()) {
                emptyStateTextView.setText(R.string.no_news);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<SaeedNews>> loader) {
        newsAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(SaeedNewsActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.restartLoader(NEWS_LOADER_ID, null, this);
    }
}
