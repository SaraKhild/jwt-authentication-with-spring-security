package com.example.authentication_with_springboot.mapper;

import com.example.authentication_with_springboot.dto.SignupDto;
import com.example.authentication_with_springboot.dto.UserProfileDto;
import com.example.authentication_with_springboot.model.User;

public interface UserMapper {

    User convertToEntity(SignupDto signupDto);

    UserProfileDto converToUserProfileDto(User user);

}