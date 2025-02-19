package com.example.recomme_be.model;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "movie_genres")
public class Genre {
    @Id
    private  ObjectId _id;
    private  Integer tmdb_id;
    private  Integer id;
    private  String name;
}
