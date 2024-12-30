package com.example.recomme_be.repository;

import com.example.recomme_be.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByMovieId(String movieId);
}