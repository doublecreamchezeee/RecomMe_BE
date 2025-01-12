package com.example.recomme_be.dto.response.movie;

import com.mongodb.BasicDBObject;
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
    List<BasicDBObject> results;
    int total_pages;
    int total_results;


}
