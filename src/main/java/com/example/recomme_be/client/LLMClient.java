package com.example.recomme_be.client;


import com.example.recomme_be.dto.response.LLMNavigateResponse;
import com.example.recomme_be.dto.response.RetrieverResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class LLMClient {
    private final RestTemplate restTemplate;

    @Value("${com.example.gemini.api.key}")
    private String geminiApiKey;

    @Value("${com.example.llm-api.base.url}")
    private String apiBaseUrl;

    public RetrieverResponse search(String collectionName, String query, Integer amount, Double threshold) {
        // Build URL with query parameters
        String url = UriComponentsBuilder.fromUriString(apiBaseUrl + "/retriever/")
                .queryParam("llm_api_key", geminiApiKey)
                .queryParam("collection_name", collectionName)
                .queryParam("query", query)
                .queryParam("amount", amount != null ? amount : 25) // Default value for amount
                .queryParam("threshold", threshold != null ? threshold : 0.5) // Default value for threshold
                .toUriString();

        // Call the API and return the response
        return restTemplate.getForObject(url, RetrieverResponse.class);
    }

    public LLMNavigateResponse navigate(String query) {
        // Build URL with query parameters
        String url = UriComponentsBuilder.fromUriString(apiBaseUrl + "/navigate/")
                .queryParam("llm_api_key", geminiApiKey)
                .queryParam("query", query)
                .toUriString();

        // Call the API and return the response
        return restTemplate.postForObject(url, null, LLMNavigateResponse.class);
    }

}
