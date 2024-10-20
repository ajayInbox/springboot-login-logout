package com.started.service;

import com.started.entity.AppUser;
import com.started.repository.AppUserRepository;
import com.started.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService{

    final private AppUserRepository userRepository;
    final private PasswordEncoder encoder;

    @Override
    public String signUp(SignUpRequest request) throws Exception {
        Optional<AppUser> optionalUser = userRepository.findByEmail(request.getEmail());
        if(optionalUser.isPresent()){
            throw new Exception("User Already Present!!");
        }
        AppUser newUser = AppUser.builder()
                .email(request.getEmail())
                .userName(request.getUserName())
                .password(encoder.encode(request.getPassword()))
                .role("ADMIN")
                .isEnabled(true)
                .build();
        AppUser savedUser = userRepository.save(newUser);
        return "User saved with id: "+savedUser.getId();
    }
}
