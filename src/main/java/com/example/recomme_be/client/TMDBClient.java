package com.example.recomme_be.client;

import com.example.recomme_be.dto.request.movie.LatestTrailerRequest;
import com.example.recomme_be.dto.response.movie.DetailTmdbMovieResponse;
import com.example.recomme_be.dto.response.movie.TmdbMovieListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;



@Component
@RequiredArgsConstructor
@Slf4j
public class TMDBClient {
    private final RestTemplate restTemplate;

    @Value("${tmdb.base-url}")
    private String baseUrl;

    @Value("${tmdb.api-key}")
    private String apiKey;


    @Value("${tmdb.read-access-token}")
    private String readAccessToken;



    private static final  String API_KEY_PARAM = "api_key";

    private HttpHeaders createAuthorizationHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + readAccessToken);  // Set the Bearer token
        return headers;
    }


    public TmdbMovieListResponse getLatestTrailer(LatestTrailerRequest request) {
        String url = String.format("%s/movie/upcoming", baseUrl);
        try {


            // Create an HttpEntity with the headers and request body
            HttpEntity<LatestTrailerRequest> entity = new HttpEntity<>(request, createAuthorizationHeaders());

            // Send the POST request
            TmdbMovieListResponse response = restTemplate.exchange(url, HttpMethod.GET, entity, TmdbMovieListResponse.class).getBody();

            if (response != null && response.getResults() != null) {
                return response;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Error fetching latest trailers: {}", e.getMessage());
            return null;
        }
    }



}
