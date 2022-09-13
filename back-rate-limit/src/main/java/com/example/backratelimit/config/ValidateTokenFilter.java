package com.example.backratelimit.config;

import com.example.backratelimit.exception.AuthorizationException;
import com.example.backratelimit.exception.ForbiddenException;
import com.example.backratelimit.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class ValidateTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
        throws ServletException, IOException {

        if (isRequestForApi(request)) {
            try {
                validateJwtToken(request);

            } catch (Exception e) {
                if (e instanceof AuthorizationException) {
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
                    return;
                } else if (e instanceof ForbiddenException) {
                    response.sendError(HttpStatus.FORBIDDEN.value(), e.getMessage());
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isRequestForApi(HttpServletRequest request) {
        return request.getRequestURL().indexOf("/api/") >= 0;
    }

    private void validateJwtToken(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        String jwtToken = jwtService.getJwtValidated(authorization);
        String email = jwtService.getEmailFromJwt(jwtToken);

        UserDetails userDetails = User.builder().username(email).roles("").password("").build();

        UsernamePasswordAuthenticationToken userToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        userToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(userToken);
    }


}
