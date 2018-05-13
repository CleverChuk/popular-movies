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
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cleverchuk.popularmovies.adapters.PopMovieBaseAdapter;
import com.cleverchuk.popularmovies.adapters.ReviewAdapter;
import com.cleverchuk.popularmovies.adapters.VideoAdapter;
import com.cleverchuk.popularmovies.databinding.ActivityDetailBinding;
import com.cleverchuk.popularmovies.models.Movie;
import com.cleverchuk.popularmovies.models.Review;
import com.cleverchuk.popularmovies.models.Video;
import com.cleverchuk.popularmovies.sqlite.FavoriteMovieContract;
import com.cleverchuk.popularmovies.utils.ApiRequest;
import com.cleverchuk.popularmovies.utils.Constants;
import com.cleverchuk.popularmovies.utils.Query;
import com.cleverchuk.popularmovies.utils.Queue;
import com.cleverchuk.popularmovies.utils.SQLiteUtil;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        PopMovieBaseAdapter.OnMovieItemClickListener {
    private static final int NUM_ROWS;
    private static final int LOADER_ID;
    private static final String REVIEWS_REQ_TAG, MOVIE_ID;
    private static final String VIDEOS_REQ_TAG;
    private static final String YOUTUBE = "https://www.youtube.com/watch?v=";
    private Response.ErrorListener mErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            String msg = getString(R.string.rep_err_msg) + error.getMessage();
            Toast.makeText(DetailActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    };

    private VideoAdapter mVideoAdapter;
    private ReviewAdapter mReviewAdapter;
    private Queue mQueue;
    private ActivityDetailBinding mBinding;

    static {
        MOVIE_ID = "id";
        NUM_ROWS = 1;
        LOADER_ID = 0;
        REVIEWS_REQ_TAG = "reviews";
        VIDEOS_REQ_TAG = "videos";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            getWindow().setAllowEnterTransitionOverlap(true);
        }
        mQueue = Queue.getInstance(this);

        Movie movie = getIntent().getParcelableExtra(Constants.MOVIE_DATA_SHARE);
        getSupportActionBar().setTitle(movie.getTitle());
        mBinding.setMovie(movie);

        /*
          * start loader to check if the selected movie is in the favorite
          * database
          */
        Bundle args = new Bundle(1);
        args.putString(MOVIE_ID, movie.getId());
        getLoaderManager().initLoader(LOADER_ID, args, this);

        /*
            set up the review adapter for review data
         */
        mReviewAdapter = new ReviewAdapter(this);
        mBinding.reviewRcv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mBinding.reviewRcv.setAdapter(mReviewAdapter);


        /*
            set up the video adapter for video data
         */
        mVideoAdapter = new VideoAdapter(this, this);
        mBinding.videoRcv.setHasFixedSize(true);
        mBinding.videoRcv.setLayoutManager(new StaggeredGridLayoutManager(NUM_ROWS,
                StaggeredGridLayoutManager.HORIZONTAL));
        mBinding.videoRcv.setAdapter(mVideoAdapter);

        String posterUri = Query.getPosterEndpoint(this, movie.getPoster());
        Picasso.get().load(posterUri).into(mBinding.backdrop);
        Picasso.get().load(posterUri).into(mBinding.poster);

        if (savedInstanceState == null) {
            requestReviews();
            requestVideos();
        }

        // add movie to favorite db
        mBinding.favoriteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable = (BitmapDrawable) mBinding.backdrop.getDrawable();

                Bitmap bitmap = drawable.getBitmap();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);

                SQLiteUtil.addFav(DetailActivity.this, mBinding.getMovie(), out.toByteArray());
                mBinding.favoriteFab.hide();
                mBinding.unfavoriteFab.show();

                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // remove movie from favorite db
        mBinding.unfavoriteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteUtil.removeFav(DetailActivity.this, mBinding.getMovie());
                mBinding.unfavoriteFab.hide();
                mBinding.favoriteFab.show();
            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mReviewAdapter.saveInstanceState(outState);
        mVideoAdapter.saveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVideoAdapter.restoreInstanceState(savedInstanceState);
        mReviewAdapter.restoreInstanceState(savedInstanceState);
    }

    /**
     * this makes request for the movie reviews
     */
    private void requestReviews() {
        String id = mBinding.getMovie().getId();
        String url = Query.getReviewPath(this, id);
        ApiRequest<Review> request = new ApiRequest<>(url, new Response.Listener<Review>() {
            @Override
            public void onResponse(Review response) {
                mReviewAdapter.addData(response.getPayload());
            }
        }, Review.class, mErrorListener);

        mQueue.queue(request, REVIEWS_REQ_TAG);
    }

    /**
     * this makes request for the movie videos
     */
    private void requestVideos() {

        String id = mBinding.getMovie().getId();
        String url = Query.getTrailerPath(this, id);
        ApiRequest<Video> videoApiRequest = new ApiRequest<>(url, new Response.Listener<Video>() {
            @Override
            public void onResponse(Video response) {
                mVideoAdapter.addData(response.getPayload());
            }
        }, Video.class, mErrorListener);
        mQueue.queue(videoApiRequest, VIDEOS_REQ_TAG);
    }


    /**
     * handle callback from the adapter
     *
     * @param position item position
     */
    @Override
    public void itemClicked(int position, View view) {
        Video video = mVideoAdapter.getItem(position);
        String url = YOUTUBE + video.getKey();
        Uri uri = Uri.parse(url);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "You need to install youtube app or a web browser", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoAdapter != null) mVideoAdapter.cleanUp();
        mErrorListener = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing())
            mQueue.cancelAll();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String movie_id = args.getString(MOVIE_ID);
        String sortOrder = FavoriteMovieContract.FavoriteMovieEntry.ID_COLUMN + " ASC";
        Uri uri = FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI.buildUpon().
                appendPath(movie_id).build();

        return new CursorLoader(this, uri, null,
                null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() == 0) {
            mBinding.unfavoriteFab.hide();
            mBinding.favoriteFab.show();
        } else {
            mBinding.unfavoriteFab.show();
            mBinding.favoriteFab.hide();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
