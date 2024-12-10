package com.example.recomme_be.service;

import com.example.recomme_be.client.TmdbClient;
import com.example.recomme_be.dto.request.movie.MovieSearchRequest;
import com.example.recomme_be.dto.response.movie.DetailTmdbMovieResponse;
import com.example.recomme_be.dto.response.movie.TmdbMovieListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // Lombok will generate the constructor with final fields
public class MovieService {

    private final TmdbClient tmdbClient;

    public TmdbMovieListResponse getPopularMovies() {
        return tmdbClient.fetchPopularMovies();
    }

    public TmdbMovieListResponse getTrendingMovies(String timeWindow) {
        return tmdbClient.fetchTrendingMovies(timeWindow);
    }

    public DetailTmdbMovieResponse getDetailMovie(String movieId) {
        return tmdbClient.fetchDetailMovie(movieId);
    }

    public TmdbMovieListResponse search(MovieSearchRequest movieSearchRequest) {
        return tmdbClient.searchMovie(movieSearchRequest);
    }
}