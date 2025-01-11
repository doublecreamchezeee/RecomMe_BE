package com.example.recomme_be.service;

import com.example.recomme_be.client.LLMClient;
import com.example.recomme_be.dto.response.RetrieverResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RetrieverService {
    private final LLMClient llmClient;

    public RetrieverResponse search(String collectionName, String query, Integer amount, Double threshold) {
        return llmClient.search(collectionName, query, amount, threshold);
    }
}
