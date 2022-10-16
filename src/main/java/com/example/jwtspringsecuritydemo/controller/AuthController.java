package com.example.jwtspringsecuritydemo.controller;

import com.example.jwtspringsecuritydemo.common.utils.SecurityUtils;
import com.example.jwtspringsecuritydemo.dto.Authentication.AuthRequest;
import com.example.jwtspringsecuritydemo.dto.Authentication.AuthResponse;
import com.example.jwtspringsecuritydemo.entity.Role;
import com.example.jwtspringsecuritydemo.entity.User;
import com.example.jwtspringsecuritydemo.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.jwtspringsecuritydemo.common.constant.Constant.PREFIX_API_URL;

@RestController
@RequestMapping(PREFIX_API_URL)
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    private final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) throws IOException, UsernameNotFoundException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }

        User user = userService.findByUsername(authRequest.getUsername());
        if (user == null || !new BCryptPasswordEncoder().matches(authRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Incorrect username or password");
        }

        int tokenLifeTime = 3600;
        Date issuedAt = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(issuedAt);
        calendar.add(Calendar.SECOND, tokenLifeTime);
        Date expiredAt = calendar.getTime();
        List<String> scopes = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());

        String token = Jwts.builder()
                .setIssuedAt(issuedAt)
                .setExpiration(expiredAt)
                .claim("scope", scopes)
                .claim("username", user.getUsername())
                .signWith(SecurityUtils.getPrivateKey("privatekey.pem"))
                .compact();

        AuthResponse authResponse = new AuthResponse();
        authResponse.accessToken = token;
        authResponse.expiresIn = 3600L;
        authResponse.scopes = scopes.toArray(new String[]{});
        return ResponseEntity.ok(authResponse);
    }

}
