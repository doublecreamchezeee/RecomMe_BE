package com.example.recomme_be.repository;

import com.example.recomme_be.model.FavoriteMovie;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;

public interface FavoriteMovieRepository extends MongoRepository<FavoriteMovie, ObjectId> {
    // Custom query method to find movies by a list of user IDs
    List<FavoriteMovie> findAllByUserId(String userId);
    void deleteByUserIdAndMovieIdIn(String userId, Collection<Integer> movieIds);
}
