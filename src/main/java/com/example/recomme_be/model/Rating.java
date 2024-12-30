package com.example.recomme_be.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "ratings")
public class Rating {
    @Id
    String id;
    String movieId;  // Reference to the movie
    String userId;   // User ID who gave the rating
    double rating;   // The rating given by the user
}