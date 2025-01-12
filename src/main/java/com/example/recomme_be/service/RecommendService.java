package com.example.recomme_be.service;

import com.example.recomme_be.dto.request.movie.MovieSearchRequest;
import com.example.recomme_be.dto.response.movie.TmdbMovieListResponse;
import com.example.recomme_be.model.Genre;
import com.example.recomme_be.model.Movie;
import com.example.recomme_be.model.SearchHistory;
import com.example.recomme_be.repository.SearchHistoryRepository;
import com.mongodb.DBObject;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RecommendService {
    private final MovieService movieService;
    private final SearchHistoryRepository searchHistoryRepository;

    public TmdbMovieListResponse getRecommendedMoviesByHistory(String userId) {
        List<SearchHistory> histories = searchHistoryRepository.findTop5ByUserIdOrderByTimestampDesc(userId);

        if (histories.isEmpty()) {
            return TmdbMovieListResponse.builder()
                    .page(1)
                    .results(List.of()) // No recommendations
                    .total_pages(0)
                    .total_results(0)
                    .build();
        }

        List<String> contextualQueries = histories.stream()
                .map(SearchHistory::getQuery)
                .distinct() // Avoid duplicate queries
                .map(query -> String.format("%s", query)) // Add context to each query
                .toList();

        String combinedQuery = String.join(" , ", contextualQueries); // Join with a comma for context

        // Step 4: Build a MovieSearchRequest using the combined query
        MovieSearchRequest request = new MovieSearchRequest();
        request.setQuery(combinedQuery);
        request.setPage(1); // First page of recommendations
        request.setIncludeAdult(false); // Default settings, adjust as necessary

        // Step 5: Call the LLM-based movie search method to get recommendations

        // Step 6: Return the recommendations
        return movieService.searchMoviesWithLLM(request);
    }

    public TmdbMovieListResponse getRecommendedMoviesByDetail(Integer movieId) {
        // Step 1: Fetch movie details by movie ID
        DBObject movie = (DBObject) movieService.getDetailMovie(movieId);

        if (movie == null) {
            // If no movie is found, return an empty response
            return TmdbMovieListResponse.builder()
                    .page(1)
                    .results(List.of()) // No recommendations
                    .total_pages(0)
                    .total_results(0)
                    .build();
        }

        String combinedQuery;

        // Step 2: Extract genres directly from the raw object
        Object genresObject = movie.get("genres");
        combinedQuery = extractNamesFromArray(genresObject, "name");

        if (combinedQuery.isEmpty()) {
            // Step 3: Fallback to production companies if genres are null or empty
            Object productionCompaniesObject = movie.get("production_companies");
            combinedQuery = extractNamesFromArray(productionCompaniesObject, "name");

            if (combinedQuery.isEmpty()) {
                // Fallback to movie title if both genres and production companies are null or empty
                combinedQuery = Objects.toString(movie.get("title"), ""); // Use the movie title or empty string
            }
        }

        // Step 4: Build a MovieSearchRequest using the combined query
        MovieSearchRequest request = new MovieSearchRequest();
        request.setQuery(combinedQuery);
        request.setPage(1); // First page of recommendations
        request.setIncludeAdult(false); // Default settings

        // Step 5: Call the movie service to get recommendations
        return movieService.searchMoviesWithLLM(request);
    }

    // Helper method to extract names from an array-like object
    private String extractNamesFromArray(Object arrayObject, String key) {
        if (arrayObject instanceof List) {
            List<?> list = (List<?>) arrayObject;
            return list.stream()
                    .map(item -> {
                        if (item instanceof DBObject) {
                            return ((DBObject) item).get(key);
                        }
                        return null;
                    })
                    .filter(Objects::nonNull) // Remove nulls
                    .map(Object::toString) // Convert to String
                    .collect(Collectors.joining(" , ")); // Combine with commas
        }
        return "";
    }


}
