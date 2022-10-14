package com.example.jwtspringsecuritydemo.dto;

import lombok.Data;

@Data
public class UserAccountDto {
    private Long userId;
    private String username;
    private String password;
}
