package com.example.recomme_be.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class LLMNavigateResponse {
    private int status;
    private Data data;

    @Getter
    @Setter
    public class Data {
        private  String route;
        private Map<String, Object> params;
        private Object metadata;
        @JsonProperty("is_success")
        private boolean isSuccess;
    }


}
