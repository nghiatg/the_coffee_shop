package com.cofeeshoporder.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Builder
@Data
@Jacksonized
public class CoffeeShopClientResponse<T> {

    T data;
    
    String error;

}
