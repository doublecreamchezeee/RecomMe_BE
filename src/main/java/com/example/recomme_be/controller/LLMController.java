package com.example.recomme_be.controller;


import com.example.recomme_be.configuration.core.PublicEndpoint;
import com.example.recomme_be.dto.ApiResponse;
import com.example.recomme_be.dto.request.movie.MovieSearchRequest;
import com.example.recomme_be.dto.response.LLMNavigateResponse;
import com.example.recomme_be.service.NavigateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
                .message("Navigate successfully")
                .result(response)
                .build();
    }

    @PublicEndpoint
    @PostMapping("/rag")
    public ApiResponse<Object> chatbot(@RequestBody String query) {
        var response = navigateService.ask(query);
        return ApiResponse.<Object>builder()
                .code(200)
                .message("Ask successfully")
                .result(response)
                .build();
    }
}
