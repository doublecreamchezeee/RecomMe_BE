package com.example.recomme_be.dto.response.movie;

import com.example.recomme_be.model.Cast;
import com.mongodb.DBObject;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TmdbCastListResponse {
    int page;
    List<Cast> results;
    int total_pages;
    long total_results;

}