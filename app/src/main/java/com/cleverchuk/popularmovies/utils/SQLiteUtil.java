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

package com.cleverchuk.popularmovies.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.cleverchuk.popularmovies.models.Movie;
import com.cleverchuk.popularmovies.sqlite.FavoriteMovieContract;

/**
 * a simple utility class to insert and remove favorite movie
 * Created by chuk on 4/28/18.
 */

public class SQLiteUtil {
    /**
     * insert movie in the db
     * @param context calling context
     * @param movie movie to insert
     * @param blob movie image
     */
    public static void addFav(Context context, Movie movie, byte[] blob) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues(6);

        values.put(FavoriteMovieContract.FavoriteMovieEntry.ID_COLUMN, movie.getId());
        values.put(FavoriteMovieContract.FavoriteMovieEntry.TITLE_COLUMN, movie.getTitle());
        values.put(FavoriteMovieContract.FavoriteMovieEntry.PLOT_SYNOPSIS, movie.getPlotSynopsis());

        values.put(FavoriteMovieContract.FavoriteMovieEntry.RELEASE_DATE_COLUMN, movie.getReleaseDate());
        values.put(FavoriteMovieContract.FavoriteMovieEntry.VOTE_AVERAGE, movie.getVoteAverage());
        values.put(FavoriteMovieContract.FavoriteMovieEntry.POSTER_BLOB_COLUMN, blob);

        values.put(FavoriteMovieContract.FavoriteMovieEntry.POSTER_URI_COLUMN, movie.getPoster());
        resolver.insert(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI, values);
    }

    /**
     * delete movie from db
     * @param context calling context
     * @param movie movie to remove
     */
    public static void removeFav(Context context, Movie movie) {
        ContentResolver resolver = context.getContentResolver();
        resolver.delete(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI.buildUpon().
                appendPath(movie.getId()).build(), null, null);
    }
}
