package com.example.recomme_be.repository;

import com.example.recomme_be.model.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RatingRepository extends MongoRepository<Rating, String> {
    Rating findByMovieIdAndUserId(String movieId, String userId);
}