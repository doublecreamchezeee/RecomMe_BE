package com.example.recomme_be.client;

import com.example.recomme_be.dto.response.movie.TmdbMovieResponse;
import com.example.recomme_be.model.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TmdbClient {

    private final RestTemplate restTemplate;

    @Value("${tmdb.api.key}")
    private String apiKey;

    private static final String BASE_URL = "https://api.themoviedb.org/3";

    /**
     * Fetches popular movies from TMDB API.
     *
     * @return List of Movie objects fetched from TMDB API.
     */
    public List<Movie> fetchPopularMovies() {
        String url = String.format("%s/movie/popular?api_key=%s", BASE_URL, apiKey);
        try {
            TmdbMovieResponse response = restTemplate.getForObject(url, TmdbMovieResponse.class);

            if (response != null && response.getResults() != null) {
                return response.getResults();
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            System.err.println("Error fetching popular movies: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
