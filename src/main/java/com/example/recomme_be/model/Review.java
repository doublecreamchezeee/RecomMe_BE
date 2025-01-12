package com.example.recomme_be.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "reviews")
public class Review {
    @Id
    String id;
    String movieId;  // Reference to the movie
    String userId;   // User ID who wrote the review
    String content;  // Review content
    String author;
    LocalDateTime time;
}