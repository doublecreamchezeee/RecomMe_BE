package com.example.recomme_be.service;

import com.example.recomme_be.dto.response.LLMNavigateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
@Service
public class NavigateService {
    private final RestTemplate restTemplate;

    @Value("${com.example.gemini.api.key}")
    private String geminiApiKey;

    @Value("${com.example.llm-api.base.url}")
    private String apiBaseUrl;

    public NavigateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public LLMNavigateResponse search(String query) {
        // Build URL with query parameters
        String url = UriComponentsBuilder.fromUriString(apiBaseUrl + "/navigate/")
                .queryParam("llm_api_key", geminiApiKey)
                .queryParam("query", query)
                .toUriString();

        // Call the API and return the response
        return restTemplate.postForObject(url, null, LLMNavigateResponse.class);
    }
}
