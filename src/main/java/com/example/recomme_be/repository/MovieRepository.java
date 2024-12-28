package com.example.recomme_be.repository;

import com.example.recomme_be.dto.request.movie.MoviePopularRequest;
import com.example.recomme_be.dto.request.movie.MovieSearchRequest;
import com.mongodb.DBObject;

import java.util.List;

public interface MovieRepository {
    List<DBObject> getPopular(MoviePopularRequest request);

    List<DBObject> getTrending(String timeWindow);

    DBObject getDetail(String movieId);

    List<DBObject> search(MovieSearchRequest request);
}
