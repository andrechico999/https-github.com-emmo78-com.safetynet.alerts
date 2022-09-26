package com.safetynet.alerts.controller;



import java.util.Arrays;

import org.modelmapper.spi.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);
	
	@ExceptionHandler(value = BadRequestException.class)
	public ResponseEntity<ErrorMessage> badRequestException(BadRequestException ex, WebRequest request) {
		logger.error("Bad Request {}", request.getDescription(false)); 
		ErrorMessage message = new ErrorMessage(ex.getMessage());
		return new ResponseEntity<ErrorMessage>(message, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = {AddressNotFoundException.class, Exception.class})
	public ResponseEntity<ErrorMessage> resourceNotFoundException(AddressNotFoundException ex, WebRequest request) {
		ErrorMessage message = new ErrorMessage(ex.getMessage());
		StringBuffer parameters = new StringBuffer(request.getDescription(false)+"?");
		request.getParameterMap().forEach((p,v) -> {
			parameters.append(p+"=");
			Arrays.asList(v).forEach(vs -> parameters.append(vs));
		});
		logger.error("Address not found {}",parameters.toString());
	    return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
	}
}