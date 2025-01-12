package com.example.recomme_be.repository.impl;

import com.example.recomme_be.dto.request.movie.CastsSearchRequest;
import com.example.recomme_be.model.Cast;
import com.example.recomme_be.repository.CastRepositoryExtend;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CastRepositoryExtendImpl implements CastRepositoryExtend {
    private final MongoTemplate mongoTemplate;

    @Override
    public List<Cast> search(CastsSearchRequest request) {
        if (request.getSearchTerm() == null || request.getSearchTerm().trim().isEmpty()) {
            return List.of();
        }

        Query query = new Query();

        Criteria bioCriteria = Criteria.where("biography").regex(request.getSearchTerm(), "i");
        Criteria nameCriteria = Criteria.where("name").regex(request.getSearchTerm(), "i");
        Criteria placeOfBirthCriteria = Criteria.where("place_of_birth").regex(request.getSearchTerm(), "i");
        Criteria knownForDeptCriteria = Criteria.where("known_for_department").regex(request.getSearchTerm(), "i");

        query.addCriteria(new Criteria().orOperator(bioCriteria, nameCriteria, placeOfBirthCriteria, knownForDeptCriteria));

        return mongoTemplate.find(query, Cast.class, Cast.COLLECTION);
    }

}
