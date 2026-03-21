package com.kimy1212.progressmeter.presentation.advice;

import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.kimy1212.progressmeter.application.exception.NotFoundException;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleNotFound(NotFoundException e, Model model) {
		log.warn("Not Found: {}", e.getMessage());
		model.addAttribute("message", e.getMessage());
		return "error/404";
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
		Map<String, String> errors = e.getBindingResult().getFieldErrors().stream()
				.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
		log.warn("Validation failed: {}", errors);
		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException e) {
		Map<String, String> errors = e.getConstraintViolations().stream()
				.collect(Collectors.toMap(
						v -> v.getPropertyPath().toString(),
						v -> v.getMessage()));
		log.warn("Constraint violation: {}", errors);
		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
		log.warn("Illegal argument: {}", e.getMessage());
		return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
	}

}
