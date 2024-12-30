package com.example.recomme_be.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "search_history")
public class SearchHistory {
    @Id
    private String id;

    private String userId;
    private String query;
    private LocalDateTime timestamp;
}
