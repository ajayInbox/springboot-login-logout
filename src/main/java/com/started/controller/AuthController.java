package com.started.controller;

import com.started.config.AppUserDetails;
import com.started.config.AppUserDetailsService;
import com.started.entity.RefreshToken;
import com.started.exception.TokenRefreshException;
import com.started.request.LoginRequest;
import com.started.request.TokenRefreshRequest;
import com.started.response.JwtResponse;
import com.started.response.TokenRefreshResponse;
import com.started.security.JwtUtils;
import com.started.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    final private AuthenticationManager manager;
    final private JwtUtils jwtUtils;
    final private RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request){
        AppUserDetails userDetails = doAuthenticate(request.getEmail(), request.getPassword());

        String token = jwtUtils.generateToken(userDetails);
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .toList();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        JwtResponse jwtResponse = JwtResponse.builder()
                .jwtToken(token)
                .userName(userDetails.getUsername())
                .tokenType("Bearer")
                .refreshToken(refreshToken.getToken())
                .build();
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

    private AppUserDetails doAuthenticate(String email, String password){
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(email, password);
        try{
            Authentication auth = manager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(auth);
            return (AppUserDetails) auth.getPrincipal();
        }catch(BadCredentialsException ex){
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken( @RequestBody TokenRefreshRequest request) throws TokenRefreshException {
        return new ResponseEntity<>(refreshTokenService.findByToken(request.getRefreshToken()), HttpStatus.OK);
//        String requestRefreshToken = request.getRefreshToken();
//
//        return refreshTokenService.findByToken(requestRefreshToken)
//                .map(refreshTokenService::verifyExpiration)
//                .map(RefreshToken::getAppUser)
//                .map(user -> {
//                    String token = jwtUtils.doGenerateToken(new HashMap<>(), user.getEmail());
//                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
//                })
//                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
//                        "Refresh token is not in database!"));
    }

}
