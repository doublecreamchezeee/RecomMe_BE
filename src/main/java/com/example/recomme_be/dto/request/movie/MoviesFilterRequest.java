package com.example.recomme_be.dto.request.movie;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MoviesFilterRequest {
    private Integer page = 1;
    private Integer pageSize = 10;
    private String objectIds;
    private String genreIds;
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String fromDate;

//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String toDate;
    private Double fromScore;
    private Double toScore;
}
