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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * a simple class that extends {@link SQLiteOpenHelper} for managing db
 * Created by chuk on 4/26/18.
 */

public class FavoriteMovieDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "favoriteMovie.db";

    /* create table statement */
    private static final String SQL_CREATE_FAVORITE_MOVIE_TABLE = "CREATE TABLE " +
            FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME + " (" +
            FavoriteMovieContract.FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY," +
            FavoriteMovieContract.FavoriteMovieEntry.ID_COLUMN + " TEXT," +
            FavoriteMovieContract.FavoriteMovieEntry.TITLE_COLUMN + " TEXT," +
            FavoriteMovieContract.FavoriteMovieEntry.RELEASE_DATE_COLUMN + " TEXT," +
            FavoriteMovieContract.FavoriteMovieEntry.VOTE_AVERAGE + " TEXT," +
            FavoriteMovieContract.FavoriteMovieEntry.PLOT_SYNOPSIS + " TEXT," +
            FavoriteMovieContract.FavoriteMovieEntry.POSTER_BLOB_COLUMN + " BLOB," +
            FavoriteMovieContract.FavoriteMovieEntry.POSTER_URI_COLUMN + " TEXT"+
            ")";

    /* delete the table statement */
    private static final String DELETE_TABLE = "DROP TABLE IF EXISTS " +
            FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME;

    public FavoriteMovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_FAVORITE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // will use alter statement to upgrade db in production application
        db.execSQL(DELETE_TABLE);
        onCreate(db);
    }
}
