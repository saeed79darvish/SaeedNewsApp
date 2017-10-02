package com.example.saeed.saeednewsapp;

public class SaeedNews {

    private String newsTitle;
    private String newsSection;
    private String newsDate;
    private String newsUrl;

    public SaeedNews(String title, String section, String date, String url) {
        newsTitle = title;
        newsSection = section;
        newsDate = date;
        newsUrl = url;
    }

    public String getTitle() {
        return newsTitle;
    }

    public String getNewsSection() {
        return newsSection;
    }

    public String getNewsDate() {
        return newsDate;
    }

    public String getUrl() {
        return newsUrl;
    }
}
