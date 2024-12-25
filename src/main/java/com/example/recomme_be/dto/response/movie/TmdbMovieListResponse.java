package com.example.recomme_be.dto.response.movie;

import com.example.recomme_be.model.Movie;
import com.mongodb.DBObject;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TmdbMovieListResponse {

    int page;
    List<Movie> results;
    int total_pages;
    int total_results;


}
