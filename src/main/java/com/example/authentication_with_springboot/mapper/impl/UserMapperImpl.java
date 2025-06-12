package com.example.authentication_with_springboot.mapper.impl;

import com.example.authentication_with_springboot.dto.SignupDto;
import com.example.authentication_with_springboot.dto.UserProfileDto;
import com.example.authentication_with_springboot.mapper.UserMapper;
import com.example.authentication_with_springboot.model.User;
import com.example.authentication_with_springboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User convertToEntity(SignupDto signupDto) {

        if (userRepository.findByUsername(signupDto.getUsername()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setName(signupDto.getName());
        user.setUsername(signupDto.getUsername());
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public UserProfileDto converToUserProfileDto(User user) {

        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setName(user.getName());

        return userProfileDto;
    }

}