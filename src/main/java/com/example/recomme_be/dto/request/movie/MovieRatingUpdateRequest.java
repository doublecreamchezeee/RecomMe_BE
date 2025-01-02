package com.example.recomme_be.dto.request.movie;

import lombok.Data;

@Data
public class MovieRatingUpdateRequest {
    private String movieId;    // Movie ID to identify the movie
    private double rating;
    private String userId;
}
