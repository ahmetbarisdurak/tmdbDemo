package com.tmdbProject.tmdbDemo.dao;

import com.tmdbProject.tmdbDemo.entity.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WatchlistRepository extends JpaRepository<Watchlist, Integer> {

    @Query("SELECT COUNT(w) FROM Watchlist w WHERE w.userId = :userId AND w.movieId = :movieId")
    int countByUserIdAndMovieId(@Param("userId") int userId, @Param("movieId") long movieId);

}
