package com.started.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponse {

    private String jwtToken;
    private String userName;
    private String tokenType;
    private String refreshToken;

}
