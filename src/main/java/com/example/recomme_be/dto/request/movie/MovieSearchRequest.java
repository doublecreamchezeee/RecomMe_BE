package com.example.recomme_be.dto.request.movie;

import lombok.Data;

@Data
public class MovieSearchRequest {
    private String query; // Required field
    private Boolean includeAdult = false; // Defaults to false
    private String language = "en-US"; // Defaults to en-US
    private String primaryReleaseYear;
    private Integer page = 1; // Defaults to 1
    private String region;
    private String year;
}
