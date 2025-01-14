package com.example.recomme_be.service;

import com.example.recomme_be.client.TMDBClient;
import com.example.recomme_be.dto.request.movie.*;
import com.example.recomme_be.dto.response.RetrieverResponse;
import com.example.recomme_be.dto.response.movie.TmdbMovieListResponse;
import com.example.recomme_be.model.*;
import com.example.recomme_be.repository.*;
import com.mongodb.BasicDBObject;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
    private final FavoriteMovieRepository favoriteMovieRepository;
    private final WatchListRepository watchListRepository;
    private final TMDBClient tmdbClient;

    public TmdbMovieListResponse getMoviesByObjectIds(Collection<String> objectIds) {
        var movies =  movieRepository.getByObjectIds(objectIds);
        return TmdbMovieListResponse.builder()
                .results(movies)
                .build();
    }

    public TmdbMovieListResponse filter(MoviesFilterRequest request) {
        var movies =  movieRepository.filter(request);
        int count = (int) movieRepository.countByFilter(request);

        return TmdbMovieListResponse.builder()
                .page(request.getPage())
                .total_pages((count / request.getPageSize()) + 1)
                .total_results( count)
                .results(movies)
                .build();
    }

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

    public BasicDBObject getDetailMovie(Integer movieId) {
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
                "movies", request.getQuery(), 20, 0.5);

        // Step 2: Extract movie IDs from the retriever response
        List<String> movieIds = retrieverResponse.getData().getResult();
        if (movieIds.isEmpty()) {
            return TmdbMovieListResponse.builder()
                    .page(request.getPage())
                    .results(List.of()) // Empty result
                    .build();
        }

        // Step 3: Iterate over IDs and retrieve movie details
        List<BasicDBObject> movies = movieIds.stream()
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
        return searchHistoryRepository.findTop5ByUserIdOrderByTimestampDesc(userId);
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
    public Review addReview(String movieId, String userId, String content, String author) {
        Review review = new Review();
        review.setMovieId(movieId);
        review.setUserId(userId);
        review.setContent(content);
        review.setAuthor(author);
        review.setTime(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    // Get reviews for a movie
    public List<Review> getReviews(String movieId) {
        return reviewRepository.findByMovieId(movieId);
    }


    public List<BasicDBObject> getFavorites(String userId) {
        List<FavoriteMovie> favoriteMovies = favoriteMovieRepository.findAllByUserId(userId);
        List<Integer> movieIds = favoriteMovies.stream().map(FavoriteMovie::getMovieId).toList();
        return movieRepository.getByIds(movieIds);
    }
    public List<FavoriteMovie> addToFavorites(AddToFavoritesRequest request) {
        List<FavoriteMovie> favoriteMovies = new ArrayList<>();
        for (Integer movieId : request.getMovieIds()) {
            FavoriteMovie favoriteMovie = FavoriteMovie.builder()
                    .movieId(movieId) // Assuming movieId is an integer
                    .userId(request.getUserId())
                    .createdAt(new Date())
                    .build();
            favoriteMovies.add(favoriteMovie);
        }
        return favoriteMovieRepository.saveAll(favoriteMovies);
    }
    public void removeFromFavorites(RemoveFromFavoritesRequest request) {
        favoriteMovieRepository.deleteByUserIdAndMovieIdIn(request.getUserId(), request.getMovieIds());
    }

    public List<BasicDBObject> getWatchList(String userId) {
        List<WatchList> favoriteMovies = watchListRepository.findAllByUserId(userId);
        List<Integer> movieIds = favoriteMovies.stream().map(WatchList::getMovieId).toList();
        return movieRepository.getByIds(movieIds);
    }
    public List<WatchList> addToWatchList(AddToWatchListRequest request) {
        List<WatchList> watchList = new ArrayList<>();
        for (Integer movieId : request.getMovieIds()) {
            WatchList watch = WatchList.builder()
                    .movieId(movieId) // Assuming movieId is an integer
                    .userId(request.getUserId())
                    .createdAt(new Date())
                    .build();
            watchList.add(watch);
        }
        return watchListRepository.saveAll(watchList);
    }
    public void deleteFromWatchList(RemoveFromWatchListRequest request) {
        watchListRepository.deleteByUserIdAndMovieIdIn(request.getUserId(), request.getMovieIds());
    }


    public TmdbMovieListResponse getLatestTrailers(LatestTrailersRequest request) {
        return tmdbClient.getLatestTrailers(request);
    }

}
