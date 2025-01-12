package com.example.recomme_be.controller;

import com.example.recomme_be.dto.ApiResponse;
import com.example.recomme_be.dto.response.movie.TmdbMovieListResponse;
import com.example.recomme_be.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recommend")
@RequiredArgsConstructor
public class RecommendController {
    private final RecommendService recommendService;

    @GetMapping("/history")
    public ApiResponse<TmdbMovieListResponse> getRecommendByHistory(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        TmdbMovieListResponse response = recommendService.getRecommendedMoviesByHistory(userId);
        return ApiResponse.<TmdbMovieListResponse>builder()
                .code(200)
                .message("Fetched rcm movies by history successfully")
                .result(response)
                .build();
    }

    @GetMapping("/movie/{movieId}")
    public ApiResponse<TmdbMovieListResponse> getRecommendByMovieDetail(@PathVariable Integer movieId) {
        TmdbMovieListResponse response = recommendService.getRecommendedMoviesByDetail(movieId);
        return ApiResponse.<TmdbMovieListResponse>builder()
                .code(200)
                .message("Fetched rcm movies by history successfully")
                .result(response)
                .build();
    }

}
