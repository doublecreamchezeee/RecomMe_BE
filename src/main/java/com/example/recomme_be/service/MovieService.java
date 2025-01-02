package com.example.recomme_be.service;

import com.example.recomme_be.dto.request.movie.MoviePopularRequest;
import com.example.recomme_be.dto.request.movie.MovieRatingUpdateRequest;
import com.example.recomme_be.dto.request.movie.MovieSearchRequest;
import com.example.recomme_be.dto.response.RetrieverResponse;
import com.example.recomme_be.dto.response.movie.TmdbMovieListResponse;
import com.example.recomme_be.model.*;
import com.example.recomme_be.repository.MovieRepository;
import com.example.recomme_be.repository.RatingRepository;
import com.example.recomme_be.repository.ReviewRepository;
import com.example.recomme_be.repository.SearchHistoryRepository;
import com.mongodb.DBObject;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieService {
    private static final Logger log = LoggerFactory.getLogger(MovieService.class);
    private final RetrieverService retrieverService;
    private final MovieRepository movieRepository;
    private final SearchHistoryRepository searchHistoryRepository;
    private final ReviewRepository reviewRepository;
    private final RatingRepository ratingRepository;

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

    public TmdbMovieListResponse searchMoviesWithLLM(MovieSearchRequest request) {
        // Step 1: Call RetrieverService to get a list of movie IDs
        RetrieverResponse retrieverResponse = retrieverService.search(
                "movies", request.getQuery(), 10, 0.5);

        // Step 2: Extract movie IDs from the retriever response
        List<String> movieIds = retrieverResponse.getData().getResult();
        if (movieIds.isEmpty()) {
            return TmdbMovieListResponse.builder()
                    .page(request.getPage())
                    .results(List.of()) // Empty result
                    .build();
        }

        // Step 3: Iterate over IDs and retrieve movie details
        List<DBObject> movies = movieIds.stream()
                .map(movieId -> {
                    try {
                        // Use existing repository method to get details
                        return movieRepository.getDetailWithObjectId(movieId);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        // Log error if needed and skip invalid results
                        return null;
                    }
                })
                .filter(java.util.Objects::nonNull) // Filter out null results
                .toList();

        // Step 4: Check if no valid movies were found
        if (movies.isEmpty()) {
            return TmdbMovieListResponse.builder()
                    .page(request.getPage())
                    .results(List.of()) // Empty result
                    .build();
        }

        // Step 5: Return movies in the response
        return TmdbMovieListResponse.builder()
                .page(request.getPage())
                .results(movies)
                .build();
    }

    public void saveSearch(String userId, String query){
        SearchHistory searchHistory = SearchHistory.builder()
                .userId(userId)
                .query(query)
                .timestamp(LocalDateTime.now())
                .build();
        searchHistoryRepository.save(searchHistory);
    }

    public List<SearchHistory> getSearchHistory(String userId) {
        return searchHistoryRepository.findByUserIdOrderByTimestampDesc(userId);
    }

    // Rate a movie
    public Rating rateMovie(MovieRatingUpdateRequest request) {
        Rating userRating = Rating.builder()
                .movieId(request.getMovieId())
                .rating(request.getRating())
                .userId(request.getUserId())
                .build();
        ratingRepository.save(userRating);
        movieRepository.updateMovieRating(request.getMovieId(), request.getRating());
        return userRating;
    }

    public List<Rating> getListRating(String userId) {
        return ratingRepository.findAllByUserId(userId);

    }

    // Add a review for a movie
    public Review addReview(String movieId, String userId, String content, double rating) {
        Review review = new Review();
        review.setMovieId(movieId);
        review.setUserId(userId);
        review.setContent(content);
        review.setRating(rating);
        return reviewRepository.save(review);
    }

    // Get reviews for a movie
    public List<Review> getReviews(String movieId) {
        return reviewRepository.findByMovieId(movieId);
    }
}
