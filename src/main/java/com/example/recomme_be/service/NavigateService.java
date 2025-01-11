package com.example.recomme_be.service;

import com.example.recomme_be.client.LLMClient;
import com.example.recomme_be.dto.response.LLMNavigateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NavigateService {

    private final LLMClient llmClient;

    public LLMNavigateResponse navigate(String query) {
        return llmClient.navigate(query);
    }
}
