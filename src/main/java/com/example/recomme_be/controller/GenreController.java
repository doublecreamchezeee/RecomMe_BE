package com.example.recomme_be.controller;


import com.example.recomme_be.configuration.core.PublicEndpoint;
import com.example.recomme_be.dto.ApiResponse;
import com.example.recomme_be.model.Genre;
import com.example.recomme_be.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            @RequestParam String objectIds) {
        List<String> objectIdList = Arrays.stream(objectIds.split(","))
                .map(String::trim)
                .toList();
        var response = genreService.getManyByObjectsId(objectIdList);
        return ApiResponse.<List<Genre>>builder()
                .code(200)
                .message("Fetched genres successfully")
                .result(response)
                .build();
    }
}
