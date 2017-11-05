package com.example.android.popular_movies;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Barkha on 24-Apr-16.
 */
public class MovieProvider extends ContentProvider{
    private static final int MOVIES = 1;
    private static final int MOVIES_ID = 2;


    private  static final UriMatcher uriMather = new UriMatcher(UriMatcher.NO_MATCH);



    static {
        uriMather.addURI(MovieContract.AUTHORITY, MovieContract.BASE_PATH, MOVIES);
        uriMather.addURI(MovieContract.AUTHORITY, MovieContract.BASE_PATH + "/#", MOVIES_ID);
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {

       DBOpenHelper openHelper = new DBOpenHelper(getContext());
        database = openHelper.getWritableDatabase();

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        if (uriMather.match(uri) == MOVIES_ID){
            selection = MovieContract.FavEntry.MOVIE_ID + "=" + uri.getLastPathSegment();
        }

        return database.query(MovieContract.FavEntry.MOVIE_TABLE, MovieContract.FavEntry.ALL_COLUMNS,
                selection, null,null, null,
                MovieContract.FavEntry.TITLE + " DESC");

    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long id = database.insert(MovieContract.FavEntry.MOVIE_TABLE, null, values);
        return  Uri.parse(MovieContract.BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        return database.delete(MovieContract.FavEntry.MOVIE_TABLE, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return database.update(MovieContract.FavEntry.MOVIE_TABLE, values, selection, selectionArgs);
    }

}
