package com.example.jwtspringsecuritydemo.services;

import com.example.jwtspringsecuritydemo.dto.Authentication.Account;
import com.example.jwtspringsecuritydemo.entity.User;
import com.example.jwtspringsecuritydemo.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthUserService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        Account account = new Account();
        account.setUsername(user.getUsername());
        account.setPassword(user.getPassword());

        return account;
    }
}
