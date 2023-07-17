package com.tmdbProject.tmdbDemo.entity;

import jakarta.persistence.*;

@Entity
@Table(name="watchlist")
public class Watchlist {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="watchlist_id")
    private int watchlistId;

    @Column(name="user_id")
    private int userId;

    @Column(name="movie_id")
    private long movieId;

    public Watchlist() {

    }

    public Watchlist(int userId, long movieId) {
        this.userId = userId;
        this.movieId = movieId;
    }

    public Watchlist(int watchlistId, int userId, long movieId) {
        this.watchlistId = watchlistId;
        this.userId = userId;
        this.movieId = movieId;
    }

    public int getWatchlistId() {
        return watchlistId;
    }

    public void setWatchlistId(int watchlistId) {
        this.watchlistId = watchlistId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }


    @Override
    public String toString() {
        return "Watchlist{" +
                "watchlistId=" + watchlistId +
                ", userId=" + userId +
                ", movieId=" + movieId +
                '}';
    }
}
