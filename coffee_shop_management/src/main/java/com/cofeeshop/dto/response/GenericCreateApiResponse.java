package com.cofeeshop.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Builder
@Data
@Jacksonized
public class GenericCreateApiResponse {
    private Long id;
}
