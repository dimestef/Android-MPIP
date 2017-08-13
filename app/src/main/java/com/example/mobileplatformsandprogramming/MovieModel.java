package com.example.mobileplatformsandprogramming;

import java.io.Serializable;

/**
 * Created by Dimitar on 8/12/2017.
 */

public class MovieModel implements Serializable {
    String name;
    String year;
    String director;
    String imdbId;
    String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return this.name;
    }

    public String getYear() {
        return year;
    }

    public String getDirector() {
        return director;
    }

    public String getImdbId() {
        return this.imdbId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }
}
