package com.example.recomme_be.controller;

import com.example.recomme_be.configuration.core.PublicEndpoint;
import com.example.recomme_be.dto.ApiResponse;
import com.example.recomme_be.dto.request.movie.CastsSearchRequest;
import com.example.recomme_be.dto.response.movie.TmdbCastListResponse;
import com.example.recomme_be.model.Cast;
import com.example.recomme_be.service.CastService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/casts")
@RequiredArgsConstructor
public class CastController {
    private final CastService castService;

    @PublicEndpoint
    @GetMapping
    public ApiResponse<TmdbCastListResponse> getAllCast(@RequestParam int page) {
        var response = castService.getAllCasts(page, 24);
        return ApiResponse.<TmdbCastListResponse>builder()
                .code(200)
                .message("Fetched cast successfully")
                .result(response)
                .build();
    }

    @PublicEndpoint
    @GetMapping("/{id}")
    public ApiResponse<Cast> getCastDetail(@PathVariable String id) {
        var response = castService.getCastById(id);
        return ApiResponse.<Cast>builder()
                .code(200)
                .message("Fetched cast successfully")
                .result(response)
                .build();
    }

    @PublicEndpoint
    @GetMapping("/query")
    public ApiResponse<List<Cast>> getAllCast(@RequestParam  String objectIds) {
        List<String> objectIdList = Arrays.stream(objectIds.split(","))
                .map(String::trim)
                .toList();
        var response = castService.getManyByObjectIds(objectIdList);
        return ApiResponse.<List<Cast>>builder()
                .code(200)
                .message("Fetched cast successfully")
                .result(response)
                .build();
    }

    @PublicEndpoint
    @GetMapping("/search")
    public ApiResponse<TmdbCastListResponse> searchMovies(
            @ModelAttribute CastsSearchRequest request, @RequestParam(defaultValue = "false") Boolean isAdvancedSearch) {

        if (request.getSearchTerm() == null || request.getSearchTerm().isBlank()) {
            throw new IllegalArgumentException("Please provide a search query.");
        }
        TmdbCastListResponse response = null;
        if (isAdvancedSearch) {
            response = castService.searchWithLLM(request);
        }
        else {
            response = castService.search(request);
        }
        return ApiResponse.<TmdbCastListResponse>builder()
                .code(200)
                .message("Fetched movies with keyword successfully")
                .result(response)
                .build();
    }
}
