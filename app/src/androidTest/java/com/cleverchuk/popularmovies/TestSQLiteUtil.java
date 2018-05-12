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

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cleverchuk.popularmovies.models.Movie;
import com.cleverchuk.popularmovies.sqlite.FavoriteMovieContract;
import com.cleverchuk.popularmovies.sqlite.FavoriteMovieDbHelper;
import com.cleverchuk.popularmovies.utils.SQLiteUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * A simple {@className()}
 * Created by chuk on 4/28/18.
 */

@RunWith(AndroidJUnit4.class)
public class TestSQLiteUtil {
    private final Context mContext = InstrumentationRegistry.getTargetContext();
    private final ContentResolver resolver = mContext.getContentResolver();

    @Before
    @After
    public void cleanUp(){
        deleteAllRecords();
    }
    @Test
    public void testAddFav(){
        Movie movie = new Movie("Chuk Reincarnate","2018","A story about programming",
                "04-19-2018","","10");
        SQLiteUtil.addFav(mContext,movie,null);

        ContentValues value = new ContentValues(2);
        value.put(FavoriteMovieContract.FavoriteMovieEntry.ID_COLUMN,movie.getId());
        value.put(FavoriteMovieContract.FavoriteMovieEntry.TITLE_COLUMN,movie.getTitle());

        Cursor cursor = resolver.query(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI,
                null,
                null,
                null,
                FavoriteMovieContract.FavoriteMovieEntry.ID_COLUMN + " ASC");

        assertNotNull("Cursor is null", cursor);
        assertEquals("insert count does not match", 1, cursor.getCount());

        cursor.moveToFirst();
        TestUtils.validateRecords("testInsert. Error validating movie entry ", cursor, value);
        cursor.close();
    }
    /**
     * delete all records in the database
     */
    private void deleteAllRecords() {
        FavoriteMovieDbHelper helper = new FavoriteMovieDbHelper(InstrumentationRegistry.getTargetContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME, null, null);
        db.close();
    }
}
