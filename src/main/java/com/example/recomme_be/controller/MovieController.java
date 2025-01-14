package com.example.recomme_be.controller;

import com.example.recomme_be.configuration.core.PublicEndpoint;
import com.example.recomme_be.dto.ApiResponse;
import com.example.recomme_be.dto.request.movie.*;
import com.example.recomme_be.dto.response.movie.TmdbMovieListResponse;
import com.example.recomme_be.model.*;
import com.example.recomme_be.service.MovieService;
import com.mongodb.BasicDBObject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @PublicEndpoint
    @GetMapping("")
    public ApiResponse<TmdbMovieListResponse> getMovies(
            @ModelAttribute MoviesFilterRequest request) {
//        List<String> objectIdList = Arrays.stream(objectIds.split(","))
//                .map(String::trim)
//                .toList();
        var response = movieService.filter(request);
        return ApiResponse.<TmdbMovieListResponse>builder()
                .code(200)
                .message("Fetched movies successfully")
                .result(response)
                .build();
    }

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

    @PublicEndpoint
    @GetMapping("/details/{movieId}")
    public ApiResponse<BasicDBObject> getDetailMovie(
            @PathVariable(name = "movieId") @NotBlank(message = "Movie ID must not be empty") String movieId) throws  NumberFormatException {

        var response = movieService.getDetailMovie(Integer.parseInt(movieId));
        return ApiResponse.<BasicDBObject>builder()
                .code(200)
                .message("Fetched movie details successfully")
                .result(response)
                .build();
    }

    // Save search history
    @PostMapping("/save-search")
    public ApiResponse<Void> saveSearch(
            Authentication authentication,
            @RequestParam String query) {
        String userId = (String) authentication.getPrincipal();
        movieService.saveSearch(userId, query);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Search history saved successfully")
                .build();
    }

    // Get search history
    @GetMapping("/searchHistory")
    public ApiResponse<List<SearchHistory>> getSearchHistory(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        var history = movieService.getSearchHistory(userId);
        return ApiResponse.<List<SearchHistory>>builder()
                .code(200)
                .message("Fetched search history successfully")
                .result(history)
                .build();
    }

    @PostMapping("/rate")
    public ApiResponse<Rating> rateMovie(Authentication authentication, @RequestBody MovieRatingUpdateRequest request) {
        try {
            String userId = (String) authentication.getPrincipal();
            request.setUserId(userId);
            // Call the service to update the movie rating
            Rating response = movieService.rateMovie(request);
            return ApiResponse.<Rating>builder()
                    .code(HttpStatus.OK.value())
                    .result(response)
                    .message("Movie rating updated successfully.")
                    .build();
        } catch (Exception e) {
            return ApiResponse.<Rating>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Error updating movie rating: " + e.getMessage())
                    .build();
        }

    }

    @GetMapping("/rate/{userId}")
    public ApiResponse<List<Rating>> rateMovie(@PathVariable String userId) {
            List<Rating> response = movieService.getListRating(userId);
            return ApiResponse.<List<Rating>>builder()
                    .code(HttpStatus.OK.value())
                    .result(response)
                    .message("Fetch rating list successfully.")
                    .build();
    }

    @GetMapping("/favorites")
    public ApiResponse<List<BasicDBObject>> getFavorites(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return ApiResponse.<List<BasicDBObject>>builder()
                .code(HttpStatus.OK.value())
                .result(movieService.getFavorites(userId))
                .message("Get favorites successfully.")
                .build();
    }
    @PostMapping("/favorites")
    public ApiResponse<List<FavoriteMovie>> addToFavorites(Authentication authentication, @RequestBody @Valid AddToFavoritesRequest request) {
        String userId = (String) authentication.getPrincipal();
        request.setUserId(userId);
        return ApiResponse.<List<FavoriteMovie>>builder()
                .code(HttpStatus.OK.value())
                .result(movieService.addToFavorites(request))
                .message("Add to favorites successfully.")
                .build();
    }
    @DeleteMapping("/favorites")
    public ApiResponse<Void> removeFromFavorites(Authentication authentication, @RequestBody @Valid RemoveFromFavoritesRequest request) {
        String userId = (String) authentication.getPrincipal();
        request.setUserId(userId);
        movieService.removeFromFavorites(request);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Remove from favorites successfully.")
                .build();
    }

    @GetMapping("/watchList/{userId}")
    public ApiResponse<List<BasicDBObject>> getWatchList(@PathVariable String userId) {
        return ApiResponse.<List<BasicDBObject>>builder()
                .code(HttpStatus.OK.value())
                .result(movieService.getWatchList(userId))
                .message("Get favorites successfully.")
                .build();
    }
    @PostMapping("/watchList")
    public ApiResponse<List<WatchList>> addToWatchList(Authentication authentication, @RequestBody @Valid AddToWatchListRequest request) {
        String userId = (String) authentication.getPrincipal();
        request.setUserId(userId);
        return ApiResponse.<List<WatchList>>builder()
                .code(HttpStatus.OK.value())
                .result(movieService.addToWatchList(request))
                .message("Add to watch list successfully.")
                .build();
    }
    @DeleteMapping("/watchList")
    public ApiResponse<Void> removeFromWatchList(Authentication authentication, @RequestBody @Valid RemoveFromWatchListRequest request) {
        String userId = (String) authentication.getPrincipal();
        request.setUserId(userId);
        movieService.deleteFromWatchList(request);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Remove from watch list successfully.")
                .build();
    }

    @PostMapping("/{movieId}/reviews")
    public ApiResponse<Review> addReview(Authentication authentication, @PathVariable String movieId,
                                         @RequestBody Map<String,String> body) {
        String userId = (String) authentication.getPrincipal();
        String author = (String) authentication.getCredentials();
        String content = body.get("content");
        Review review = movieService.addReview(movieId, userId, content, author);
        return ApiResponse.<Review>builder()
                .code(200)
                .message("Review added successfully")
                .result(review)
                .build();
    }
    @PublicEndpoint
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


    @PublicEndpoint
    @GetMapping("/latestTrailers")
    public ApiResponse<TmdbMovieListResponse> getLatestTrailer(@ModelAttribute LatestTrailersRequest request) {
        TmdbMovieListResponse tmdbMovieListResponse = movieService.getLatestTrailers(request);
        return ApiResponse.<TmdbMovieListResponse>builder()
                .code(200)
                .message("Fetched latest trailer successfully")
                .result(tmdbMovieListResponse)
                .build();
    }

}
