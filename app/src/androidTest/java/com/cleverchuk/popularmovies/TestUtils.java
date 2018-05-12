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

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.cleverchuk.popularmovies.sqlite.FavoriteMovieContract;

import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

/**
 * A simple {@className()}
 * Created by chuk on 4/26/18.
 */
class TestUtils {
    private static final String AUTHORITY = "com.cleverchuk.popularmovies";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    private static final String PATH_FAVORITE_MOVIES = "favorite-movies";
    static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES).build();

    static final String TABLE_NAME = "favorite-movies";
    static final String TITLE_COLUMN = "movie_title";
    static final String ID_COLUMN = "movie_id";


    static ContentValues[] createBulkTestFavoriteMovieValues() {
        ContentValues values[] = new ContentValues[5];
        String[] movies = {
                "12 Years a Slave",
                "13 Going on 30",
                "13 Hours: The Secret Soldiers of Benghazi",
                "13th Warrior",
                "The 14 Carrot Rabbit"};
        for (int i = 0; i < 5; i++) {
            ContentValues movieValue = new ContentValues();
            movieValue.put(FavoriteMovieContract.FavoriteMovieEntry.TITLE_COLUMN, movies[i]);
            movieValue.put(FavoriteMovieContract.FavoriteMovieEntry.ID_COLUMN, i);

            values[i] = movieValue;
        }
        return values;
    }


    public static void validateRecords(String error, Cursor cursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();

        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int index = cursor.getColumnIndex(columnName);

            // test if column exists in the cursor
            String columnNotFoundError = "Column: " + columnName + " not found. " + error;
            assertFalse(columnNotFoundError, index == -1);

            // test if values match
            String expectValue = entry.getValue().toString();
            String actualValue = cursor.getString(index);
            String valuesDontMatchError = "Actual value: " + actualValue + " did not match expected value " +
                    expectValue + ". " + error;
            assertEquals(valuesDontMatchError, expectValue, actualValue);
        }

    }
}
