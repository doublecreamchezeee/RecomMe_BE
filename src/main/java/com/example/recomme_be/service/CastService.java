package com.example.recomme_be.service;

import com.example.recomme_be.dto.request.movie.CastsSearchRequest;
import com.example.recomme_be.dto.response.RetrieverResponse;
import com.example.recomme_be.dto.response.movie.TmdbCastListResponse;
import com.example.recomme_be.model.Cast;
import com.example.recomme_be.repository.CastRepository;
import com.example.recomme_be.repository.CastRepositoryExtend;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;


@Service
@AllArgsConstructor
public class CastService {
    private final CastRepository castRepository;
    private final CastRepositoryExtend castRepositoryExtend;
    private final  RetrieverService retrieverService;

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

    public List<Cast> getManyByObjectIds(Collection<String> objectIds) {
        List<ObjectId> objectIdList = objectIds.stream()
                .map(ObjectId::new)
                .toList();
        return castRepository.findAllBy_idIn(objectIdList);
    }

    public TmdbCastListResponse search(CastsSearchRequest request) {
        return TmdbCastListResponse.builder()
                .page(request.getPage())
                .results(castRepositoryExtend.search(request))
                .build();
    }

    public TmdbCastListResponse searchWithLLM(CastsSearchRequest request) {
        RetrieverResponse retrieverResponse = retrieverService.search(
                Cast.COLLECTION, request.getSearchTerm(), 10, 0.5
        );
        List<String> objectIdsString = retrieverResponse.getData().getResult();
        List<ObjectId> objectIds = objectIdsString.stream()
                .map(ObjectId::new).toList();
        return TmdbCastListResponse.builder()
                .page(request.getPage())
                .results(castRepository.findAllBy_idIn(objectIds))
                .build();
    }
}
