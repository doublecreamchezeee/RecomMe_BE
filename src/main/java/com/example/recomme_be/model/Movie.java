package com.example.recomme_be.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Movie {
    int id;
    String title;
    String overview;
    String release_date;
    String poster_path;
}