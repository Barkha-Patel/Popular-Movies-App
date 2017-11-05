package com.example.android.popular_movies;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.Toast;

/**
 * Created by Barkha on 25-Apr-16.
 */
public class MovieCursorLoader extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private GridView gridView;
    private int mposition = GridView.INVALID_POSITION;
    MovieCursorAdapter cursorAdapter= new MovieCursorAdapter(getActivity(), null, 0);
    @Override
    public final void onSaveInstanceState(Bundle outState) {

        if (mposition != GridView.INVALID_POSITION) {
            outState.putInt("Selectedkey", mposition);

        }

        super.onSaveInstanceState(outState);
    }






    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);


        cursorAdapter = new MovieCursorAdapter(getActivity(), null, 0);
        gridView = (GridView) getActivity().findViewById(R.id.MovieGrid);

        gridView.setAdapter(cursorAdapter);
        if (savedInstanceState != null && savedInstanceState.containsKey("Selectedkey")) {
            mposition = savedInstanceState.getInt("Selectedkey");

        }
        if (mposition != GridView.INVALID_POSITION) {
            gridView.setSelection(mposition);
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getAdapter().getItem(position);
                //Movies movie = (Movies) parent.getAdapter().getItem(position);
                if (getActivity().findViewById(R.id.fragmentDetailContainer) != null) {


                    Bundle arguments = new Bundle();
                    arguments.putInt("MovieID", cursor.getInt(cursor.getColumnIndex(MovieContract.FavEntry.MOVIE_ID)));
                   // cursor.close();
                    DetailCursorFragment fragment = new DetailCursorFragment();
                    fragment.setArguments(arguments);
                    getFragmentManager().beginTransaction().replace(R.id.fragmentDetailContainer, fragment).commit();

                }
                else{


                    Intent intent = new Intent(getActivity(), DetailCursor.class);
                    intent.putExtra("Movie", cursor.getInt(cursor.getColumnIndex(MovieContract.FavEntry.MOVIE_ID)));
                  //  cursor.close();
                 //   Log.d("cursorLoader", "passed " + cursor.getInt(cursor.getColumnIndex(MovieContract.FavEntry.MOVIE_ID)));

                    startActivity(intent);}
               /* int duration = Toast.LENGTH_SHORT;
                Context context = getActivity().getApplicationContext();
                Toast toast = Toast.makeText(context, "clicked", duration);
                toast.show();*/
                mposition=position;

            }
        });


        getLoaderManager().initLoader(0, null, MovieCursorLoader.this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        return new CursorLoader(getActivity(), MovieContract.CONTENT_URI,
                null, null, null, null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Log.d(TAG, "This is the data from the cursor " + data.getColumnNames().toString());
        //Log.d(TAG, "This is the number of items in the collection " + data.getCount());
        cursorAdapter.swapCursor(data);

    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }
}
