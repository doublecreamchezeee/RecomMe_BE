package com.example.recomme_be.repository;

import com.example.recomme_be.dto.request.movie.MoviePopularRequest;
import com.example.recomme_be.dto.request.movie.MovieSearchRequest;
import com.example.recomme_be.dto.request.movie.MoviesFilterRequest;
import com.mongodb.BasicDBObject;

import java.util.Collection;
import java.util.List;

public interface MovieRepository {
    List<BasicDBObject> getPopular(MoviePopularRequest request);

    List<BasicDBObject> getTrending(String timeWindow);

    BasicDBObject getDetail(Integer movieId);

    BasicDBObject getDetailWithObjectId(String objectId);

    List<BasicDBObject> search(MovieSearchRequest request);

    void updateMovieRating(String movieId, double newRating);

    List<BasicDBObject> getByIds(Collection<Integer> ids);
    List<BasicDBObject> getByObjectIds(Collection<String> objectIds);
    List<BasicDBObject> filter(MoviesFilterRequest request);


//    List<BasicDBObject> getMovies(List<String> movieIds);
}
