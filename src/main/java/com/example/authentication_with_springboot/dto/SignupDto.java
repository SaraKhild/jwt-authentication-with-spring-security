package com.example.authentication_with_springboot.dto;

import lombok.Data;

@Data
public class SignupDto {

    private String name;
    private String username;
    private String password;
}