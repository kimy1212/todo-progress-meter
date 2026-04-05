package com.kimy1212.progressmeter.presentation.advice;

import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.kimy1212.progressmeter.application.exception.NotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	private boolean isAjax(HttpServletRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	}

	@ExceptionHandler(NotFoundException.class)
	public Object handleNotFound(NotFoundException e, HttpServletRequest request) {
		log.warn("Not Found: {}", e.getMessage());
		if (!isAjax(request)) {
			return "redirect:/error/404";
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Object handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
		log.warn("Validation failed: {}", e.getMessage());
		if (!isAjax(request)) {
			return "redirect:/error/general";
		}
		Map<String, String> errors = e.getBindingResult().getFieldErrors().stream()
				.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public Object handleConstraintViolation(ConstraintViolationException e, HttpServletRequest request) {
		log.warn("Constraint violation: {}", e.getMessage());
		if (!isAjax(request)) {
			return "redirect:/error/general";
		}
		Map<String, String> errors = e.getConstraintViolations().stream()
				.collect(Collectors.toMap(
						v -> v.getPropertyPath().toString(),
						v -> v.getMessage()));
		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public Object handleIllegalArgument(IllegalArgumentException e, HttpServletRequest request) {
		log.warn("Illegal argument: {}", e.getMessage());
		if (!isAjax(request)) {
			return "redirect:/error/general";
		}
		return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
	}

}
