package com.tmdbProject.tmdbDemo.service;

import com.tmdbProject.tmdbDemo.entity.Watchlist;

public interface WatchlistService {

    Watchlist saveToWatchlist(int userId, long movieId);
}
