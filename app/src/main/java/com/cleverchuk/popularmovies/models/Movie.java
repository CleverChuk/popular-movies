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

package com.cleverchuk.popularmovies.models;

import android.os.Parcel;

import com.cleverchuk.popularmovies.utils.ApiResponse;
import com.google.gson.annotations.SerializedName;

/**
 * A subclass of {@link ApiResponse} that encapsulates the
 * movie data from Api endpoint
 * Created by chuk on 4/25/18.
 */

public class Movie extends ApiResponse<Movie>{

    private String title;

    @SerializedName("overview")
    private String plotSynopsis;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("backdrop_path")
    private String poster;

    @SerializedName("vote_average")
    private String voteAverage;

    private String id;
    private String popularity;

    public Movie(String title,String id, String plotSynopsis, String releaseDate, String poster, String voteAverage){
        this.title = title;
        this.plotSynopsis = plotSynopsis;
        this.releaseDate = releaseDate;
        this.poster = poster;
        this.voteAverage = voteAverage;
        this.id = id;
    }

    private Movie(Parcel in) {
        title = in.readString();
        plotSynopsis = in.readString();
        releaseDate = in.readString();
        poster = in.readString();
        voteAverage = in.readString();
        id = in.readString();
        popularity = in.readString();
    }

    public String getTitle() {
        return title;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPoster() {
        return poster;
    }

    public String getVoteAverage() {
        return voteAverage;
    }
    public String getPopularity() {
        return popularity;
    }

    public String getId() {
        return id;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(plotSynopsis);
        parcel.writeString(releaseDate);
        parcel.writeString(poster);
        parcel.writeString(voteAverage);
        parcel.writeString(id);
        parcel.writeString(popularity);
    }
}
