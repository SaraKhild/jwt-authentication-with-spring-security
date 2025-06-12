package com.example.authentication_with_springboot.controller;

import com.example.authentication_with_springboot.configuration.security.JwtService;
import com.example.authentication_with_springboot.dto.LoginDto;
import com.example.authentication_with_springboot.dto.SignupDto;
import com.example.authentication_with_springboot.model.User;
import com.example.authentication_with_springboot.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupDto signupDto) {

        User user = userService.signup(signupDto);

        return new ResponseEntity<>(user.getUsername() + " Signup Successfully ", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {

        String token = userService.login(loginDto);

        return new ResponseEntity<>(Map.of("token", token), HttpStatus.OK);

    }

    /*
    To implement a logout API with JWT-based authentication, you need to understand that:
    JWT is stateless, meaning once issued, it can’t be revoked unless you track it somehow — like with a token blacklist, expiration, or client-side deletion.
    On logout, store the token in the blacklist until it expires.
    */
    @PostMapping("/logout") // TODO:
    public ResponseEntity<?> logout(HttpServletRequest request) {

        if(jwtService.extractTokenFromRequest(request).isPresent()){
            log.info("*****" + String.valueOf(jwtService.extractTokenFromRequest(request).isPresent()));
            userService.logout(jwtService.extractTokenFromRequest(request).get());
            return ResponseEntity.ok("Successfully logged out.");
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No JWT token found.");
        }

        // ***************************another way****************************************
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            userService.logout(authHeader.substring(7));
//            return ResponseEntity.ok("Successfully logged out.");
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No JWT token found.");
//        }
    }
}
