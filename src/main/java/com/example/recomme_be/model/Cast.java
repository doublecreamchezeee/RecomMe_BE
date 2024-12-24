package com.example.recomme_be.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cast {
    boolean adult;
    int gender;
    int id;
    String knownForDepartment;
    String name;
    String originalName;
    double popularity;
    String profilePath;
    int castId;
    String character;
    String creditId;
    int order;
}
