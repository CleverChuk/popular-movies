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

package com.cleverchuk.popularmovies.adapters;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cleverchuk.popularmovies.utils.ApiResponse;

import java.util.ArrayList;

/**
 * base class for all adapters
 * Created by chuk on 4/27/18.
 */

abstract public class PopMovieBaseAdapter<VH extends RecyclerView.ViewHolder, D extends ApiResponse> extends RecyclerView.Adapter<VH> {
    ArrayList<D> mData;
    private String sDATA_KEY;
    OnMovieItemClickListener mListener;
    /**
     * interface to communicate with the activity
     */
    public interface OnMovieItemClickListener {
        void itemClicked(int position);
    }

    PopMovieBaseAdapter(String key) {
        sDATA_KEY = key;
        mData = new ArrayList<>();
    }

    /**
     * retrieve item at position i
     * @param i item position
     * @return object of type D
     */
    public D getItem(int i) {
        return mData.get(i);
    }

    /**
     * saves the current data
     * @param out bundle
     */
    public void saveInstanceState(Bundle out) {
        out.putParcelableArrayList(sDATA_KEY, mData);
    }

    /**
     * restores save data
     * @param in bundle
     */
    public void restoreInstanceState(Bundle in) {
        mData = in.getParcelableArrayList(sDATA_KEY);
        notifyDataSetChanged();
    }

    public void addData(D data) {
        mData.add(data);
        notifyDataSetChanged();
    }


    public void addData(ArrayList<D> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
       return mData.size();
    }

    public void cleanUp(){
        mListener = null;
    }

    /**
     * base class for all view holders
     */
    abstract static class BaseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        BaseHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }
    }

}
