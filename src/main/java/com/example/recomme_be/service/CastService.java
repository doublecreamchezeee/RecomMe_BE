package com.example.recomme_be.service;

import com.example.recomme_be.dto.response.movie.TmdbCastListResponse;
import com.example.recomme_be.model.Cast;
import com.example.recomme_be.repository.CastRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class CastService {
    private final CastRepository castRepository;

    public TmdbCastListResponse getAllCasts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Cast> casts = castRepository.findAll(pageable);
        return TmdbCastListResponse.builder()
                .page(page)
                .results(casts.getContent())
                .total_pages(casts.getTotalPages())
                .total_results(casts.getTotalElements())
                .build(); // Fetch all casts
    }

    public Cast getCastById(String id) {
        return castRepository.findById(Integer.parseInt(id)); // Find cast by id
    }
}
