package com.example.recomme_be.repository;

import com.example.recomme_be.model.SearchHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SearchHistoryRepository extends MongoRepository<SearchHistory, String> {
    List<SearchHistory> findByUserIdOrderByTimestampDesc(String userId);
}
