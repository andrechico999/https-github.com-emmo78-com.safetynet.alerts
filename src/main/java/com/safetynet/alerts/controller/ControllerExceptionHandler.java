package com.safetynet.alerts.controller;

import org.modelmapper.spi.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@ControllerAdvice
public class ControllerExceptionHandler {
	
	private Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);
	
	@Autowired
	private RequestService requestService;
	
	@ExceptionHandler(value = BadRequestException.class)
	public ResponseEntity<ErrorMessage> badRequestException(BadRequestException ex, WebRequest request) {
		logger.error("Bad request {} : {} : {}", requestService.requestToString(request), ((ServletWebRequest) request).getHttpMethod(), ex.getMessage()); 
		ErrorMessage message = new ErrorMessage(ex.getMessage());
		return new ResponseEntity<ErrorMessage>(message, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = ResourceNotFoundException.class)
	public ResponseEntity<ErrorMessage> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		logger.error("{} : {} : {}", requestService.requestToString(request), ((ServletWebRequest) request).getHttpMethod(), ex.getMessage());
		ErrorMessage message = new ErrorMessage(ex.getMessage());
		return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = ResourceConflictException.class)
	public ResponseEntity<ErrorMessage> resourceConflictException(ResourceConflictException ex, WebRequest request) {
		logger.error("{} : {} : {}", requestService.requestToString(request), ((ServletWebRequest) request).getHttpMethod(), ex.getMessage());
		ErrorMessage message = new ErrorMessage(ex.getMessage());
		return new ResponseEntity<ErrorMessage>(message, HttpStatus.CONFLICT);
	}
	
}