package com.example.jwtspringsecuritydemo.services.user;

import com.example.jwtspringsecuritydemo.dto.UserAccountDto;
import com.example.jwtspringsecuritydemo.entity.Role;
import com.example.jwtspringsecuritydemo.entity.User;
import com.example.jwtspringsecuritydemo.repositories.RoleRepository;
import com.example.jwtspringsecuritydemo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void createPresidentAccount(UserAccountDto dto) throws Exception {
        User findUser = userRepository.findByUsername(dto.getUsername());
        if (findUser != null) {
            throw new Exception("User is existed");
        }

        Role userRole = roleRepository.findByName("PRESIDENT");

        if (userRole != null) {
            User user = new User();
            user.setCreateTime(Timestamp.from(Instant.now()));
            user.setUsername(dto.getUsername());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));

            ArrayList<Role> roles = new ArrayList<>();
            roles.add(userRole);
            user.setRoles(roles);

            userRepository.save(user);
        } else {
            throw new Exception("Can not find PRESIDENT role");
        }
    }
}
