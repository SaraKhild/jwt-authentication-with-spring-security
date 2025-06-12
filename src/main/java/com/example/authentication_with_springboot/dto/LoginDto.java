package com.example.authentication_with_springboot.dto;

import lombok.Data;

@Data
public class LoginDto {

    private String username;
    private String password;
}