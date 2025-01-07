package com.example.recomme_be.model;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;


@Data
@Builder
@Document(collection = "favorite_movie")
public class FavoriteMovie {
    @Id
    private ObjectId _id;

    @Field("movie_id")
    private int movieId;

    @Field("user_id")
    private String userId;

    @Builder.Default
    @Field("created_at")
    private Date createdAt = new Date(); // Default to current time
}
