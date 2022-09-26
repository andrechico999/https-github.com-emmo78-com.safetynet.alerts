package com.safetynet.alerts.controller;



import org.modelmapper.spi.ErrorMessage;
//import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.safetynet.alerts.exception.AddressNotFoundException;
import com.safetynet.alerts.exception.BadRequestException;

@ControllerAdvice
public class ControllerExceptionHandler {
  
	@ExceptionHandler(value = BadRequestException.class)
	public ResponseEntity<ErrorMessage> badRequestException(BadRequestException ex, WebRequest request) {
		ErrorMessage message = new ErrorMessage(ex.getMessage());
	    return new ResponseEntity<ErrorMessage>(message, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = {AddressNotFoundException.class, Exception.class})
	public ResponseEntity<ErrorMessage> resourceNotFoundException(AddressNotFoundException ex, WebRequest request) {
		ErrorMessage message = new ErrorMessage(ex.getMessage());
	    return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
	}
}