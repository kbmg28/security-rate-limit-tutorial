package com.example.backratelimit.exception;

import lombok.Getter;

public class RateLimitException extends RuntimeException {

    public RateLimitException() {
    }

    public RateLimitException(String message) {
        super(message);
    }
}