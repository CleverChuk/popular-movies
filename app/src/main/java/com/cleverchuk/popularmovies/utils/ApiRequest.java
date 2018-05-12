package com.cleverchuk.popularmovies.utils;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

/**
 * A custom request that extends {@link Request}
 * Created by chuk on 4/25/18.
 */

public class ApiRequest<T> extends Request<T> {
    private final Response.Listener<T> mListener;
    private final Class<T> tClass;

    public ApiRequest(String url, Response.Listener<T> listener, Class<T> tClass,
                      Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);

        this.mListener = listener;
        this.tClass = tClass;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            Gson gson = new Gson();
            return Response.success(gson.fromJson(json,tClass),
                    HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }
}
