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

package com.cleverchuk.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cleverchuk.popularmovies.R;
import com.cleverchuk.popularmovies.models.Movie;
import com.cleverchuk.popularmovies.utils.Query;
import com.squareup.picasso.Picasso;

/**
 * A simple adapter that extends from {@link BaseAdapter}
 * Created by chuk on 4/24/18.
 */

public class MovieAdapter extends PopMovieBaseAdapter<MovieAdapter.MovieHolder,Movie> {
    private static final String DATA_KEY;
    private final Context mContext;

    static {
        DATA_KEY = "com.cleverchuk.popularmovies.movieadapter";
    }

    public MovieAdapter(Context context, OnMovieItemClickListener listener) {
        super(DATA_KEY);
        mContext = context;
        mListener = listener;
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_movie_poster, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
        Movie data = mData.get(position);
        String url = Query.getPosterEndpoint(mContext, data.getPoster());
        holder.populate(url);
    }


    /**
     * a simple view holder that extends {@link PopMovieBaseAdapter.BaseHolder}
     */
    class MovieHolder extends PopMovieBaseAdapter.BaseHolder {
        ImageView imageView;
        MovieHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.poster);
            itemView.setOnClickListener(this);
        }

        void populate(String url) {
            Picasso.get().load(url).into(imageView);
        }

        @Override
        public void onClick(View v) {
            int i = getAdapterPosition();
            mListener.itemClicked(i,v);
        }
    }

}