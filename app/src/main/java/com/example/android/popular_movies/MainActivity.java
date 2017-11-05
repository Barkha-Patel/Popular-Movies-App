package com.example.android.popular_movies;


import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    GridView Grid;
    ArrayList<Movies> popularlist;
    ArrayList<Movies> topratedlist;
    FragmentManager fragmentManager;
    int mposition;



    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putParcelableArrayList("poplist", popularlist);
        outState.putParcelableArrayList("toplist", topratedlist);

        if (mposition != GridView.INVALID_POSITION) {
            outState.putInt("Selectedkey", mposition);

        }

        super.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey("Selectedkey")) {
            mposition = savedInstanceState.getInt("Selectedkey");
        }
        // Loading Saved data
        popularlist = savedInstanceState.getParcelableArrayList("poplist");
        topratedlist = savedInstanceState.getParcelableArrayList("toplist");


    }

    protected void onResume() {
        super.onResume();
        Grid = (GridView) findViewById(R.id.MovieGrid);

        if (popularlist == null || topratedlist == null) { // Checking to see if data is present before loading
            if (NetworkCheck.isOnline(MainActivity.this)) {
                new MainSync().execute();
            } else {
                Context context = getApplicationContext();
                CharSequence text = getString(R.string.noconn);
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        } else {
            loadList();
        }


        Grid.setOnItemClickListener(MainActivity.this);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null && savedInstanceState.containsKey("Selectedkey")) {
            mposition = savedInstanceState.getInt("Selectedkey");
        }


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Movies movie = (Movies) parent.getAdapter().getItem(position);
        if (findViewById(R.id.fragmentDetailContainer) != null) {

            Bundle args = new Bundle();
            args.putParcelable("Movie", movie);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getFragmentManager().beginTransaction().replace(R.id.fragmentDetailContainer, fragment)
                    .commit();
        } else {

            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("Movie", movie);
            startActivity(intent);
        }
        mposition = position;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public class MainSync extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;


        @Override
        protected void onPreExecute() {


            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setTitle(getString(R.string.load));
            dialog.show();


        }

        @Override
        protected Void doInBackground(Void... params) {


            String linkpopular;
            String linktoprated;


            linkpopular = getString(R.string.popurl)
                    + getString(R.string.apikey);

            linktoprated = getString(R.string.topurl)
                    + getString(R.string.apikey);

            popularlist = new ArrayList<>();
            topratedlist = new ArrayList<>();

            URLResult(linkpopular, popularlist);
            URLResult(linktoprated, topratedlist);


            return null;
        }


        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            loadList();
            if (findViewById(R.id.fragmentDetailContainer) != null) {
                Movies moviestart;
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                if (sharedPreferences.getString("pref_list", "popular").equals("popular")) {
                    moviestart = popularlist.get(0);
                    Bundle args = new Bundle();
                    args.putParcelable("Movie", moviestart);

                    DetailFragment fragment = new DetailFragment();
                    fragment.setArguments(args);

                    getFragmentManager().beginTransaction().replace(R.id.fragmentDetailContainer, fragment)
                            .commit();
                } else if (sharedPreferences.getString("pref_list", "popular").equals("rated")) {
                    moviestart = topratedlist.get(0);
                    Bundle args = new Bundle();
                    args.putParcelable("Movie", moviestart);

                    DetailFragment fragment = new DetailFragment();
                    fragment.setArguments(args);

                    getFragmentManager().beginTransaction().replace(R.id.fragmentDetailContainer, fragment)
                            .commit();

                } else {
                    Cursor mCursor;
                    Uri uri = MovieContract.CONTENT_URI;
                    mCursor = getContentResolver().query(uri, MovieContract.FavEntry.ALL_COLUMNS,
                            null, null, null, null);
                    mCursor.moveToFirst();
                    Bundle arguments = new Bundle();
                    arguments.putInt("MovieID", mCursor.getInt(mCursor.getColumnIndex(MovieContract.FavEntry.MOVIE_ID)));
                    mCursor.close();

                    DetailCursorFragment fragment = new DetailCursorFragment();
                    fragment.setArguments(arguments);
                    getFragmentManager().beginTransaction().replace(R.id.fragmentDetailContainer, fragment).commit();

                }


            }

            dialog.cancel();

        }
    }

    private void loadList() {
        // Checking Shared Preference for data
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        if (sharedPreferences.getString("pref_list", "popular").equals("popular")) {
            loadMovieAdapter(popularlist);
        } else if (sharedPreferences.getString("pref_list", "popular").equals("rated")) {
            loadMovieAdapter(topratedlist);

        } else {
            fragmentManager = getFragmentManager();
            MovieCursorLoader movieDBLIstFragment = new MovieCursorLoader();
            fragmentManager.beginTransaction().replace(R.id.fragmentGridListContainer, movieDBLIstFragment).commit();
        }
    }

    private void loadMovieAdapter(ArrayList<Movies> list) {
        ImageAdapter adapter = new ImageAdapter(MainActivity.this,
                list);
        Grid.setAdapter(adapter);
        if (mposition != GridView.INVALID_POSITION) {
            Grid.setSelection(mposition);
        }
    }

    private void URLResult(String linkpopular, ArrayList<Movies> list) {
        try {

            URL url = new URL(linkpopular);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            String results = IOUtils.toString(inputStream);
            jsonParser(results, list);
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();


        }
    }

    private void jsonParser(String s, ArrayList<Movies> movielist) {
        try {
            JSONObject mObject = new JSONObject(s);

            JSONArray resultsArray = mObject.getJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject item = resultsArray.getJSONObject(i);
                Movies movieitem = new Movies();

                movieitem.setId(item.getInt("id"));
                movieitem.setOriginal_title(item.getString("original_title"));
                movieitem.setOverview(item.getString("overview"));
                movieitem.setRelease_date(item.getString("release_date"));
                movieitem.setPoster_path(item.getString("poster_path"));
                movieitem.setTitle(item.getString("title"));
                movieitem.setVote_average(item.getString("vote_average"));


                movielist.add(movieitem); // Add each item to the list
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }


    }








