package com.example.backratelimit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseErrorDTO {
    @Schema(example = "400")
    private final String httpStatus;

    @Schema(example = "400 - Bad user information")
    private final String message;

    public ResponseErrorDTO(HttpStatus http, String message) {
        String template = "%d - %s";
        this.httpStatus = String.format(template, http.value(), http.getReasonPhrase());

        this.message = message;
    }
}
