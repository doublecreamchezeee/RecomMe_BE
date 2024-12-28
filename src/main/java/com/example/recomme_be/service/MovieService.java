package com.example.recomme_be.service;

import com.example.recomme_be.dto.request.movie.MoviePopularRequest;
import com.example.recomme_be.dto.request.movie.MovieSearchRequest;
import com.example.recomme_be.dto.response.movie.TmdbMovieListResponse;
import com.example.recomme_be.repository.MovieRepository;
import com.mongodb.DBObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieService {

    private final MovieRepository movieRepository;

    public TmdbMovieListResponse getPopularMovies(MoviePopularRequest request) {
        var movies = movieRepository.getPopular(request);
        return TmdbMovieListResponse.builder()
                .page(request.getPage())
                .results(movies)
                .build();
    }

    public TmdbMovieListResponse getTrendingMovies(String timeWindow) {
        var movies = movieRepository.getTrending(timeWindow);
        return TmdbMovieListResponse.builder()
                .results(movies)
                .build();
    }

    public DBObject getDetailMovie(String movieId) {
        return movieRepository.getDetail(movieId);
    }

    public TmdbMovieListResponse searchMovies(MovieSearchRequest request) {
        var movies = movieRepository.search(request);
        return TmdbMovieListResponse.builder()
                .page(request.getPage())
                .results(movies)
                .build();
    }
}
