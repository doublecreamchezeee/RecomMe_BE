package com.example.recomme_be.dto.request.movie;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class MovieRequest {
    String title;
    String overview;
    String release_date;
    String poster_path;
}
