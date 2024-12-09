package com.example.recomme_be.dto.response.movie;

import com.example.recomme_be.model.Movie;
import lombok.Data;

import java.util.List;

@Data
public class TmdbMovieResponse {
    private List<Movie> results;
}