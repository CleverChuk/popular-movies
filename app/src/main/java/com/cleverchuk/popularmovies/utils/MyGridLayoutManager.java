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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

/**
 * Idea taken from:
 * Author: Diep Nguyen
 * Source: https://codentrick.com/part-4-android-recyclerview-grid/
 * a subclass of {@link GridLayoutManager} this class
 * auto fits the number of columns based on screen size
 * Created by chuk on 4/30/18,at 22:47.
 */
public class MyGridLayoutManager extends GridLayoutManager {
        private int width;
        private boolean isWidthChanged = true;

        public MyGridLayoutManager(Context context, int spanCount) {
            super(context, spanCount);
            setWidth(getColumnWidth(context, spanCount));
        }

        public MyGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
            super(context, spanCount, orientation, reverseLayout);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            if (isWidthChanged && width > 0) {
                int totalSpace;
                if (getOrientation() == VERTICAL) {
                    totalSpace = getWidth() - getPaddingLeft() - getPaddingRight();
                } else {
                    totalSpace = getHeight() - getPaddingBottom() - getPaddingTop();
                }

                int spanCount = Math.max(1, totalSpace / width);
                setSpanCount(spanCount);
                isWidthChanged = false;
            }
            super.onLayoutChildren(recycler, state);
        }

        private int getColumnWidth(Context context, int spanCount) {
            int width = spanCount;
            if (spanCount <= 0) {
                width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48,
                        context.getResources().getDisplayMetrics());
            }
            return width;
        }

        private void setWidth(int newWidth) {
            if (newWidth > 0 && newWidth != width)
            {
                width = newWidth;
                isWidthChanged = true;
            }
        }
}
