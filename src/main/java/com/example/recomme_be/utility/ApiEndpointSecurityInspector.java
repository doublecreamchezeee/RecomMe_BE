package com.example.recomme_be.utility;

import com.example.recomme_be.configuration.core.PublicEndpoint;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
//import io.swagger.v3.oas.models.PathItem.HttpMethod;

@Component
@RequiredArgsConstructor
public class ApiEndpointSecurityInspector {

    private final RequestMappingHandlerMapping requestHandlerMapping;

    @Getter
    private final List<String> publicGetEndpoints = new ArrayList<>();
    @Getter
    private final List<String> publicPostEndpoints = new ArrayList<>();

    @PostConstruct
    public void init() {
        final var handlerMethods = requestHandlerMapping.getHandlerMethods();
        handlerMethods.forEach((requestInfo, handlerMethod) -> {
            if (handlerMethod.hasMethodAnnotation(PublicEndpoint.class)) {
                final var httpMethod = requestInfo.getMethodsCondition().getMethods().iterator().next().asHttpMethod();
                final var apiPaths = requestInfo.getPathPatternsCondition().getPatternValues();

                if (httpMethod.equals(GET)) {
                    publicGetEndpoints.addAll(apiPaths);
                } else if (httpMethod.equals(POST)) {
                    publicPostEndpoints.addAll(apiPaths);
                }
            }
        });
    }

    public boolean isUnsecureRequest(@NonNull final HttpServletRequest request) {
        final var requestHttpMethod = HttpMethod.valueOf(request.getMethod());
        var unsecuredApiPaths = getUnsecuredApiPaths(String.valueOf(requestHttpMethod));
        unsecuredApiPaths = Optional.ofNullable(unsecuredApiPaths).orElseGet(ArrayList::new);

        return unsecuredApiPaths.stream().anyMatch(apiPath -> {
            boolean match = new AntPathMatcher().match("/api" + apiPath, request.getRequestURI());
            if (match) {
                System.out.println("Match found for path: " + apiPath);
            }
            return match;
        });
    }

    private List<String> getUnsecuredApiPaths(@NonNull final String httpMethodString) {
        HttpMethod httpMethod = HttpMethod.valueOf(httpMethodString.toUpperCase());

        if (httpMethod.equals(GET)) {
            return publicGetEndpoints;
        } else if (httpMethod.equals(POST)) {
            return publicPostEndpoints;
        }
        return Collections.emptyList();
    }
}
