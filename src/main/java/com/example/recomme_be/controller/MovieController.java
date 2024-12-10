package com.example.recomme_be.controller;

import com.example.recomme_be.configuration.core.PublicEndpoint;
import com.example.recomme_be.dto.ApiResponse;
import com.example.recomme_be.dto.request.movie.MovieSearchRequest;
import com.example.recomme_be.dto.response.movie.DetailTmdbMovieResponse;
import com.example.recomme_be.dto.response.movie.TmdbMovieListResponse;
import com.example.recomme_be.service.MovieService;
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
    public ApiResponse<TmdbMovieListResponse> getPopularMovies() {
        TmdbMovieListResponse tmdbMovieListResponse = movieService.getPopularMovies();

        if (tmdbMovieListResponse == null) {
            return ApiResponse.<TmdbMovieListResponse>builder()
                    .code(404)
                    .message("No popular movies found")
                    .result(null)
                    .build();
        } else {
            return ApiResponse.<TmdbMovieListResponse>builder()
                    .code(200)
                    .message("Fetched popular movies successfully")
                    .result(tmdbMovieListResponse)
                    .build();
        }
    }

    @PublicEndpoint
    @GetMapping("/trending")
    public ApiResponse<TmdbMovieListResponse> getTrendingMovies(
            @RequestParam(defaultValue = "day") String timeWindow) {

        if (!timeWindow.equals("day") && !timeWindow.equals("week")) {
            return ApiResponse.<TmdbMovieListResponse>builder()
                    .code(400)
                    .message("Invalid time window. Please use 'day' or 'week'.")
                    .result(null)
                    .build();
        }

        TmdbMovieListResponse tmdbMovieListResponse = movieService.getTrendingMovies(timeWindow);

        if (tmdbMovieListResponse == null) {
            return ApiResponse.<TmdbMovieListResponse>builder()
                    .code(404)
                    .message("No trending movies found")
                    .result(null)
                    .build();
        }

        return ApiResponse.<TmdbMovieListResponse>builder()
                .code(200)
                .message("Fetched trending movies successfully")
                .result(tmdbMovieListResponse)
                .build();
    }

    @PublicEndpoint
    @GetMapping("/search")
    public ApiResponse<TmdbMovieListResponse> searchMovies(
            @ModelAttribute MovieSearchRequest movieSearchRequest) {

        if (movieSearchRequest.getQuery() == null || movieSearchRequest.getQuery().isBlank()) {
            return ApiResponse.<TmdbMovieListResponse>builder()
                    .code(400)
                    .message("Please provide a query")
                    .result(null)
                    .build();
        }

        TmdbMovieListResponse tmdbMovieListResponse = movieService.search(movieSearchRequest);

        if (tmdbMovieListResponse == null) {
            return ApiResponse.<TmdbMovieListResponse>builder()
                    .code(404)
                    .message("No movies found")
                    .result(null)
                    .build();
        }

        return ApiResponse.<TmdbMovieListResponse>builder()
                .code(200)
                .message("Fetched movies with keyword successfully")
                .result(tmdbMovieListResponse)
                .build();
    }

    @PublicEndpoint
    @GetMapping("/{movieId}")
    public ApiResponse<DetailTmdbMovieResponse> getDetailMovie (
            @PathVariable(name = "movieId") @NotBlank(message = "Movie ID must not be empty") String movieId) {
        DetailTmdbMovieResponse detailTmdbMovieResponse = movieService.getDetailMovie(movieId);

        if (detailTmdbMovieResponse == null) {
            return ApiResponse.<DetailTmdbMovieResponse>builder()
                    .code(404)
                    .message("Movie not found")
                    .result(null)
                    .build();
        }

        return ApiResponse.<DetailTmdbMovieResponse>builder()
                .code(200)
                .message("Fetched trending movies successfully")
                .result(detailTmdbMovieResponse)
                .build();
    }
}
