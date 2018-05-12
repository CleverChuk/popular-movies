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

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * a subclass of {@link ContentProvider}
 * Created by chuk on 4/26/18.
 */

public class FavoriteMovieContentProvider extends ContentProvider {
    private FavoriteMovieDbHelper mFavoriteMovieDbHelper;

    private static final int FAVORITE_MOVIE = 500;
    private static final int FAVORITE_MOVIE_WITH_ID = 501;
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavoriteMovieContract.AUTHORITY, FavoriteMovieContract.PATH_FAVORITE_MOVIES,
                FAVORITE_MOVIE);
        uriMatcher.addURI(FavoriteMovieContract.AUTHORITY, FavoriteMovieContract.PATH_FAVORITE_MOVIES + "/#",
                FAVORITE_MOVIE_WITH_ID);
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        mFavoriteMovieDbHelper = new FavoriteMovieDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mFavoriteMovieDbHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case FAVORITE_MOVIE:
                cursor = db.query(FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FAVORITE_MOVIE_WITH_ID:
                String id = uri.getLastPathSegment();

                selectionArgs = new String[]{id};
                cursor = db.query(FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        FavoriteMovieContract.FavoriteMovieEntry.ID_COLUMN + " = ? ",
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("Not implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mFavoriteMovieDbHelper.getWritableDatabase();

        Uri outUri;
        switch (uriMatcher.match(uri)) {
            case FAVORITE_MOVIE:
                long id = db.insert(FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME,
                        null, values);
                if (id < 0)throw new SQLException("Failed to insert row into " + uri);

                outUri = ContentUris.withAppendedId(FavoriteMovieContract.FavoriteMovieEntry.
                        CONTENT_URI, id);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(outUri, null);
        }

        return outUri;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mFavoriteMovieDbHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case FAVORITE_MOVIE:
                db.beginTransaction();
                int rowsInserted = 0;

                try {
                    for (ContentValues value : values) {
                        long id = db.insert(FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME,
                                null, value);
                        if (id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();

                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0 && getContext() != null) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mFavoriteMovieDbHelper.getWritableDatabase();

        int rowsDeleted = 0;

        switch (uriMatcher.match(uri)) {
            case FAVORITE_MOVIE:
                rowsDeleted = db.delete(FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case FAVORITE_MOVIE_WITH_ID:
                String id = uri.getLastPathSegment();
                selectionArgs = new String[]{id};
                rowsDeleted = db.delete(FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME,
                        FavoriteMovieContract.FavoriteMovieEntry.ID_COLUMN + " = ? ",
                        selectionArgs);
                break;
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mFavoriteMovieDbHelper.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriMatcher.match(uri)) {
            case FAVORITE_MOVIE:
                rowsUpdated = db.update(FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case FAVORITE_MOVIE_WITH_ID:
                String id = uri.getLastPathSegment();
                selectionArgs = new String[]{id};
                rowsUpdated = db.update(FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME,
                        values,
                        FavoriteMovieContract.FavoriteMovieEntry.ID_COLUMN + " = ? ",
                        selectionArgs);
                break;
        }
        return rowsUpdated;
    }
}
