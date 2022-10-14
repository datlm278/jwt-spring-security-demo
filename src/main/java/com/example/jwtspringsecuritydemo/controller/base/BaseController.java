package com.example.jwtspringsecuritydemo.controller.base;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
public class BaseController {
    private Claims user;

    public Object getCurentUser() {
        if (user == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                user = (Claims) authentication.getPrincipal();
            }
        }
        return user;
    }
}
