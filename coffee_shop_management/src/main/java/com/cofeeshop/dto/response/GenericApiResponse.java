package com.cofeeshop.dto.response;

import com.cofeeshop.config.Constants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Builder
@Data
@Jacksonized
public class GenericApiResponse {
    Object data;
    Object error;

    public static GenericApiResponse buildDefaultSuccessResponse() {
        DefaultResponse defaultResponse = new DefaultResponse(Constants.DEFAULT_SUCCESS_RESPONSE);
        return GenericApiResponse.builder().data(defaultResponse).build();
    }

    public static GenericApiResponse buildErrorResponse(RuntimeException e) {
        DefaultResponse errResponse = new DefaultResponse(e.getMessage());
        return GenericApiResponse.builder().error(errResponse).build();
    }

    @AllArgsConstructor
    @Data
    static class DefaultResponse {
        String response;
    }
}
