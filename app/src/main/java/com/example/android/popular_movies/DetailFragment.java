package com.example.android.popular_movies;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class DetailFragment extends Fragment implements AdapterView.OnItemClickListener {
    //  private Movies movieIntent;
   



    ArrayList<Review> reviewList;
    ArrayList<Trailer> trailerList;
    Movies movieIntent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)

    @Override
    public void onStart() {
        super.onStart();

      // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle arguments = getArguments();
        if (arguments != null) {
            movieIntent = arguments.getParcelable("Movie");
        }
//        movieIntent = arguments.getParcelable("Movie");

      //  final Movies movieIntent = (Movies) getActivity().getIntent().getExtras().getParcelable("Movie");
        if (movieIntent != null) {

            TextView titleText = (TextView) getActivity().findViewById(R.id.title);
            titleText.setText(movieIntent.getTitle());


            ImageView detailImage = (ImageView) getActivity().findViewById(R.id.poster);
            Picasso.with(getActivity()).load("https://image.tmdb.org/t/p/w500" + movieIntent.getPoster_path()).placeholder(R.drawable.noposter).error(R.drawable.noposter)
                    .into(detailImage);


            TextView releaseDateText = (TextView) getActivity().findViewById(R.id.year);
            releaseDateText.setText(movieIntent.getRelease_date());



            TextView overviewText = (TextView) getActivity().findViewById(R.id.overview);
            overviewText.setText(movieIntent.getOverview());


            TextView voteAverageText = (TextView) getActivity().findViewById(R.id.voteaverage);
            voteAverageText.setText(movieIntent.getVote_average());

            Uri uri = Uri.parse(MovieContract.CONTENT_URI + "/" + movieIntent.getId());
            final String movieFilter = MovieContract.FavEntry.MOVIE_ID + "=" + uri.getLastPathSegment();


            Cursor cursor = getActivity().getContentResolver().query(uri,
                    MovieContract.FavEntry.ALL_COLUMNS, movieFilter, null, null, null);



            if (cursor.getCount() == 0) {
                getActivity().findViewById(R.id.favorite_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Would you like to add " + movieIntent.getTitle() + " to your Favorites");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ContentValues values = new ContentValues();
                                values.put(MovieContract.FavEntry.MOVIE_ID, movieIntent.getId());
                                values.put(MovieContract.FavEntry.TITLE, movieIntent.getTitle());
                                values.put(MovieContract.FavEntry.POSTER_PATH, movieIntent.getPoster_path());
                                values.put(MovieContract.FavEntry.RELEASE_DATE, movieIntent.getRelease_date());
                                values.put(MovieContract.FavEntry.VOTE_AVERAGE, movieIntent.getVote_average());
                                values.put(MovieContract.FavEntry.OVERVIEW, movieIntent.getOverview());
                                getActivity().getContentResolver().insert(MovieContract.CONTENT_URI, values);
                                getActivity().findViewById(R.id.favorite_button).setVisibility(View.GONE);
                                getActivity().findViewById(R.id.fav_button).setVisibility(View.VISIBLE);
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                getActivity().findViewById(R.id.fav_button).setVisibility(View.GONE);
                            }
                        });
                        builder.show();


                    }
                });
            } else {
                getActivity().findViewById(R.id.favorite_button).setVisibility(View.GONE);
                getActivity().findViewById(R.id.fav_button).setVisibility(View.VISIBLE);

                getActivity(). findViewById(R.id.fav_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Would you like to remove " + movieIntent.getTitle() + " from your Favorites");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity(). getContentResolver().delete(MovieContract.CONTENT_URI, movieFilter, null);
                                getActivity(). findViewById(R.id.fav_button).setVisibility(View.GONE);
                                getActivity(). findViewById(R.id.favorite_button).setVisibility(View.VISIBLE);
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                getActivity(). findViewById(R.id.favorite_button).setVisibility(View.GONE);
                            }
                        });
                        builder.show();



                    }
                });


            }
            cursor.close();

            if (reviewList == null || trailerList == null) {
                new reviewAndTrailerSync().execute(movieIntent.getId());
            } else {
                setAdapters();
            }


        }

    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
        }
        return (super.onOptionsItemSelected(item));
    }
*/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Trailer clickedTrailer = (Trailer) parent.getAdapter().getItem(position);
        intent.setData(Uri.parse("https://www.youtube.com/watch?v=" + clickedTrailer.getKey()));
        Log.d("MovieActivty-", "https://www.youtube.com/watch?v=" + clickedTrailer.getKey());
        startActivity(intent);

    }
    public class reviewAndTrailerSync extends AsyncTask<Integer, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(getActivity());
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            dialog.setTitle("Loading...");
            dialog.show();

        }

        @Override
        protected Void doInBackground(Integer... params) {

            try {
                // Add Trailer
                JSONArray trailerArray = new JSONObject(getURlString("Trailer", params[0]))
                        .getJSONArray("results");
                trailerList = new ArrayList<>();
                if (trailerArray.length() > 0) {
                    int x;
                    if (trailerArray.length()<3)
                    {
                        x=trailerArray.length();
                    }
                    else
                    {
                        x=3;
                    }
                    for (int n = 0; n < x; n++) {
                        JSONObject trailerObject = trailerArray.getJSONObject(n);
                        Trailer trailer = new Trailer();
                        trailer.setId(trailerObject.getString("id"));
                        trailer.setKey(trailerObject.getString("key"));
                        trailer.setName(trailerObject.getString("name"));
                        trailer.setSite(trailerObject.getString("site"));
                        trailer.setType(trailerObject.getString("type"));
                        trailerList.add(trailer);

                    }

                }

                // Add Review
                reviewList = new ArrayList<>();
                JSONArray reviewArray = new JSONObject(getURlString("Review", params[0]))
                        .getJSONArray("results");
                if (reviewArray.length() > 0) {
                    int n;
                    if (reviewArray.length()<3)
                    {
                        n=reviewArray.length();
                    }
                    else
                    {
                        n=1;
                    }
                    for (int x = 0; x < n; x++) {
                        JSONObject reviewObject = reviewArray.getJSONObject(x);
                        Review review = new Review();
                        review.setId(reviewObject.getString("id"));
                        review.setAuthor(reviewObject.getString("author"));
                        review.setContent(reviewObject.getString("content"));
                        review.setUrl(reviewObject.getString("url"));
                        reviewList.add(review);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            setAdapters();
            dialog.cancel();

        }
    }

    private void setAdapters() {
        if (trailerList.size() > 0) {

            ListView listView = (ListView) getActivity().findViewById(R.id.detail_trailer_list);
            ArrayAdapter<Trailer> adapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1, trailerList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(DetailFragment.this);
            Helper.getListViewSize(listView);

        }

        if (reviewList.size() > 0) {

          /*  ListView reviewListView = (ListView) findViewById(R.id.detail_review_list);
            ArrayAdapter<Review> reviewArrayAdapter = new ArrayAdapter<>(DetailActivity.this,
                    android.R.layout.simple_list_item_1, reviewList);
            reviewListView.setAdapter(reviewArrayAdapter);
            Helper.getListViewSize(reviewListView);*/
            TextView reviewtextview= (TextView) getActivity().findViewById(R.id.detail_review_list);
            Review r=reviewList.get(0);
            String s=r.getContent();
            reviewtextview.setText(s);



        }
    }

    /**
     * @param type
     * @param id
     * @return
     */
    private String getURlString(String type, int id) {
        String result;
        String Webaddress;
        if (type.equals("Review")) {
            Webaddress = "http://api.themoviedb.org/3/movie/" + id + "/reviews?sort_by=popularity.desc&api_key="
                    + getString(R.string.apikey);
        } else {
            Webaddress = "http://api.themoviedb.org/3/movie/" + id + "/videos?sort_by=popularity.desc&api_key="
                    + getString(R.string.apikey);
        }

        try {
            URL url = new URL(Webaddress);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();
            result = IOUtils.toString(is);
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            result = "";
        } catch (IOException e) {
            e.printStackTrace();
            result = "";
        }


        return result;
    }

}
