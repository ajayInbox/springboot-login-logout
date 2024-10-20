package com.started.service;

import com.started.entity.AppUser;
import com.started.entity.RefreshToken;
import com.started.exception.TokenRefreshException;
import com.started.repository.AppUserRepository;
import com.started.repository.RefreshTokenRepository;
import com.started.response.TokenRefreshResponse;
import com.started.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenServiceImpl.class);
    @Value("${app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    private final AppUserRepository userRepository;

    private final JwtUtils jwtUtils;

    @Override
    public TokenRefreshResponse findByToken(String token) throws TokenRefreshException {
        logger.info("finding refresh token based to token field {}", token);
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        if(refreshToken.isEmpty()){
            throw new TokenRefreshException("Refresh token not found!!");
        }
        RefreshToken rt = refreshToken.get();
        verifyExpiration(rt);
        AppUser user = rt.getAppUser();
        String accessToken = jwtUtils.doGenerateToken(new HashMap<>(), user.getEmail());
        return new TokenRefreshResponse(accessToken, rt.getToken());
    }

    @Override
    public RefreshToken createRefreshToken(Long userId) {
        AppUser user = userRepository.findById(userId).get();
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByAppUser(user);
        if(optionalRefreshToken.isPresent()){
            Long idToBeDeleted = optionalRefreshToken.get().getRefreshTokenId();
           String msg = deleteById(idToBeDeleted);
           logger.info("{}: {}", idToBeDeleted, msg);

        }
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setAppUser(user);
        refreshToken.setExpiry(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        try{
            if (token.getExpiry().compareTo(Instant.now()) < 0) {
                refreshTokenRepository.delete(token);
                throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        }catch (TokenRefreshException e){
            e.getStackTrace();
        }
        return token;
    }

    public int deleteByUser(AppUser user) {
        return refreshTokenRepository.deleteByAppUser(user);
    }

    @Transactional
    public String deleteById(Long id) {
        refreshTokenRepository.deleteById(id);
        return "Deleted Successfully.";
    }
}
