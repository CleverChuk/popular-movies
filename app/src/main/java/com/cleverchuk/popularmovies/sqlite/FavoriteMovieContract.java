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

package com.cleverchuk.popularmovies.sqlite;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * this the SQLite database contract that defines the database
 * this class is a Singleton. it has inner class that defines
 * the table columns
 * Created by chuk on 4/26/18.
 */

public final class FavoriteMovieContract {
    public static final String AUTHORITY = "com.cleverchuk.popularmovies";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);
    public static final String PATH_FAVORITE_MOVIES = "favorite_movies";

    private FavoriteMovieContract(){}


    public static class FavoriteMovieEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES).build();

        public static final String  TABLE_NAME = "favorite_movies";
        public static final String TITLE_COLUMN = "movie_title";
        public static final String ID_COLUMN ="movie_id";
        public static final String POSTER_BLOB_COLUMN = "poster";
        public static final String RELEASE_DATE_COLUMN = "release_date";
        public static final String PLOT_SYNOPSIS = "plot_synopsis";
        public static final String VOTE_AVERAGE = "vote_average";
        public static final String POSTER_URI_COLUMN = "posterUri";
    }

}
