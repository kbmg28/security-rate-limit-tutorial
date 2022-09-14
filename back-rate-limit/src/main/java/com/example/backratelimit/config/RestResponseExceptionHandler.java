package com.example.backratelimit.config;

import com.example.backratelimit.dto.ResponseErrorDTO;
import com.example.backratelimit.exception.AuthorizationException;
import com.example.backratelimit.exception.ForbiddenException;
import com.example.backratelimit.exception.LockedClientException;
import com.example.backratelimit.exception.RateLimitException;
import com.example.backratelimit.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.stream.Collectors;


@Slf4j
@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler({Exception.class})
    public ResponseEntity<ResponseErrorDTO> handleInternal(final Exception ex, final WebRequest request) {
        return generatedError("Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ResponseErrorDTO> handleAccessDeniedException(final AccessDeniedException ex, final WebRequest request) {
        return generatedError("You don't have permission", HttpStatus.FORBIDDEN, ex);
    }

    @ExceptionHandler({ForbiddenException.class})
    public ResponseEntity<ResponseErrorDTO> handleForbiddenException(final ForbiddenException ex, final WebRequest request) {
        return generatedError("You don't have permission", HttpStatus.FORBIDDEN, ex);
    }

    @ExceptionHandler({DataAccessException.class, HibernateException.class, DataIntegrityViolationException.class, EntityNotFoundException.class})
    protected ResponseEntity<ResponseErrorDTO> handleConflict(final DataAccessException ex, final WebRequest request) {

        return generatedError("Failed to process request", HttpStatus.CONFLICT, ex);
    }


    @ExceptionHandler({IllegalArgumentException.class,
            EntityExistsException.class})
    public ResponseEntity<ResponseErrorDTO> handleArguments(final RuntimeException ex, final WebRequest request) {
        return generatedError(ex.getMessage(), HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler({ServiceException.class})
    public ResponseEntity<ResponseErrorDTO> handleServiceException(final ServiceException ex, final WebRequest request) {
        return generatedError(ex.getMessage(), HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler({AuthorizationException.class})
    public ResponseEntity<ResponseErrorDTO> handleAccessDeniedException(final AuthorizationException ex, final WebRequest request) {
        String mes = ex.getMessage() == null ? "Access Unauthorized" : ex.getMessage();
        return generatedError(mes, HttpStatus.UNAUTHORIZED, ex);
    }

    @ExceptionHandler({LockedClientException.class})
    public ResponseEntity<ResponseErrorDTO> handleLockedClientException(final LockedClientException ex, final WebRequest request) {
        String mes = ex.getMessage() == null ? "Invalid recaptcha" : ex.getMessage();
        return generatedError(mes, HttpStatus.LOCKED, ex);
    }

    @ExceptionHandler({RateLimitException.class})
    public ResponseEntity<ResponseErrorDTO> handleRateLimitException(final RateLimitException ex, final WebRequest request) {
        String mes = ex.getMessage() == null ? "Blocked by client" : ex.getMessage();

        return generatedError(mes, HttpStatus.TOO_MANY_REQUESTS, ex);
    }

    @ExceptionHandler({HttpMessageConversionException.class})
    public ResponseEntity<ResponseErrorDTO> handleHttpMessageConversionException(final RuntimeException ex, final WebRequest request) {
        return generatedError("Invalid arguments", HttpStatus.BAD_REQUEST, ex);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status,
        final WebRequest request) {
        String msg = "Invalid data fields";
        String errors = ex.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
        ResponseErrorDTO responseError = new ResponseErrorDTO(HttpStatus.BAD_REQUEST, String.format("%s %s", msg, errors));


        return handleExceptionInternal(ex, responseError, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
        ResponseErrorDTO responseError = new ResponseErrorDTO(HttpStatus.BAD_REQUEST, "Invalid data");

        return handleExceptionInternal(ex, responseError, headers, HttpStatus.BAD_REQUEST, request);
    }

    private ResponseEntity<ResponseErrorDTO> generatedError(String message, HttpStatus http, Exception ex) {
        ResponseErrorDTO responseError = new ResponseErrorDTO(http, message);
        return new ResponseEntity<>(responseError, http);
    }

}
