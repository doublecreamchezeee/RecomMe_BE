package com.example.recomme_be.client;

import com.example.recomme_be.dto.response.movie.DetailTmdbMovieResponse;
import com.example.recomme_be.dto.response.movie.TmdbMovieListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;



@Component
@RequiredArgsConstructor
@Slf4j
public class TmdbClient {

    private static final  Logger logger = LoggerFactory.getLogger(TmdbClient.class);

    private final RestTemplate restTemplate;

    @Value("${tmdb.api.key}")
    private String apiKey;

    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final  String API_KEY_PARAM = "api_key";

    /**
     * Fetches popular movies from TMDB API.
     *
     * @return List of Movie objects fetched from TMDB API.
     */
    public TmdbMovieListResponse fetchPopularMovies() {
        String url = String.format("%s/movie/popular?api_key=%s", BASE_URL, apiKey);
        try {
            TmdbMovieListResponse response = restTemplate.getForObject(url, TmdbMovieListResponse.class);

            if (response != null && response.getResults() != null) {
                return response;
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("Error fetching popular movies: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Fetches trending movies based on the time window ('day' or 'week').
     *
     * @param timeWindow Time window for trending movies ("day" or "week").
     * @return List of Movie objects fetched from TMDB API.
     */
    public TmdbMovieListResponse fetchTrendingMovies(String timeWindow) {
        // Ensure the timeWindow parameter is either "day" or "week"
        if (!timeWindow.equals("day") && !timeWindow.equals("week")) {
            throw new IllegalArgumentException("Invalid time window. Please use 'day' or 'week'.");
        }

        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/trending/movie/{timeWindow}")
                .queryParam(API_KEY_PARAM, apiKey)
                .buildAndExpand(timeWindow)
                .toUriString();

        try {
            // Fetch the response from the TMDB API
            TmdbMovieListResponse response = restTemplate.getForObject(url, TmdbMovieListResponse.class);

            if (response != null && response.getResults() != null) {
                return response;
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("Error fetching trending movies: {}", e.getMessage());
            return null;
        }
    }

    public DetailTmdbMovieResponse fetchDetailMovie(String movieId) {
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/movie/{movieId}")
                .queryParam(API_KEY_PARAM, apiKey)
                .buildAndExpand(movieId)
                .toUriString();
        try {
            // Fetch the response from the TMDB API
            return restTemplate.getForObject(url, DetailTmdbMovieResponse.class);
        } catch (Exception e) {
            logger.error("Error fetching trending detail movie: {}", e.getMessage());
            return null;
        }
    }

    public  TmdbMovieListResponse searchMovie(String keyword) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add(API_KEY_PARAM, apiKey);
        queryParams.add("query", keyword);
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/search/movie")
                .queryParams(queryParams)
                .toUriString();
        try {
            // Fetch the response from the TMDB API
            return restTemplate.getForObject(url, TmdbMovieListResponse.class);
        } catch (Exception e) {
            logger.error("Error search movie: {}", e.getMessage());
            return null;
        }
    }


}
