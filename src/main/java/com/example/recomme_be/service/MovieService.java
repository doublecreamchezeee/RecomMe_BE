package com.example.recomme_be.service;

import com.example.recomme_be.client.TmdbClient;
import com.example.recomme_be.dto.response.movie.MovieResponse;
import com.example.recomme_be.mapper.MovieMapper;
import com.example.recomme_be.model.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Lombok will generate the constructor with final fields
public class MovieService {

    private final TmdbClient tmdbClient;
    private final MovieMapper movieMapper;

    public List<MovieResponse> getPopularMovies() {
        List<Movie> movies = tmdbClient.fetchPopularMovies();

        return movies.stream()
                .map(movieMapper::toMovieResponse)
                .collect(Collectors.toList());
    }

    public List<MovieResponse> getTrendingMovies(String timeWindow) {
        List<Movie> trendingMovies = tmdbClient.fetchTrendingMovies(timeWindow);

        return trendingMovies.stream()
                .map(movieMapper::toMovieResponse)
                .collect(Collectors.toList());
    }
}