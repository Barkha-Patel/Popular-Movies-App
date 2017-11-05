package com.example.android.popular_movies;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import com.squareup.picasso.Picasso;


public class ImageAdapter extends BaseAdapter {
    Context context;
    ArrayList<Movie> movieList;

    public ImageAdapter(Context context, ArrayList<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }


    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Movie getItem(int position) {
        return movieList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_item_row, parent, false);
        }
        Movie movieDb = getItem(position);


        ImageView imageViewcustom = (ImageView) convertView.findViewById(R.id.custom);
        Picasso.with(context).load("https://image.tmdb.org/t/p/w500" + movieDb.getPoster_path())
                .into(imageViewcustom);

        return convertView;
    }
}
