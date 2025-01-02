package com.example.recomme_be.controller;

import com.example.recomme_be.configuration.core.PublicEndpoint;
import com.example.recomme_be.dto.ApiResponse;
import com.example.recomme_be.dto.request.movie.MoviePopularRequest;
import com.example.recomme_be.dto.response.movie.TmdbCastListResponse;
import com.example.recomme_be.dto.response.movie.TmdbMovieListResponse;
import com.example.recomme_be.service.CastService;
import com.example.recomme_be.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/casts")
@RequiredArgsConstructor
public class CastController {
    private final CastService castService;

    @PublicEndpoint
    @GetMapping
    public ApiResponse<TmdbCastListResponse> getAllCast(@RequestParam int page) {
        var response = castService.getAllCasts(page, 20);
        return ApiResponse.<TmdbCastListResponse>builder()
                .code(200)
                .message("Fetched cast successfully")
                .result(response)
                .build();
    }

}
