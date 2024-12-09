package com.example.recomme_be.controller;

import com.example.recomme_be.dto.ApiResponse;
import com.example.recomme_be.dto.request.movie.MovieRequest;
import com.example.recomme_be.dto.response.movie.MovieResponse;
import com.example.recomme_be.mapper.MovieMapper;
import com.example.recomme_be.model.Movie;
import com.example.recomme_be.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/popular")
    public ApiResponse<List<MovieResponse>> getPopularMovies() {
        List<MovieResponse> popularMovies = movieService.getPopularMovies();

        if (popularMovies.isEmpty()) {
            return ApiResponse.<List<MovieResponse>>builder()
                    .code(200)
                    .message("No popular movies found")
                    .result(popularMovies)
                    .build();
        } else {
            return ApiResponse.<List<MovieResponse>>builder()
                    .code(200)
                    .message("Fetched popular movies successfully")
                    .result(popularMovies)
                    .build();
        }
    }
}