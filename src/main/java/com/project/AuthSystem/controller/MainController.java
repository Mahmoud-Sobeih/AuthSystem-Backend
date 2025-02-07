package com.project.AuthSystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secured")
public class MainController {

    @GetMapping("/profile")
    public ResponseEntity<String> getProfile(){
        return new ResponseEntity<>("Welcome " + SecurityContextHolder.getContext().getAuthentication().getName(), HttpStatus.OK);
    }
}
