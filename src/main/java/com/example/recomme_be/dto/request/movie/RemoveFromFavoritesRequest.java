package com.example.recomme_be.dto.request.movie;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class RemoveFromFavoritesRequest {
    @JsonIgnore
    private String userId;
    @NotEmpty(message = "movieIds must not be empty.")
    private Collection<Integer> movieIds;
}
