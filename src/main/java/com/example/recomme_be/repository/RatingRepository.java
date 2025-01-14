package com.example.recomme_be.repository;

import com.example.recomme_be.model.Rating;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends MongoRepository<Rating, ObjectId> {
    List<Rating> findAllByUserId(String userId);
    Optional<Rating> findByUserIdAndMovieId(String userId, String movieId);
}
