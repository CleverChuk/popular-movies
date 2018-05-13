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

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cleverchuk.popularmovies.R;
import com.cleverchuk.popularmovies.models.Video;
import com.squareup.picasso.Picasso;

/**
 * adapter class that extends {@link PopMovieBaseAdapter}
 * used for displaying movie trailers
 * Created by chuk on 4/27/18.
 */

public class VideoAdapter extends PopMovieBaseAdapter<VideoAdapter.VideoHolder, Video> {
    private static final String KEY = "com.cleverchuk.video.adapter.key";
    private static final String thumbs = "http://img.youtube.com/vi/ph/2.jpg";
    private Context mContext;

    public VideoAdapter(Context context, OnMovieItemClickListener listener) {
        super(KEY);
        mContext = context;
        mListener = listener;
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_video, parent, false);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        Video video = mData.get(position);
        String id = video.getKey();
        Picasso.get().load(thumbs.replace("ph", id)).into(holder.thumbnailIv);

        holder.titleTv.setText(video.getType());
    }

    class VideoHolder extends PopMovieBaseAdapter.BaseHolder {
        ImageView playIconIv;
        ImageView thumbnailIv;
        TextView titleTv;

        public VideoHolder(View itemView) {
            super(itemView);
            playIconIv = itemView.findViewById(R.id.play_icon_iv);
            thumbnailIv = itemView.findViewById(R.id.thumbnail_iv);
            titleTv = itemView.findViewById(R.id.type_tv);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            mListener.itemClicked(pos,v);
        }
    }
}
