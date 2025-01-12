package com.example.recomme_be.repository;

import com.example.recomme_be.dto.request.movie.CastsSearchRequest;
import com.example.recomme_be.model.Cast;

import java.util.List;

public interface CastRepositoryExtend  {
    List<Cast> search(CastsSearchRequest request);
    //    Page<Cast> findAll(Pageable pageable);
}
