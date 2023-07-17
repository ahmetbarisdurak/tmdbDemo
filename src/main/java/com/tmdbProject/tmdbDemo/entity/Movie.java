package com.tmdbProject.tmdbDemo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Movie {
    @Id
    private long id;
    private String original_title;
    private String posterURL;

    public Movie() {

    }
    public Movie(long id, String original_title, String posterURL) {
        this.id = id;
        this.original_title = original_title;
        this.posterURL = posterURL;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }
}
