package com.example.android.popular_movies;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Barkha on 24-Apr-16.
 */
public class MovieContract {

    public static final String AUTHORITY = "com.example.android.popular_movies.movieprovider";
    public static final String BASE_PATH = "movies";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    public static final class FavEntry implements BaseColumns {

        public static final String MOVIE_TABLE = "movie_table";
        public static final String ID = "_id";
        public static final String MOVIE_ID = "movie_id";
        public static final String BACKDROP_PATH = "backdrop_path";
        public static final String TITLE = "title";
        public static final String OVERVIEW = "overview";
        public static final String RELEASE_DATE = "release_date";
        public static final String POSTER_PATH = "poster_path";
        public static final String POPULARITY = "popularity";
        public static final String VOTE_AVERAGE = "vote_average";
        public static final String VOTE_COUNT = "vote_count";
        public static final String[] ALL_COLUMNS =
                {ID, MOVIE_ID, TITLE,
                        OVERVIEW, RELEASE_DATE, POSTER_PATH,
                        VOTE_AVERAGE};

    }
}

