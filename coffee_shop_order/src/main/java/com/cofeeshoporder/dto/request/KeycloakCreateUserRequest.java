package com.cofeeshoporder.dto.request;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
public class KeycloakCreateUserRequest {
    
    private String enabled;

    private String username;

    private List<String> groups;

    private List<Credentials> credentials;
    
    @Data
    @Jacksonized
    @Builder
    public static class Credentials {
        
        private String type;
        
        private String value;
        
        private boolean temporary;
    
    }
    
}
