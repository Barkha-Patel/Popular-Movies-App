package com.example.android.popular_movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;



public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Movie movieIntent =  (Movie) intent.getSerializableExtra("Movie");

        if (movieIntent != null) {

            TextView titleText = (TextView) findViewById(R.id.title);
            titleText.setText(movieIntent.getTitle());


            ImageView detailImage = (ImageView) findViewById(R.id.poster);
            Picasso.with(this).load("https://image.tmdb.org/t/p/w500" + movieIntent.getPoster_path())
                    .into(detailImage);


            TextView releaseDateText = (TextView) findViewById(R.id.year);
            releaseDateText.setText(movieIntent.getRelease_date());



            TextView overviewText = (TextView) findViewById(R.id.overview);
            overviewText.setText(movieIntent.getOverview());


            TextView voteAverageText = (TextView) findViewById(R.id.voteaverage);
            voteAverageText.setText(movieIntent.getVote_average());




        } else {
            Toast.makeText(this, "ERROR No data was read",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return (super.onOptionsItemSelected(item));
    }




}
