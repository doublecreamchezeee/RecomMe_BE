package com.example.recomme_be.repository;

import com.example.recomme_be.model.Cast;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CastRepository extends MongoRepository<Cast, ObjectId> {
    Cast findById(int id);

//    Page<Cast> findAll(Pageable pageable);
}
