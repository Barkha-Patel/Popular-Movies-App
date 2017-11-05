package com.example.android.popular_movies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Barkha on 25-Apr-16.
 */
public class MovieCursorAdapter extends CursorAdapter  {

    public MovieCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    /**
     * Makes a new view to hold the data pointed to by cursor.
     *
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.custom_item_row, parent, false);
    }

    /**
     * Bind an existing view to the data pointed to by cursor
     *
     * @param view    Existing view, returned earlier by newView
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String poster_path = cursor.getString(cursor.getColumnIndex(MovieContract.FavEntry.POSTER_PATH));


        ImageView imageViewcustom = (ImageView) view.findViewById(R.id.custom);
        Picasso.with(context).load("https://image.tmdb.org/t/p/w185" + poster_path)
                .into(imageViewcustom);

    }
}


