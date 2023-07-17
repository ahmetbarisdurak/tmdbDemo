package com.tmdbProject.tmdbDemo.service;

import com.tmdbProject.tmdbDemo.entity.Movie;
import com.tmdbProject.tmdbDemo.exceptions.MovieNotFoundException;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService{

    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private JSONParser jsonParser;
    private RestTemplate restTemplate;

    @Value("${api.key}")
    private String apiKey;

    @Value("${access.token}")
    private String accessToken;

    MovieServiceImpl() {
        jsonParser = new JSONParser();
        restTemplate = new RestTemplate();
    }

    @Override
    public Movie findById(int theId) {
        // creating URL
        String url = BASE_URL + "/movie/" + theId;

        ResponseEntity<String> response = apiCall(url);

        // Get response answers
        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();

            JSONObject movieObject = null;
            try {
                movieObject = (JSONObject) jsonParser.parse(responseBody);
            }
            catch (ParseException e) {
                throw new RuntimeException("Error parsing JSON: " + e.getMessage(), e);
            }

            return extractMovieDetails(movieObject);
        }
        else
            throw new MovieNotFoundException("No movie with this id");
    }

    @Override
    public List<Movie> searchMovie(String inputMovieTitle) {

        inputMovieTitle = inputMovieTitle.replace(" ", "+");

        // Build the URL with the search endpoint and query parameter
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/search/movie")
                .queryParam("query", inputMovieTitle)
                .toUriString();

        ResponseEntity<String> response = apiCall(url);

        // Process the response
        if (response.getStatusCode().is2xxSuccessful()) {

            String responseBody = response.getBody();

            JSONObject  jsonObject = null;
            try {
                jsonObject = (JSONObject) jsonParser.parse(responseBody);
            }
            catch (ParseException e) {
                throw new RuntimeException("Error parsing JSON: " + e.getMessage(), e);
            }

            JSONArray resultsArray = (JSONArray) jsonObject.get("results");

            List<Movie> foundMovieList = new ArrayList<Movie>();

            for(int i = 0; i < resultsArray.size(); i++) {

                JSONObject movieObject = (JSONObject) resultsArray.get(i);

                foundMovieList.add(extractMovieDetails(movieObject));
            }

            return foundMovieList;

        }
        else
            throw new MovieNotFoundException("No movie with this title");
    }

    private ResponseEntity<String> apiCall(String url) {

        // creating headers for authorization
        HttpHeaders headers = new HttpHeaders();

        // setting authorization as accessToken "bearer ...."
        headers.setBearerAuth(accessToken);

        // add headers to entity for response
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // calling GET request
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        return response;
    }
    private Movie extractMovieDetails(JSONObject movieObject) {
        String posterURL = (String) movieObject.get("poster_path");
        long movieId = (long) movieObject.get("id");
        String movieTitle = (String) movieObject.get("original_title");

        Movie foundMovie = new Movie(movieId, movieTitle, posterURL);

        return foundMovie;
    }

}
