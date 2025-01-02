package com.example.recomme_be.controller;

import com.example.recomme_be.configuration.core.PublicEndpoint;
import com.example.recomme_be.dto.ApiResponse;
import com.example.recomme_be.dto.request.movie.MoviePopularRequest;
import com.example.recomme_be.dto.request.movie.MovieRatingUpdateRequest;
import com.example.recomme_be.dto.request.movie.MovieSearchRequest;
import com.example.recomme_be.dto.response.movie.TmdbMovieListResponse;
import com.example.recomme_be.model.Review;
import com.example.recomme_be.model.SearchHistory;
import com.example.recomme_be.service.MovieService;
import com.mongodb.DBObject;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
                @ModelAttribute MovieSearchRequest movieSearchRequest, @RequestParam Boolean isAdvancedSearch) {

            if (movieSearchRequest.getQuery() == null || movieSearchRequest.getQuery().isBlank()) {
                throw new IllegalArgumentException("Please provide a search query.");
            }
            TmdbMovieListResponse response = null;
            if (isAdvancedSearch) {
                response = movieService.searchMoviesWithLLM(movieSearchRequest);
            }
            else {
                response = movieService.searchMovies(movieSearchRequest);
            }
            return ApiResponse.<TmdbMovieListResponse>builder()
                    .code(200)
                    .message("Fetched movies with keyword successfully")
                    .result(response)
                    .build();
        }

//        @PublicEndpoint
//        public ApiResponse<TmdbMovieListResponse> searchMoviesWithLLM(
//                @ModelAttribute MovieSearchRequest movieSearchRequest){
//            if (movieSearchRequest.getQuery() == null || movieSearchRequest.getQuery().isBlank()) {
//                throw new IllegalArgumentException("Please provide a search query.");
//            }
//            var response = movieService.searchMoviesWithLLM(movieSearchRequest);
//            return ApiResponse.<TmdbMovieListResponse>builder()
//                    .code(200)
//                    .message("Fetched movies with llm successfully")
//                    .result(response)
//                    .build();
//        }

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

    // Save search history
    @PostMapping
    public ApiResponse<Void> saveSearch(
            @RequestParam String userId,
            @RequestParam String query) {
        movieService.saveSearch(userId, query);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Search history saved successfully")
                .build();
    }

    // Get search history
    @GetMapping
    public ApiResponse<List<SearchHistory>> getSearchHistory(@RequestParam String userId) {
        var history = movieService.getSearchHistory(userId);
        return ApiResponse.<List<SearchHistory>>builder()
                .code(200)
                .message("Fetched search history successfully")
                .result(history)
                .build();
    }

    @PostMapping("/rate")
    public ApiResponse<Void> rateMovie(@RequestBody MovieRatingUpdateRequest request) {
        try {
            // Call the service to update the movie rating
            movieService.rateMovie(request);
            return ApiResponse.<Void>builder()
                    .code(HttpStatus.OK.value())
                    .message("Movie rating updated successfully.")
                    .build();
        } catch (Exception e) {
            return ApiResponse.<Void>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Error updating movie rating: " + e.getMessage())
                    .build();
        }

    }

    // Add review for a movie
    @PostMapping("/{movieId}/reviews")
    public ApiResponse<Review> addReview(@PathVariable String movieId, @RequestParam String userId,
                                         @RequestParam String content, @RequestParam double rating) {
        Review review = movieService.addReview(movieId, userId, content, rating);
        return ApiResponse.<Review>builder()
                .code(200)
                .message("Review added successfully")
                .result(review)
                .build();
    }

    // Get movie reviews
    @GetMapping("/{movieId}/reviews")
    public ApiResponse<List<Review>> getReviews(@PathVariable String movieId) {
        List<Review> reviews = movieService.getReviews(movieId);
        return ApiResponse.<List<Review>>builder()
                .code(200)
                .message("Fetched reviews successfully")
                .result(reviews)
                .build();
    }

}
