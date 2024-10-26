package com.started.config;

import com.started.entity.AppUser;
import com.started.repository.AppUserRepository;
import com.started.service.RefreshTokenServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(AppUserDetailsService.class);

    final private AppUserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Email: {}", email);
        return userRepository.findByEmail(email)
                .map(AppUserDetails::new)
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found!!"));
    }
}
