package com.example.recomme_be.repository;

import com.example.recomme_be.dto.request.movie.MoviePopularRequest;
import com.example.recomme_be.dto.request.movie.MovieSearchRequest;
import com.example.recomme_be.model.Movie;

import java.util.List;

public interface MovieRepository {
    List<Movie> getPopular(MoviePopularRequest request);

    List<Movie> getTrending(String timeWindow);

    Movie getDetail(String movieId);

    List<Movie> search(MovieSearchRequest request);
}
