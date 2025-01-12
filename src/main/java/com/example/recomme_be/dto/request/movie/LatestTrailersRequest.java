package com.example.recomme_be.dto.request.movie;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LatestTrailersRequest {
    private String language = "en-US"; // Defaults to en-US
    private String region;
    private Integer page = 1; // Defaults to 1
}
