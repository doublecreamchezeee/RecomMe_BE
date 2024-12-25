package com.example.recomme_be.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
class Collection {
    int id;
    String name;
    String poster_path;
    String backdrop_path;
}

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
class Genre {
    int id;
    String name;
}

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
class ProductionCompany {
    int id;
    String logo_path;
    String name;
    String origin_country;
}

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
class ProductionCountry {
    String iso_3166_1;
    String name;
}

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
class SpokenLanguage {
    String english_name;
    String iso_639_1;
    String name;
}

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
class Credit {
    @Id
    BigInteger _id;
    Cast cast;
    Crew crew;
}

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    public static final String COLLECTION = "movies";
    public static final String NOW_PLAYING_COLLECTION = "movies_now_playing";
    public static final String POPULAR_COLLECTION = "movies_popular";
    public static final String TOP_RATED_COLLECTION = "movies_top_rated";
    public static final String TRENDING_DAY_COLLECTION = "movies_trending_day";
    public static final String TRENDING_WEEK_COLLECTION = "movies_trending_week";
    public static final String UPCOMING_COLLECTION = "movies_upcoming";

    @Id
    ObjectId _id;
    boolean adult;
    String backdrop_path;
    Collection belongs_to_collection;
    int budget;
    List<Genre> genres;
    String homepage;
    Integer imdb_id;
    List<String> origin_country;
    String original_language;
    String original_title;
    String overview;
    double popularity;
    String poster_path;
    List<ProductionCompany> production_companies;
    List<ProductionCountry> production_countries;
    String release_date;
    long revenue;
    int runtime;
    List<SpokenLanguage> spoken_languages;
    String status;
    String tagline;
    String title;
    boolean video;
    double vote_average;
    int vote_count;
    Credit credits;
}
