package com.example.backratelimit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    private BuildProperties buildProperties;

    @PostMapping("/test")
    public String testPost() {
        return getUserLogged();
    }

    @PutMapping("/test")
    public String testPut() {
        return getUserLogged();
    }

    @DeleteMapping("/test")
    public String testDelete() {
        return getUserLogged();
    }

    @GetMapping("/test")
    public String testGet() {
        return getUserLogged();
    }

    private String getUserLogged() {
        return "User logged: " + SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

}
