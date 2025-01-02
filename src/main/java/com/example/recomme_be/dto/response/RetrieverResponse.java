package com.example.recomme_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetrieverResponse {
    private int status;
    private RetrieverData data;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RetrieverData {
        private List<String> result;
    }
}