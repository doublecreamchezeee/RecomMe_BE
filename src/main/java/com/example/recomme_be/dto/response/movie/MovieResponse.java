package com.example.recomme_be.dto.response.movie;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieResponse {
    int id;
    String title;
    String overview;
    String release_date;
    String poster_path;
}
