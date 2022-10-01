package com.safetynet.alerts.controller;

import org.modelmapper.spi.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.safetynet.alerts.exception.BadRequestException;
import com.safetynet.alerts.exception.ResourceConflictException;
import com.safetynet.alerts.service.RequestService;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {
	
	@Autowired
	private RequestService requestService;
	
	@ExceptionHandler(value = BadRequestException.class)
	public ResponseEntity<ErrorMessage> badRequestException(BadRequestException ex, WebRequest request) {
		log.error("Bad request {} : {} : {}", requestService.requestToString(request), ((ServletWebRequest) request).getHttpMethod(), ex.getMessage()); 
		ErrorMessage message = new ErrorMessage(ex.getMessage());
		return new ResponseEntity<ErrorMessage>(message, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = ResourceNotFoundException.class)
	public ResponseEntity<ErrorMessage> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		log.error("Not found {} : {} : {}", requestService.requestToString(request), ((ServletWebRequest) request).getHttpMethod(), ex.getMessage());
		ErrorMessage message = new ErrorMessage(ex.getMessage());
		return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = ResourceConflictException.class)
	public ResponseEntity<ErrorMessage> resourceConflictException(ResourceConflictException ex, WebRequest request) {
		log.error("Conflict {} : {} : {}", requestService.requestToString(request), ((ServletWebRequest) request).getHttpMethod(), ex.getMessage());
		ErrorMessage message = new ErrorMessage(ex.getMessage());
		return new ResponseEntity<ErrorMessage>(message, HttpStatus.CONFLICT);
	}
	
}