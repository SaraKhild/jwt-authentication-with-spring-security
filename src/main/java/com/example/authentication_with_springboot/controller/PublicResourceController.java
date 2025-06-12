package com.example.authentication_with_springboot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public-resource")
public class PublicResourceController {

    @GetMapping
    public ResponseEntity<String> publicResource() {

        return new ResponseEntity<>("you are in public area", HttpStatus.OK);
    }
}
