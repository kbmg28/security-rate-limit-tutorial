package com.example.backratelimit.exception;

public class LockedClientException extends RuntimeException {
    public LockedClientException() {
        super();
    }

    public LockedClientException(String msg) {
        super(msg);
    }
}