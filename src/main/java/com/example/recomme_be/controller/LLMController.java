package com.example.recomme_be.controller;


import com.example.recomme_be.configuration.core.PublicEndpoint;
import com.example.recomme_be.dto.ApiResponse;
import com.example.recomme_be.dto.request.movie.MovieSearchRequest;
import com.example.recomme_be.dto.response.LLMNavigateResponse;
import com.example.recomme_be.service.NavigateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/llm")
@RequiredArgsConstructor
public class LLMController {

    private final NavigateService navigateService;

    @PublicEndpoint
    @GetMapping("/navigate")
    public ApiResponse<LLMNavigateResponse> getPopularMovies(
            @ModelAttribute MovieSearchRequest movieSearchRequest) {
        if (movieSearchRequest.getQuery() == null || movieSearchRequest.getQuery().isBlank()) {
            throw new IllegalArgumentException("Please provide a search query.");
        }
        var response = navigateService.navigate(movieSearchRequest.getQuery());
        return ApiResponse.<LLMNavigateResponse>builder()
                .code(200)
                .message("Fetched popular movies successfully")
                .result(response)
                .build();
    }
}
