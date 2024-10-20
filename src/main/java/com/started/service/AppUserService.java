package com.started.service;

import com.started.request.SignUpRequest;

public interface AppUserService {
    String signUp(SignUpRequest request) throws Exception;
}
