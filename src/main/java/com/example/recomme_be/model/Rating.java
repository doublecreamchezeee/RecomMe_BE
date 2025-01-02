package com.example.recomme_be.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@Document("ratings")
public class Rating {
    @Id
    ObjectId _id;

    String userId;
    String movieId;
    double rating;
}
