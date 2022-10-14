package com.example.jwtspringsecuritydemo.controller;

import com.example.jwtspringsecuritydemo.common.constant.Constant;
import com.example.jwtspringsecuritydemo.controller.base.BaseController;
import com.example.jwtspringsecuritydemo.dto.UserAccountDto;
import com.example.jwtspringsecuritydemo.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constant.PREFIX_API_URL + "user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @PostMapping("/create-president-account")
    public ResponseEntity<String> createPresidentAccount(@RequestBody UserAccountDto dto) throws Exception {
        userService.createPresidentAccount(dto);
        return ResponseEntity.ok().body("Create president account successfully!");
    }

}
