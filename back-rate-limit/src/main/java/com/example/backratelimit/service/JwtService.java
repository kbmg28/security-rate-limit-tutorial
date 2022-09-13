package com.example.backratelimit.service;

import com.example.backratelimit.dto.LoginDTO;
import com.example.backratelimit.exception.AuthorizationException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    @Value("${secret-key-jwt}")
    private String secretKeyJwt;

    public String getJwtValidated(String authorization) {

        if (Strings.isBlank(authorization) || !authorization.startsWith("Bearer ")) {
            throw new AuthorizationException();
        }

        String jwtToken = authorization.substring(7, authorization.length());

        if (!isValidJwtToken(jwtToken)) {
            throw new AuthorizationException();
        }

        return jwtToken;
    }

    public String getEmailFromJwt(String jwtToken) {
        return Jwts.parser()
                .setSigningKey(secretKeyJwt)
                .parseClaimsJws(jwtToken)
                .getBody()
                .get("email", String.class);
    }

    public boolean isValidJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKeyJwt).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String generateToken(LoginDTO loginDTO) {

        LocalDateTime now = LocalDateTime.now();
        Date startDate = getDate(now);
        Date expireDate = getDate(now.plusMinutes(5));

        return generateToken(loginDTO, startDate, expireDate);
    }

    private String generateToken(LoginDTO loginDTO, Date startDate, Date expireDate) {
        return Jwts.builder()
                .setIssuer("My application with Recaptcha v3 and Bucket4j")
                .setSubject(UUID.randomUUID().toString()) // The subject can be of user id
                .setIssuedAt(startDate)
                .claim("email", loginDTO.getEmail())
                .claim("permissions", new ArrayList<>()) //insert here permissions logic
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, secretKeyJwt)
                .compact();
    }

    private Date getDate(LocalDateTime startDate) {
        return Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
    }
}
