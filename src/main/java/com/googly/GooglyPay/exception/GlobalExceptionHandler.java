package com.googly.GooglyPay.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.googly.GooglyPay.utils.Contants;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest webRequest) {
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), webRequest.getDescription(false));
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("message", Contants.CUSTOMER_NOT_FOUND);
		return new ResponseEntity<>(errorDetails, responseHeaders, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(IncorrectDataException.class)
	public ResponseEntity<ErrorDetails> handleIncorrectDataException(IncorrectDataException exception, WebRequest webRequest) {
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), webRequest.getDescription(false));
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("message", Contants.CUSTOMER_NOT_FOUND);
		return new ResponseEntity<>(errorDetails, responseHeaders, HttpStatus.BAD_REQUEST);
	}
}
