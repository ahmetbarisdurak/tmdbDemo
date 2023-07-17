package com.tmdbProject.tmdbDemo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.tmdbProject.tmdbDemo.entity.Movie;
import com.tmdbProject.tmdbDemo.entity.Token;
import com.tmdbProject.tmdbDemo.entity.User;
import com.tmdbProject.tmdbDemo.exceptions.MovieNotFoundException;
import com.tmdbProject.tmdbDemo.exceptions.UserNotFoundException;
import com.tmdbProject.tmdbDemo.exceptions.WatchlistException;
import com.tmdbProject.tmdbDemo.service.MovieService;
import com.tmdbProject.tmdbDemo.service.TokenService;
import com.tmdbProject.tmdbDemo.service.UserService;
import com.tmdbProject.tmdbDemo.service.WatchlistService;
import com.tmdbProject.tmdbDemo.token.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController extends BaseController {

    private static final String ROLE_USER = "USER";
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private MovieService movieService;
    private WatchlistService watchlistService;


    @Autowired
    public UserController(UserService theUserService, TokenService theTokenService, MovieService theMovieService, WatchlistService theWatchlistService) {
        userService = theUserService;
        tokenService = theTokenService;
        movieService = theMovieService;
        watchlistService = theWatchlistService;
    }

    @GetMapping("/searchMovie")
    public ResponseEntity<List<Movie>> searchMovie(@RequestParam String movieTitle) {

        List<Movie> foundMovies = null;
        try {
            foundMovies = movieService.searchMovie(movieTitle);
        }
        catch (ParseException e) {
            logger.error("Error occurred: " + e.getMessage(), e);
        }

        if (foundMovies != null && !foundMovies.isEmpty())
            return ResponseEntity.ok(foundMovies);
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/searchMovie/{movieId}")
    public ResponseEntity<Movie> searchMovie(@PathVariable int movieId) {
        // I can find movie without login in. Since websites allow it. This one will return only one movie.
        Movie foundMovies = null;
        try {
            foundMovies = movieService.findById(movieId);
        }
        catch (Exception e) {
            logger.error("Error occurred: " + e.getMessage(), e);
        }

        if(foundMovies != null)
            return ResponseEntity.ok(foundMovies);
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping("/addWatchlist/{movieId}")
    public ResponseEntity<String> addWatchlist(@PathVariable int movieId, @RequestParam(PARAM_TOKEN) String token) {
        // Only user can add movies to his/hers watchlist
        if(!methodAuth(ROLE_USER, token))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized");

        try {
            // Find user by token
            Token userToken = tokenService.findByToken(token); // If it is null throws runtime exception

            Movie foundMovie = null;

            foundMovie = movieService.findById(movieId); //throws error for parsing or no movie found

            if(foundMovie != null) {

                watchlistService.saveToWatchlist(userToken.getId(), foundMovie.getId());

                return ResponseEntity.ok("Movie has been added to watchlist");
            }
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie could not be found");

        }
        catch(UserNotFoundException e)
        {
            logger.error("Error occurred: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no user with this token!");
        }
        catch (MovieNotFoundException e) {
            logger.error("Error occurred: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no movie with this id!");
        }
        catch(WatchlistException e) {
            logger.error("Error occurred: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("This movie is already been added to watchlist");
        }
        catch (Exception e) {
            logger.error("Error occurred: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no movie with this id!");
        }

    }
}
