package com.example.jwtspringsecuritydemo.dto.Authentication;

import lombok.Data;
import lombok.Generated;

@Data
@Generated
public class AuthResponse {
    public String accessToken;
    public Long expiresIn;
    public String[] scopes;
}
