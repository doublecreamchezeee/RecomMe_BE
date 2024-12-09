package com.example.recomme_be.dto.response.movie;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DetailTmdbMovieResponse {
    boolean adult;
    String backdrop_path;
    CollectionResponse belongs_to_collection;
    int budget;
    List<Genre> genres;
    String homepage;
    int id;
    String imdb_id;
    List<String> origin_country;
    String original_language;
    String original_title;
    String overview;
    double popularity;
    String poster_path;
    List<ProductionCompanyResponse> production_companies;
    List<ProductionCountryResponse> production_countries;
    String release_date;
    long revenue;
    int runtime;
    List<SpokenLanguageResponse> spoken_languages;
    String status;
    String tagline;
    String title;
    boolean video;
    double vote_average;
    int vote_count;

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class CollectionResponse {
        int id;
        String name;
        String poster_path;
        String backdrop_path;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Genre {
        int id;
        String name;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ProductionCompanyResponse {
        int id;
        String logo_path;
        String name;
        String origin_country;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ProductionCountryResponse {
        String iso_3166_1;
        String name;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class SpokenLanguageResponse {
        String english_name;
        String iso_639_1;
        String name;
    }
}
