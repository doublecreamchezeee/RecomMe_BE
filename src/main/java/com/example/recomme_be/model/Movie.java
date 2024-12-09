package com.example.recomme_be.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    int id;
    String title;
    String overview;
    String release_date;
    String poster_path;
}