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
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cleverchuk.popularmovies.sqlite.FavoriteMovieContract;
import com.cleverchuk.popularmovies.sqlite.FavoriteMovieDbHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * A simple {@className()}
 * Created by chuk on 4/26/18.
 */
@RunWith(AndroidJUnit4.class)
public class TestProvider {
    private final Context mContext = InstrumentationRegistry.getTargetContext();
    private final ContentResolver resolver = mContext.getContentResolver();

    @Before
    @After
    public void cleanUp() {
        deleteAllRecords();
    }

    @Test
    public void testBulkInsert() {
        deleteAllRecords();
        ContentValues[] values = TestUtils.createBulkTestFavoriteMovieValues();
        int count = resolver.bulkInsert(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI,
                values);
        assertEquals("Bulk insert count does not match", 5, count);

        Cursor cursor = resolver.query(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI,
                null,
                null,
                null,
                FavoriteMovieContract.FavoriteMovieEntry.ID_COLUMN + " ASC");

        assertNotNull("Cursor is null", cursor);
        assertEquals("Bulk insert count does not match", 5, cursor.getCount());

        cursor.moveToFirst();
        for (int i = 0; i < 5; i++, cursor.moveToNext()) {
            TestUtils.validateRecords("testBulkInsert. Error validating movie entry " + i, cursor, values[i]);
        }


        cursor.close();
    }

    @Test
    public void testInsert() {
        deleteAllRecords();
        Uri expectUri = FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI.buildUpon().appendPath("1").build();

        ContentValues value = TestUtils.createBulkTestFavoriteMovieValues()[0];
        Uri actualUri = resolver.insert(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI, value);


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

        assertEquals("insert uri does not match", expectUri, actualUri);

    }

    @Test
    public void testQuery() {
        testInsert();
        Cursor cursor = resolver.query(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI,
                null,
                null,
                null,
                FavoriteMovieContract.FavoriteMovieEntry.ID_COLUMN + " ASC");

        assertNotNull("cursor is null",cursor);
        cursor.moveToFirst();

        int index = cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.TITLE_COLUMN);
        String actualTitle = cursor.getString(index);
        String expectedTitle = "12 Years a Slave";

        // validate title
        assertEquals("",expectedTitle,actualTitle);

        // validate id
        index = cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.ID_COLUMN);
        String actualId = cursor.getString(index);
        String expectedId = "0";

        assertEquals("",expectedId,actualId);


    }
    @Test
    public void testQuery2() {
        // insert data into the database
        testInsert();

        // read from the database
        Cursor cursor = resolver.query(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI.buildUpon()
                .appendPath("0").build(),
                null,
                null,
                null,
                FavoriteMovieContract.FavoriteMovieEntry.ID_COLUMN + " ASC");

        assertNotNull("cursor is null",cursor);
        cursor.moveToFirst();

        int index = cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.TITLE_COLUMN);
        String actualTitle = cursor.getString(index);
        String expectedTitle = "12 Years a Slave";

        // validate title
        assertEquals("",expectedTitle,actualTitle);

        // validate id
        index = cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.ID_COLUMN);
        String actualId = cursor.getString(index);
        String expectedId = "0";

        assertEquals("",expectedId,actualId);


    }

    @Test
    public void testDelete() {
        // insert data into the data base
        testInsert();

        // delete data from the db
        int rowsDeleted = resolver.delete(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI.buildUpon()
                        .appendPath("0").build(),null,null);

        // read data from the database
        Cursor cursor = resolver.query(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI.buildUpon()
                        .appendPath("0").build(),
                null,
                null,
                null,
                FavoriteMovieContract.FavoriteMovieEntry.ID_COLUMN + " ASC");

        // ensure that data was successfully removed from the database
        assertNotNull("cursor is null",cursor);
        assertEquals("Count",0,cursor.getCount());
        assertEquals("deleted ",1,rowsDeleted);
    }


    @Test
    public void testUpdate() {
        // insert data into the DB
        testInsert();
        // create update values for the inserted data
        ContentValues values = new ContentValues(2);
        values.put(FavoriteMovieContract.FavoriteMovieEntry.TITLE_COLUMN,"12 Years on the run");
        values.put(FavoriteMovieContract.FavoriteMovieEntry.ID_COLUMN,"0");

        // update values
        int rowsUpdate =  resolver.update(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI.buildUpon()
                .appendPath("0").build(),values,null,null);


        // read updated value
        Cursor cursor = resolver.query(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI.buildUpon()
                        .appendPath("0").build(),
                null,
                null,
                null,
                FavoriteMovieContract.FavoriteMovieEntry.ID_COLUMN + " ASC");

        assertNotNull("cursor is null",cursor);
        cursor.moveToFirst();

        int index = cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.TITLE_COLUMN);
        String actualTitle = cursor.getString(index);
        String expectedTitle = "12 Years on the run";

        // validate title
        assertEquals("",expectedTitle,actualTitle);

        // validate id
        index = cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.ID_COLUMN);
        String actualId = cursor.getString(index);
        String expectedId = "0";

        assertEquals("",expectedId,actualId);
        assertEquals("updated ",1,rowsUpdate);
        assertEquals("Count",1,cursor.getCount());
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
