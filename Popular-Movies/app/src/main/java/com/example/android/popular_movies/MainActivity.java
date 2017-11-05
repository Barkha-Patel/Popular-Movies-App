package com.example.android.popular_movies;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

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
    ArrayList<Movie> popularlist;
    ArrayList<Movie> topratedlist;
    
      protected void onStart() {
        super.onStart();

        if (popularlist == null || topratedlist == null){ // Checking to see if data is present before loading
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
        

        Grid = (GridView) findViewById(R.id.MovieGrid);



    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Movie movie = (Movie) parent.getAdapter().getItem(position);

        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("Movie", movie);
        startActivity(intent);
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
            Intent intent=new Intent(this,SettingsActivity.class);
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

            linktoprated =getString(R.string.topurl)
            + getString(R.string.apikey);

            popularlist = new ArrayList<>();
            topratedlist = new ArrayList<>();

            URLResult(linkpopular, popularlist);
            URLResult(linktoprated, topratedlist);


            return null;
        }


        @Override
        protected void onPostExecute(Void  s) {
            super.onPostExecute(s);
            loadList();
            dialog.cancel();

        }
    }

    private void loadList() {
        // Checking Shared Preference for data
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        if (sharedPreferences.getString("pref_list", "popular").equals("popular")) {
        loadMovieAdapter(popularlist);
        } else {
           loadMovieAdapter(topratedlist);

        }
    }

    private void loadMovieAdapter(ArrayList<Movie> list) {
        ImageAdapter adapter = new ImageAdapter(MainActivity.this,
                list);
        Grid.setAdapter(adapter);
    }

    private void URLResult(String linkpopular, ArrayList<Movie> list) {
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

    private void jsonParser(String s, ArrayList<Movie> movielist) {
        try {
            JSONObject mObject = new JSONObject(s);

            JSONArray resultsArray = mObject.getJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject item = resultsArray.getJSONObject(i);
                Movie movieitem = new Movie();

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
