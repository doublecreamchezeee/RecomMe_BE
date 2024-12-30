package com.example.recomme_be.service;

import com.example.recomme_be.dto.request.movie.MoviePopularRequest;
import com.example.recomme_be.dto.request.movie.MovieSearchRequest;
import com.example.recomme_be.dto.response.movie.TmdbMovieListResponse;
import com.example.recomme_be.model.*;
import com.example.recomme_be.repository.MovieRepository;
import com.example.recomme_be.repository.RatingRepository;
import com.example.recomme_be.repository.ReviewRepository;
import com.example.recomme_be.repository.SearchHistoryRepository;
import com.mongodb.DBObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieService {
    private final LlmApiService llmApiService;
    private final MovieRepository movieRepository;
    private final SearchHistoryRepository searchHistoryRepository;
    private final RatingRepository ratingRepository;
    private final ReviewRepository reviewRepository;

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
        String refinedQuery = llmApiService.processQuery(request.getQuery());
        request.setQuery(refinedQuery);
        var movies = movieRepository.search(request);
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
    public Rating rateMovie(String movieId, String userId, double ratingValue) {
        // Check if the user has already rated the movie
        Rating existingRating = ratingRepository.findByMovieIdAndUserId(movieId, userId);
        if (existingRating != null) {
            existingRating.setRating(ratingValue);  // Update rating if exists
            return ratingRepository.save(existingRating);
        } else {
            Rating newRating = new Rating();
            newRating.setMovieId(movieId);
            newRating.setUserId(userId);
            newRating.setRating(ratingValue);
            return ratingRepository.save(newRating);
        }
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
