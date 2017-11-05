
package com.example.android.popular_movies;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.android.popular_movies.Movies;
import com.example.android.popular_movies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    Context context;
    ArrayList<Movies> movieList;

    public ImageAdapter(Context context, ArrayList<Movies> movieList) {
        this.context = context;
        this.movieList = movieList;
    }


    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Movies getItem(int position) {
        return movieList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 123456000 + position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_item_row, parent, false);
        }
        Movies movieDb = getItem(position);


        ImageView imageViewcustom = (ImageView) convertView.findViewById(R.id.custom);
        Picasso.with(context).load("https://image.tmdb.org/t/p/w500" + movieDb.getPoster_path()).placeholder(R.drawable.noposter).error(R.drawable.noposter)
                .into(imageViewcustom);

        return convertView;
    }
}