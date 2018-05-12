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

import com.cleverchuk.popularmovies.BuildConfig;
import com.cleverchuk.popularmovies.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple class for building Api endpoints
 * Created by chuk on 4/24/18.
 */

public class Query {
    private static final String API_KEY = BuildConfig.API_KEY;

    /**
     * appends the api key to the end point
     *
     * @param context calling context
     * @return complete endpoint
     */
    public static String getPopularEndpoint(Context context) {
        return context.getString(R.string.popular_movies_endpoint) +
                API_KEY;
    }

    /**
     * appends the api key to the end point
     *
     * @param context calling context
     * @return complete endpoint
     */
    public static String getTopRatedEndpoint(Context context) {
        return context.getString(R.string.top_rated_movies_endpoint) +
                API_KEY;
    }

    /**
     * creates the complete path to the poster
     *
     * @param posterUri poster relative path
     * @param context   calling context
     * @return complete endpoint
     */
    public static String getPosterEndpoint(Context context, String posterUri) {
        String root = context.getString(R.string.poster_endpoint);
        Pattern p = Pattern.compile(context.getString(R.string.reg_pattern));
        Matcher m = p.matcher(posterUri);

        if (m.matches())
            return root + posterUri.replace("/", "");

        return root + posterUri;
    }

    public static String getReviewPath(Context context, String id) {
        return context.getString(R.string.movie_endpoint) + id +"/"+ context.getString(R.string.reviews)
                + "?api_key=" + API_KEY;
    }

    public static String getTrailerPath(Context context, String id) {
        return context.getString(R.string.movie_endpoint) + id+"/" + context.getString(R.string.trailer)
                + "?api_key=" + API_KEY;
    }
}
