package com.example.backratelimit.controller;

import com.example.backratelimit.config.recaptcha.v3.RecaptchaEnum;
import com.example.backratelimit.config.recaptcha.v3.RecaptchaV3Service;
import com.example.backratelimit.dto.JwtTokenDTO;
import com.example.backratelimit.dto.LoginDTO;
import com.example.backratelimit.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private BuildProperties buildProperties;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RecaptchaV3Service recaptchaV3Service;

    @PostMapping("/v1/login")
    public JwtTokenDTO authenticate(@RequestBody LoginDTO loginDTO) {

        String token = jwtService.generateToken(loginDTO);

        return new JwtTokenDTO(token);
    }

    @PostMapping("/v2/login")
    public JwtTokenDTO authenticateWithRecaptcha(@RequestBody LoginDTO loginDTO,
                                                 @RequestParam(name="g-recaptcha-response") String recaptchaResponse) {
        recaptchaV3Service.processResponse(recaptchaResponse, RecaptchaEnum.LOGIN_ACTION.getValue());
        String token = jwtService.generateToken(loginDTO);

        return new JwtTokenDTO(token);
    }

}
