package com.example.backratelimit.controller;

import com.example.backratelimit.dto.JwtTokenDTO;
import com.example.backratelimit.dto.LoginDTO;
import com.example.backratelimit.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private BuildProperties buildProperties;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public JwtTokenDTO authenticate(@RequestBody LoginDTO loginDTO) {

        String token = jwtService.generateToken(loginDTO);

        return new JwtTokenDTO(token);
    }

}
