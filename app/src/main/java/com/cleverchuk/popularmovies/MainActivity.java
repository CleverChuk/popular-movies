/*
 *
 * MIT License
 *
 * Copyright (c) 2018 Chukwubuikem Ume-Ugwa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.cleverchuk.popularmovies;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeImageTransform;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cleverchuk.popularmovies.adapters.MovieAdapter;
import com.cleverchuk.popularmovies.adapters.PopMovieBaseAdapter;
import com.cleverchuk.popularmovies.models.Movie;
import com.cleverchuk.popularmovies.sqlite.FavoriteMovieContract;
import com.cleverchuk.popularmovies.utils.ApiRequest;
import com.cleverchuk.popularmovies.utils.Constants;
import com.cleverchuk.popularmovies.utils.MyGridLayoutManager;
import com.cleverchuk.popularmovies.utils.Query;
import com.cleverchuk.popularmovies.utils.Queue;

import java.util.ArrayList;

import static com.cleverchuk.popularmovies.utils.Constants.FAV_PROJECTION;
import static com.cleverchuk.popularmovies.utils.Constants.INDEX_ID;
import static com.cleverchuk.popularmovies.utils.Constants.INDEX_PLOT_SYNOPSIS;
import static com.cleverchuk.popularmovies.utils.Constants.INDEX_POSTER_URI;
import static com.cleverchuk.popularmovies.utils.Constants.INDEX_RELEASE_DATE;
import static com.cleverchuk.popularmovies.utils.Constants.INDEX_TITLE;
import static com.cleverchuk.popularmovies.utils.Constants.INDEX_VOTE_AVERAGE;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        MovieAdapter.OnMovieItemClickListener {

    private static final String TAG;
    private static final int LOADER_ID;
    private Queue mQueue;

    private PopMovieBaseAdapter<?, Movie> mAdapter;
    private ApiRequest<Movie> mApiRequest;
    private RecyclerView mRecyclerView;

    static {
        TAG = "request.TAG";
        LOADER_ID = 200;
    }

    /**
     * error listener for handling api request error
     */
    private Response.ErrorListener mErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            String msg = getString(R.string.rep_err_msg) + error.getMessage();
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQueue = Queue.getInstance(this);
        mAdapter = new MovieAdapter(this, this);

        mRecyclerView = findViewById(R.id.movieGrid);
        mRecyclerView.setAdapter(mAdapter);
        GridLayoutManager layoutManager = new MyGridLayoutManager(this, 400);
        mRecyclerView.setLayoutManager(layoutManager);

        if (savedInstanceState == null) {
            requestPopularMovies();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setSharedElementExitTransition(new ChangeImageTransform());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mAdapter.restoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mAdapter.saveInstanceState(outState);
    }

    /**
     * makes request for popular movies
     */
    private void requestPopularMovies() {
        mApiRequest = new ApiRequest<>(Query.getPopularEndpoint(this), new Response.Listener<Movie>() {
            @Override
            public void onResponse(Movie response) {
                /* display the right data base on user action */
                mAdapter.addData(response.getPayload());

            }
        }, Movie.class, mErrorListener);
        mQueue.queue(mApiRequest, TAG);
    }

    /**
     * makes request for top rated movies
     */
    private void requestTopRatedMovies() {
        mApiRequest = new ApiRequest<>(Query.getTopRatedEndpoint(this), new Response.Listener<Movie>() {
            @Override
            public void onResponse(Movie response) {
                /* display the right data base on user action */

                mAdapter.addData(response.getPayload());

            }
        }, Movie.class, mErrorListener);
        mQueue.queue(mApiRequest, TAG);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.popular:
                requestPopularMovies();
                return true;
            case R.id.topRated:
                requestTopRatedMovies();
                return true;
            case R.id.favorites:
                getLoaderManager().restartLoader(LOADER_ID, null, this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing())
            mQueue.cancelAll();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) mAdapter.cleanUp();
        mErrorListener = null;
    }


    /**
     * handle callback from the adapter
     *
     * @param position item position
     */
    @Override
    public void itemClicked(int position, View view) {
        Movie movie = mAdapter.getItem(position);
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(Constants.MOVIE_DATA_SHARE, movie);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this, view, getString(R.string.movie_poster));
            startActivity(intent, optionsCompat.toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        /* query the DB for favorite movies */
        String sortOrder = FavoriteMovieContract.FavoriteMovieEntry.ID_COLUMN + " ASC";
        Uri uri = FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI;

        return new CursorLoader(this, uri, FAV_PROJECTION,
                null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
       /* check if user has any favorites then display favorites */
        int count = cursor.getCount();
        if (count == 0) {
            String msg = getString(R.string.no_fav_msg);
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            return;
        }

        /**
         * FIXME: recycler view keeps growing with empty rows
         * if you keep tapping favorites. Any suggestion to
         * fix?
         */
        ArrayList<Movie> movies = new ArrayList<>(count);
        cursor.moveToFirst();
        for (int i = 0; i < count; i++, cursor.moveToNext()) {
            movies.add(getMovieFromCursor(cursor));
        }
        mAdapter.addData(movies);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private Movie getMovieFromCursor(Cursor cursor) {
        /* read id from the cursor */
        String id = cursor.getString(INDEX_ID);
        /* read title from the cursor */
        String title = cursor.getString(INDEX_TITLE);
        /* read release date from cursor */
        String release_date = cursor.getString(INDEX_RELEASE_DATE);
        /* read average vote from cursor */
        String vote_average = cursor.getString(INDEX_VOTE_AVERAGE);
        /* read the plot synopsis from the cursor */
        String plot_synopsis = cursor.getString(INDEX_PLOT_SYNOPSIS);
        /* read the poster uri from the cursor */
        String posterUri = cursor.getString(INDEX_POSTER_URI);

        return new Movie(title, id, plot_synopsis, release_date, posterUri, vote_average);
    }
}
