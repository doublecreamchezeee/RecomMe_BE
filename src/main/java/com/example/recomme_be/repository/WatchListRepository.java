package com.example.recomme_be.repository;


import com.example.recomme_be.model.WatchList;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;

public interface WatchListRepository extends MongoRepository<WatchList, ObjectId> {
    List<WatchList> findAllByUserId(String userId);
    void deleteByUserIdAndMovieIdIn(String userId, Collection<Integer> movieIds);

}
