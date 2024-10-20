package com.started.controller;

import com.started.request.SignUpRequest;
import com.started.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AppUserController {

    final private AppUserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest request) throws Exception {
        String msg = userService.signUp(request);
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }
}
