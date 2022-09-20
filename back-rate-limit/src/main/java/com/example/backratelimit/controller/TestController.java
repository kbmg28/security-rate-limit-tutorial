package com.example.backratelimit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

import java.time.Duration;

@RestController
@RequestMapping("/api")
public class TestController {

    private final Bucket bucket;

    @Autowired
    private BuildProperties buildProperties;

    public TestController() {
        Bandwidth limit = Bandwidth.classic(3, Refill.greedy(3, Duration.ofMinutes(1)));
        this.bucket = Bucket4j.builder()
            .addLimit(limit)
            .build();
    }

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
    public ResponseEntity<String> testGet() {
        if (bucket.tryConsume(1))
            return ResponseEntity.ok(getUserLogged());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    private String getUserLogged() {
        return "User logged: " + SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

}
