package com.example.authentication_with_springboot.service;

import com.example.authentication_with_springboot.dto.LoginDto;
import com.example.authentication_with_springboot.dto.SignupDto;
import com.example.authentication_with_springboot.dto.UserProfileDto;
import com.example.authentication_with_springboot.model.User;

public interface UserService {

    User signup(SignupDto signupDto);

    String login(LoginDto loginDto);

    UserProfileDto getUserProfile(String token);

    void logout(String token);

}