package com.example.recomme_be.controller;

import com.example.recomme_be.configuration.core.PublicEndpoint;
import com.example.recomme_be.dto.ApiResponse;
import com.example.recomme_be.dto.request.movie.MoviePopularRequest;
import com.example.recomme_be.dto.request.movie.MovieSearchRequest;
import com.example.recomme_be.dto.response.movie.TmdbMovieListResponse;
import com.example.recomme_be.service.MovieService;
import com.mongodb.DBObject;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
    public class MovieController {

        private final MovieService movieService;

        @PublicEndpoint
        @GetMapping("/popular")
        public ApiResponse<TmdbMovieListResponse> getPopularMovies(
                @ModelAttribute MoviePopularRequest moviePopularRequest) {
            var response = movieService.getPopularMovies(moviePopularRequest);
            return ApiResponse.<TmdbMovieListResponse>builder()
                    .code(200)
                    .message("Fetched popular movies successfully")
                    .result(response)
                    .build();
        }

        @PublicEndpoint
        @GetMapping("/trending")
        public ApiResponse<TmdbMovieListResponse> getTrendingMovies(
                @RequestParam(defaultValue = "day") String timeWindow) {

            if (!timeWindow.equals("day") && !timeWindow.equals("week")) {
                throw new IllegalArgumentException("Invalid time window. Use 'day' or 'week'.");
            }

            var response = movieService.getTrendingMovies(timeWindow);
            return ApiResponse.<TmdbMovieListResponse>builder()
                    .code(200)
                    .message("Fetched trending movies successfully")
                    .result(response)
                    .build();
        }

        @PublicEndpoint
        @GetMapping("/search")
        public ApiResponse<TmdbMovieListResponse> searchMovies(
                @ModelAttribute MovieSearchRequest movieSearchRequest) {

            if (movieSearchRequest.getQuery() == null || movieSearchRequest.getQuery().isBlank()) {
                throw new IllegalArgumentException("Please provide a search query.");
            }

            var response = movieService.searchMovies(movieSearchRequest);
            return ApiResponse.<TmdbMovieListResponse>builder()
                    .code(200)
                    .message("Fetched movies with keyword successfully")
                    .result(response)
                    .build();
        }

        @PublicEndpoint
        @GetMapping("/{movieId}")
        public ApiResponse<DBObject> getDetailMovie(
                @PathVariable(name = "movieId") @NotBlank(message = "Movie ID must not be empty") String movieId) {

            var response = movieService.getDetailMovie(movieId);
            return ApiResponse.<DBObject>builder()
                    .code(200)
                    .message("Fetched movie details successfully")
                    .result(response)
                    .build();
        }
    }
