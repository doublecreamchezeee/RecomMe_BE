package com.example.recomme_be.dto.response.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionResponse<T> {
    private String status;
    private T description;
}
