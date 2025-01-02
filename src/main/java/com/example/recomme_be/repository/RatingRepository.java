package com.example.recomme_be.repository;

import com.example.recomme_be.model.Rating;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RatingRepository extends MongoRepository<Rating, ObjectId> {
    List<Rating> findAllByUserId(String userId);
}
