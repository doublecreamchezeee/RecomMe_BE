package com.example.recomme_be.repository;

import com.example.recomme_be.dto.request.movie.MoviePopularRequest;
import com.example.recomme_be.dto.request.movie.MovieSearchRequest;
import com.mongodb.DBObject;

import java.util.Collection;
import java.util.List;

public interface MovieRepository {
    List<DBObject> getPopular(MoviePopularRequest request);

    List<DBObject> getTrending(String timeWindow);

    DBObject getDetail(Integer movieId);

    DBObject getDetailWithObjectId(String objectId);

    List<DBObject> search(MovieSearchRequest request);

    void updateMovieRating(String movieId, double newRating);

    List<DBObject> getByIds(Collection<Integer> ids);

//    List<DBObject> getMovies(List<String> movieIds);
}
