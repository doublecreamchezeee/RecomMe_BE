package com.example.recomme_be.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "cast")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cast {
    @Id
    ObjectId _id;
    boolean adult;
    String biography;
    String deathday;
    int gender;
    String homepage;
    int id;
    String known_for_department;
    Object movie_credits;
    String name;
    double popularity;
    String profile_path;
    int tmdb_id;
    String birthday;
}
