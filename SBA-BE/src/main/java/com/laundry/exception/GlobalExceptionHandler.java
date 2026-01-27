package com.laundry.exception;

import com.laundry.constant.ErrorEnum;
import com.laundry.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j(topic = "ERROR-EXCEPTION")
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFoundException(AppException exception) {
        returnError(exception);
        ApiResponse<Object> response = ApiResponse.builder()
                .status("error")
                .code(exception.getCode())
                .message(exception.getMessage())
                .data(null)
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<FieldValidateException>>> handleArgumentNotValidationException(MethodArgumentNotValidException exception) {
        List<FieldValidateException> errors = new java.util.ArrayList<>(exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> new FieldValidateException(e.getField(), e.getDefaultMessage()))
                .toList());
        var globalErrors = exception.getBindingResult()
                .getGlobalErrors()
                .stream()
                .map(e -> new FieldValidateException(e.getObjectName(), e.getDefaultMessage()))
                .toList();
        errors.addAll(globalErrors);
        returnError(exception);
        ApiResponse<List<FieldValidateException>> response = ApiResponse.<List<FieldValidateException>>builder()
                .status("error")
                .code(ErrorEnum.INVALID_FIELDS.getCode())
                .message(ErrorEnum.INVALID_FIELDS.getMessage())
                .data(errors)
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<List<FieldValidateException>>> handleConstraintViolationException(ConstraintViolationException ex) {
        List<FieldValidateException> errors = ex.getConstraintViolations()
                .stream()
                .map(cv -> new FieldValidateException(
                        cv.getPropertyPath().toString(),
                        cv.getMessage()
                ))
                .collect(Collectors.toList());

        ApiResponse<List<FieldValidateException>> response = ApiResponse.<List<FieldValidateException>>builder()
                .status("error")
                .code(ErrorEnum.INVALID_FIELDS.getCode())
                .message(ErrorEnum.INVALID_FIELDS.getMessage())
                .data(errors)
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<List<FieldValidateException>>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String msg = ex.getRootCause() != null
                ? ex.getRootCause().getMessage()
                : ex.getMessage();
        FieldValidateException error = new FieldValidateException("requestBody", msg);
        ApiResponse<List<FieldValidateException>> response = ApiResponse.<List<FieldValidateException>>builder()
                .status("error")
                .code(ErrorEnum.INVALID_FIELDS.getCode())
                .message("Malformed JSON request or missing body")
                .data(List.of(error))
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse<Object>> handleNoSuchElement(NoSuchElementException ex) {
        log.info("Resource not found: {}", ex.getMessage());
        ApiResponse<Object> response = ApiResponse.builder()
                .status("error")
                .code(ErrorEnum.USER_NOT_FOUND.getCode())
                .message("Resource not found")
                .data(null)
                .build();
        return ResponseEntity.status(404).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.info("Database constraint violated: {}", ex.getMessage());
        ApiResponse<Object> response = ApiResponse.builder()
                .status("error")
                .code(ErrorEnum.DATA_INTEGRITY_VIOLATION.getCode())
                .message("Database constraint violated: " + ex.getMostSpecificCause().getMessage())
                .data(null)
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDenied(AccessDeniedException ex) {
        log.info("Access denied: {}", ex.getMessage());
        ApiResponse<Object> response = ApiResponse.builder()
                .status("error")
                .code(ErrorEnum.ACCESS_DENIED.getCode())
                .message("You do not have permission to perform this action")
                .data(null)
                .build();
        return ResponseEntity.status(403).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
        returnError(ex);
        ApiResponse<Object> response = ApiResponse.builder()
                .status("error")
                .code(ErrorEnum.INTERNAL_SERVER_ERROR.getCode())
                .message("Unexpected error occurred: " + ex.getMessage())
                .data(null)
                .build();
        return ResponseEntity.status(500).body(response);
    }
    public void returnError(Exception e) {
        log.error("AppException: {}", e.getMessage());
    }
}
