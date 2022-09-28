package com.example.backratelimit.controller;

import com.example.backratelimit.config.recaptcha.v3.RecaptchaEnum;
import com.example.backratelimit.config.recaptcha.v3.RecaptchaV3Service;
import com.example.backratelimit.domain.Person;
import com.example.backratelimit.dto.JwtTokenDTO;
import com.example.backratelimit.dto.LoginDTO;
import com.example.backratelimit.exception.AuthorizationException;
import com.example.backratelimit.repository.PersonRepository;
import com.example.backratelimit.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private BuildProperties buildProperties;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RecaptchaV3Service recaptchaV3Service;

    @Autowired
    private PersonRepository personRepository;

    @PostMapping("/v1/login")
    public JwtTokenDTO authenticate(@RequestBody LoginDTO loginDTO) {

        return getJwtTokenDTOValidated(loginDTO);
    }

    @PostMapping("/v2/login")
    public JwtTokenDTO authenticateWithRecaptcha(@RequestBody LoginDTO loginDTO,
                                                 @RequestParam(name="g-recaptcha-response") String recaptchaResponse) {
        recaptchaV3Service.processResponse(recaptchaResponse, RecaptchaEnum.LOGIN_ACTION.getValue());

        return getJwtTokenDTOValidated(loginDTO);
    }

    private JwtTokenDTO getJwtTokenDTOValidated(LoginDTO loginDTO) {
        validateUser(loginDTO);
        String token = jwtService.generateToken(loginDTO);

        return new JwtTokenDTO(token);
    }

    private void validateUser(LoginDTO loginDTO) {
        Person person = personRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(AuthorizationException::new);

        validatePassword(loginDTO.getPassword(), person.getPassword());
    }

    private void validatePassword(String plainTextPassword, String hashPassword) {
        boolean isCorrectPassword = BCrypt.checkpw(plainTextPassword, hashPassword);

        if (!isCorrectPassword) {
            throw new AuthorizationException();
        }
    }
}
