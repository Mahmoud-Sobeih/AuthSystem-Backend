package com.project.AuthSystem.controller;

import com.project.AuthSystem.DTO.LoginRequest;
import com.project.AuthSystem.DTO.LoginResponse;
import com.project.AuthSystem.DTO.SignupRequest;
import com.project.AuthSystem.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(authService.login(loginRequest), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest signupRequest) {
        return new ResponseEntity<>(authService.signup(signupRequest), HttpStatus.CREATED);
    }

}
