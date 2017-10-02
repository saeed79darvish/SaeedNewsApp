package com.example.saeed.saeednewsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SaeedNewsAdapter extends ArrayAdapter<SaeedNews> {

    public SaeedNewsAdapter(Context context, List<SaeedNews> SaeedNews) {
        super(context, 0, SaeedNews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }


        SaeedNews currentNews = getItem(position);
        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        titleView.setText(currentNews.getTitle());

        TextView newsSectionView = (TextView) listItemView.findViewById(R.id.news_section);
        newsSectionView.setText(currentNews.getNewsSection());


        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("EEEE dd MMMM yyyy", Locale.US);
        try {
            Date date = dateFormat.parse(currentNews.getNewsDate());

            String parsedaDate = dateFormat2.format(date);
            dateView.setText(parsedaDate);
        } catch (ParseException e) {
        }
        return listItemView;
    }
}
