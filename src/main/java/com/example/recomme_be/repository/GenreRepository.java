package com.example.recomme_be.repository;

import com.example.recomme_be.model.Genre;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;


public interface GenreRepository extends MongoRepository<Genre, ObjectId> {
    List<Genre> findAllBy_idIn(Collection<ObjectId> objectIds);
}
