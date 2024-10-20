package com.started.request;

import lombok.Data;

@Data
public class TokenRefreshRequest {

    private String refreshToken;
}
