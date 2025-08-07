package com.example.jpa_homework.exception.global;

import com.example.jpa_homework.exception.BadRequestException;
import com.example.jpa_homework.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.ConnectException;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.example.jpa_homework.utils.RequestUtils.handleDetailsException;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(exception = {
//            AccountNotVerifiedException.class,
//            ApiException.class,
            BadRequestException.class,
//            NotAllowedFile.class,
            IllegalArgumentException.class,
            NullPointerException.class
    })
    public ProblemDetail handleBadRequestExceptions(HttpServletRequest request, Exception e) {
        return handleDetailsException(request, e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleJsonParseError(HttpServletRequest request, HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        String errorMessage = "Invalid JSON format";

        if (cause instanceof JsonParseException) {
            errorMessage = "JSON syntax error or invalid format. Please check the request body.";
        } else if (cause instanceof JsonMappingException) {
            errorMessage = "JSON mapping error or missing required fields. Please check the request body.";
        } else if (cause instanceof UnrecognizedPropertyException) {
            UnrecognizedPropertyException upe = (UnrecognizedPropertyException) cause;
            errorMessage = "Unknown field '" + upe.getPropertyName() + "' in JSON";
        }

        ProblemDetail detail = ProblemDetail.forStatusAndDetail(BAD_REQUEST, errorMessage);
        detail.setTitle("Invalid Request");
        detail.setProperty("timestamp", LocalDateTime.now());
////        detail.setType(URI.create(API_URL + request.getRequestURI() + "/validate"));

        return detail;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(HttpServletRequest request, MethodArgumentTypeMismatchException ex) {
        String message = (ex.getRequiredType() == LocalDate.class)
                ? "Invalid date format. Please use yyyy-MM-dd."
                : "Invalid parameter.";
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(BAD_REQUEST, message);
        detail.setProperty("timestamp", LocalDateTime.now());
//        detail.setType(URI.create(API_URL + request.getRequestURI() + "/validate"));
        return detail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(HttpServletRequest request, MethodArgumentNotValidException ex) {
        var detail = ProblemDetail.forStatus(BAD_REQUEST);
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        detail.setProperty("errors", errors);
        detail.setProperty("timestamp", LocalDateTime.now());
//        detail.setType(URI.create(API_URL + request.getRequestURI() + "/validate"));
        return detail;
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ProblemDetail handleValidationExceptions(HttpServletRequest request, HandlerMethodValidationException ex) {
        var detail = ProblemDetail.forStatus(BAD_REQUEST);
        Map<String, String> errors = new HashMap<>();
        ex.getParameterValidationResults().forEach(
                parameterError -> {
                    String paramName = parameterError.getMethodParameter().getParameterName();

                    parameterError.getResolvableErrors().forEach(
                            resolvableError -> {
                                errors.put(paramName, resolvableError.getDefaultMessage());
                            }
                    );
                });
        detail.setProperty("errors", errors);
        detail.setProperty("timestamp", LocalDateTime.now());
//        detail.setType(URI.create(API_URL + request.getRequestURI() + "/validate"));
        return detail;
    }

    @ExceptionHandler(ConnectException.class)
    public ProblemDetail HandleConnectionMinioException(ConnectException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Connection failed");
        problemDetail.setDetail("Connection is failed ");
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(HttpServletRequest request, Exception e) {
        var detail = handleDetailsException(request, e.getMessage(), BAD_REQUEST);
//        detail.setType(URI.create(API_URL + request.getRequestURI() + "/validate"));
        return detail;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFoundExceptions(HttpServletRequest request, Exception e) {
        var detail = handleDetailsException(request, e.getMessage(), NOT_FOUND);
//        detail.setType(URI.create(API_URL + request.getRequestURI()));
        return detail;
    }
}
