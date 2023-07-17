package com.tmdbProject.tmdbDemo.service;

import com.tmdbProject.tmdbDemo.entity.Movie;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;

public interface MovieService {

    Movie findById(int theId) throws IOException, InterruptedException, ParseException;

    List<Movie> searchMovie(String movieName) throws ParseException;
}
