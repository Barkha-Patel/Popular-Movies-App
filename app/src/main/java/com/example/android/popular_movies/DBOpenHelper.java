package com.example.android.popular_movies;

/**
 * Created by Barkha on 24-Apr-16.
 */
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBOpenHelper extends SQLiteOpenHelper {

    //Constants for db name and version
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;


    // String to create Table
    private  static final String TABLE_CREATE =
            "CREATE TABLE " + MovieContract.FavEntry.MOVIE_TABLE + " (" +
                    MovieContract.FavEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MovieContract.FavEntry.MOVIE_ID + " INTEGER, " +
                    MovieContract.FavEntry.TITLE + " TEXT, " +
                    MovieContract.FavEntry.OVERVIEW + " TEXT, " +
                    MovieContract.FavEntry.RELEASE_DATE + " TEXT, " +
                    MovieContract.FavEntry.POSTER_PATH + " TEXT, " +
                    MovieContract.FavEntry.VOTE_AVERAGE + " INTEGER" +
                    ")";

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TABLE_CREATE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + MovieContract.FavEntry.MOVIE_TABLE);
        onCreate(db);
    }
}
