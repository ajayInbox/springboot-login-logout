package com.started.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class TokenRefreshResponse {

    private String token;
    private String refreshToken;
    private String tokenType;

    public TokenRefreshResponse(String token, String refreshToken){
        this.token = token;
        this.refreshToken = refreshToken;
        this.tokenType = "Bearer";
    }

}
