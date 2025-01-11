package com.example.recomme_be.repository;

import com.example.recomme_be.model.Cast;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;

public interface CastRepository extends MongoRepository<Cast, ObjectId> {
    Cast findById(int id);
    List<Cast> findAllBy_idIn(Collection<ObjectId> objectIds);

    //    Page<Cast> findAll(Pageable pageable);
}
