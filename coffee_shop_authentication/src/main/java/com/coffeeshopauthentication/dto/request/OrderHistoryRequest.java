package com.coffeeshopauthentication.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Data
public class OrderHistoryRequest {

    private String customerName;
    
}
