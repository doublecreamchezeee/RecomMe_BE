package com.example.recomme_be.dto.response.movie;

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
    List<MovieResponse> results;
    int total_pages;
    int total_results;

    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class MovieResponse {
        String backdrop_path;
        int id;
        String title;
        String original_title;
        String overview;
        String poster_path;
        String media_type;
        boolean adult;
        String original_language;
        List<Integer> genre_ids;
        double popularity;
        String release_date;
        boolean video;
        double vote_average;
        int vote_count;
    }
}
