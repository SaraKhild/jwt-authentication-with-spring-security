package com.example.authentication_with_springboot.service.impl;

import com.example.authentication_with_springboot.configuration.security.JwtService;
import com.example.authentication_with_springboot.dto.LoginDto;
import com.example.authentication_with_springboot.dto.SignupDto;
import com.example.authentication_with_springboot.dto.UserProfileDto;
import com.example.authentication_with_springboot.mapper.UserMapper;
import com.example.authentication_with_springboot.model.User;
import com.example.authentication_with_springboot.repository.UserRepository;
import com.example.authentication_with_springboot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    @Override
    public User signup(SignupDto signupDto) {

        return this.userMapper.convertToEntity(signupDto);
    }

    @Override
    public String login(LoginDto loginDto) {

        // Makes the intention explicit: this token is for authentication request (not yet authenticated).
        // It was added to make the developer’s intent clearer.
        var authToken = UsernamePasswordAuthenticationToken.unauthenticated(
                loginDto.getUsername(), loginDto.getPassword());
        //Not: you can do like this but sets authenticated to false
//        var authToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword(), null);

        var authentication = this.authenticationManager.authenticate(authToken);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return jwtService.generateToken(userDetails.getUsername());
    }

    @Override
    public UserProfileDto getUserProfile(String token) {

        User user = userRepository.findByUsername(token).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "The user account has been deleted or inactivated"));

        return userMapper.converToUserProfileDto(user);
    }

    @Override
    public void logout(String token) {

        jwtService.blacklistUser(token);
    }

}