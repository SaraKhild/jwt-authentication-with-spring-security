package com.example.authentication_with_springboot.controller;

import com.example.authentication_with_springboot.dto.UserProfileDto;
import com.example.authentication_with_springboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserProfileDto> getUser(Authentication authentication) {

        return ResponseEntity.ok(userService.getUserProfile(authentication.getName()));
    }

}
