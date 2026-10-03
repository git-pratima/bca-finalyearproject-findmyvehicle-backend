package com.pratima.bca.findmyvehicle.exception;

import com.pratima.bca.findmyvehicle.dto.Response;
import com.pratima.bca.findmyvehicle.dto.Status;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new LinkedHashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {

            errors.put(error.getField(), error.getDefaultMessage());

        }

        Status status = new Status();
        status.setStatus(HttpStatus.BAD_REQUEST.value());
        status.setMessage("Validation Failed");

        Response response = new Response();
        response.setStatus(status);
        response.setData(errors);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Response> handleNotFoundException(ResourceNotFoundException ex){
        Status status = Status.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build();

        Response response = Response.builder()
                .status(status)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Response> handleAccessDeniedException(AccessDeniedException ex) {
        Status status = Status.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .message("Access denied.")
                .build();

        Response response = Response.builder()
                .status(status)
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleAllExceptions(Exception ex){
        Status status = Status.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .build();

        Response response = Response.builder()
                .status(status)
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Response> handleDataIntegrityViolation(
            DataIntegrityViolationException ex) {

        String message = "Duplicate data found.";

        Throwable rootCause = ex.getMostSpecificCause();

        if (rootCause != null) {

            String error = rootCause.getMessage();

            if (error.contains("vehicle_chassis_number")) {
                message = "Chassis number already exists.";
            } else if (error.contains("reg_number")) {
                message = "Registration number already exists.";
            } else if (error.contains("owner_email")) {
                message = "Email already exists.";
            }
        }

        Status status = new Status();
        status.setStatus(HttpStatus.CONFLICT.value());
        status.setMessage(message);

        Response response = new Response();
        response.setStatus(status);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Duplicate Resource
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Response> handleDuplicateResource(
            DuplicateResourceException ex) {

        Status status = new Status();
        status.setStatus(HttpStatus.CONFLICT.value());
        status.setMessage(ex.getMessage());

        Response response = new Response();
        response.setStatus(status);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }


    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Response> handleInvalidCredentialsException(InvalidCredentialsException ex){
        Status status = Status.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();

        Response response = Response.builder()
                .status(status)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<Response> handlePasswordMismatch(
            PasswordMismatchException ex) {

        Status status = new Status();
        status.setStatus(400);
        status.setMessage(ex.getMessage());

        Response response = new Response();
        response.setStatus(status);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Response> handleInvalidPassword(
            InvalidPasswordException ex) {

        Status status = new Status();
        status.setStatus(400);
        status.setMessage(ex.getMessage());

        Response response = new Response();
        response.setStatus(status);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}
