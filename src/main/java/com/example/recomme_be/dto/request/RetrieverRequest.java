package com.example.recomme_be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RetrieverRequest {
    private String geminiApiKey;
    private String collectionName;
    private String query;
    private Integer amount; // (Optional)
    private Double threshold; // (Optional)
}
