package com.example.recomme_be.controller;


import com.example.recomme_be.configuration.core.PublicEndpoint;
import com.example.recomme_be.dto.ApiResponse;
import com.example.recomme_be.dto.request.movie.GenresFilterRequest;
import com.example.recomme_be.model.Genre;
import com.example.recomme_be.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;


    @GetMapping
    @PublicEndpoint
    public ApiResponse<List<Genre>> getGenres(
            @ModelAttribute GenresFilterRequest request) {
        var response = genreService.getMany(request);
        return ApiResponse.<List<Genre>>builder()
                .code(200)
                .message("Fetched genres successfully")
                .result(response)
                .build();
    }
}
