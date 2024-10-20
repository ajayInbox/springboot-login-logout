package com.started.service;

import com.started.entity.RefreshToken;
import com.started.exception.TokenRefreshException;
import com.started.response.TokenRefreshResponse;

import java.util.Optional;

public interface RefreshTokenService {

    TokenRefreshResponse findByToken(String token) throws TokenRefreshException;

    public RefreshToken createRefreshToken(Long userId);

    public RefreshToken verifyExpiration(RefreshToken token);
}
