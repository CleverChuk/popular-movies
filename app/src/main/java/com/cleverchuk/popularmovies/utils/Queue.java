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

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

/**
 * A simple singleton that encapsulates {@link RequestQueue}
 * Created by chuk on 4/25/18.
 */

public class Queue {
    private static Queue mQueue;
    private final RequestQueue mRequestQueue;
    private final ArrayList<String> tags;

    private Queue(Context context) {
        mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        tags = new ArrayList<>();
    }

    /**
     * return an instance of this class if not create one
     * @param context calling context
     * @return Queue object
     */
    public static Queue getInstance(Context context) {
        if (mQueue == null)
            mQueue = new Queue(context);

        return mQueue;
    }

    /**
     * queues the given request
     *
     * @param request request to queue
     * @param tag request tag
     * @param <T> request type
     */
    public <T> void queue(Request<T> request, String tag) {
        tags.add(tag);
        request.setTag(tag);
        mRequestQueue.add(request);
    }

    /**
     * cancel a request with given tag
     * @param tag request tag
     */
    public void cancel(String tag) {
        mRequestQueue.cancelAll(tag);
    }

    /**
     * cancels all queued requests
     */
    public void cancelAll() {
        for (String tag : tags) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
