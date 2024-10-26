package com.started.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RegularController {

    @GetMapping("/regular/hello")
    public ResponseEntity<?> helloRegular(){
        return new ResponseEntity<>("Hello From Regular!!", HttpStatus.OK);
    }
}
