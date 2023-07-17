package com.tmdbProject.tmdbDemo.service;

import com.tmdbProject.tmdbDemo.dao.WatchlistRepository;
import com.tmdbProject.tmdbDemo.entity.Watchlist;
import com.tmdbProject.tmdbDemo.exceptions.WatchlistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WatchlistServiceImpl implements WatchlistService {

    private WatchlistRepository watchlistRepository;

    @Autowired
    public WatchlistServiceImpl(WatchlistRepository thewatchlistRepository) {
        watchlistRepository = thewatchlistRepository;
    }

    @Override
    public Watchlist saveToWatchlist(int userId, long movieId) {

        if(watchlistRepository.countByUserIdAndMovieId(userId, movieId) > 0 )
            throw new WatchlistException("This movie has been already added to list");

        Watchlist theWatchlist = new Watchlist(userId, movieId);

        watchlistRepository.save(theWatchlist);

        return theWatchlist;
    }
}
