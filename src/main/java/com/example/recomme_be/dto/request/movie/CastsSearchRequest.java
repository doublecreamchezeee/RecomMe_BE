package com.example.recomme_be.dto.request.movie;

import lombok.Data;

@Data
public class CastsSearchRequest {
    private String searchTerm; // Required field
    private Integer page = 1; // Defaults to 1
}
