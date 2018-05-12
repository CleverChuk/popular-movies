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

import com.cleverchuk.popularmovies.sqlite.FavoriteMovieContract;

/**
 * A simple class to hold constants shared by the activities
 * Created by chuk on 4/26/18.
 */

public final class Constants {
    public static final String MOVIE_DATA_SHARE = "movie-data";
    public static final String[] FAV_PROJECTION;
    public static final int INDEX_ID, INDEX_TITLE, INDEX_RELEASE_DATE,INDEX_POSTER_URI;
    public static final int INDEX_VOTE_AVERAGE, INDEX_PLOT_SYNOPSIS, INDEX_POSTER_BLOB;


    static {


        FAV_PROJECTION = new String[]{
                FavoriteMovieContract.FavoriteMovieEntry.ID_COLUMN,
                FavoriteMovieContract.FavoriteMovieEntry.TITLE_COLUMN,
                FavoriteMovieContract.FavoriteMovieEntry.RELEASE_DATE_COLUMN,
                FavoriteMovieContract.FavoriteMovieEntry.VOTE_AVERAGE,
                FavoriteMovieContract.FavoriteMovieEntry.PLOT_SYNOPSIS,
                FavoriteMovieContract.FavoriteMovieEntry.POSTER_BLOB_COLUMN,
                FavoriteMovieContract.FavoriteMovieEntry.POSTER_URI_COLUMN
        };

        INDEX_ID = 0;
        INDEX_TITLE = 1;
        INDEX_RELEASE_DATE = 2;

        INDEX_VOTE_AVERAGE = 3;
        INDEX_PLOT_SYNOPSIS = 4;
        INDEX_POSTER_BLOB = 5;
        INDEX_POSTER_URI = 6;
    }
}
