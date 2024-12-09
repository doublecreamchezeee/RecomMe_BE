package com.example.recomme_be.mapper;

import com.example.recomme_be.dto.request.movie.MovieRequest;
import com.example.recomme_be.dto.response.movie.MovieResponse;
import com.example.recomme_be.model.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface MovieMapper {
//    @Mappings({
//            @Mapping(target = "id", ignore = true),  // Ignore the id field since MovieRequest doesn't have it
//            @Mapping(target = "title", source = "title"),  // Mapping title to title
//            @Mapping(target = "overview", source = "overview"),  // Mapping overview to overview
//            @Mapping(target = "release_date", source = "release_date"),  // Mapping releaseDate to releaseDate
//            @Mapping(target = "poster_path", source = "poster_path")  // Mapping posterPath to posterPath
//    })
//    Movie toMovie(MovieRequest movieRequest);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "overview", source = "overview")
    @Mapping(target = "release_date", source = "release_date")
    @Mapping(target = "poster_path", source = "poster_path")
    MovieResponse toMovieResponse(Movie movie);
}