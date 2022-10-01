package com.safetynet.alerts.controller;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.spi.ErrorMessage;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.safetynet.alerts.exception.BadRequestException;
import com.safetynet.alerts.exception.ResourceConflictException;
import com.safetynet.alerts.service.RequestService;

@ExtendWith(MockitoExtension.class)
public class ControllerExceptionHandlerTest {

	@InjectMocks
	private ControllerExceptionHandler controllerExceptionHandler;
	
	@Mock
	private RequestService requestService;
	
	private MockHttpServletRequest requestMock;
	private WebRequest request;
	
	@BeforeEach
	public void setUpPerTest() {
		requestMock = new MockHttpServletRequest();
		requestMock.setServerName("http://localhost:8080");
		requestMock.setRequestURI("/phoneAlert");
		requestMock.setQueryString("firestation=1");
		requestMock.setMethod("GET");
		request = new ServletWebRequest(requestMock);
	}
	
	@AfterEach
    public void undefPerTest() {
		requestMock = null;
		request = null;
	}
	
	@Test
	@Tag("badRequestException test")
	@DisplayName("badRequestExceptionTest should return a ResponseEntity with error message and HttpStatus.BAD_REQUEST")
	public void badRequestExceptionTest() {
		//GIVEN
		BadRequestException ex = new BadRequestException("Correct parameter value is an integer");
		when(requestService.requestToString(any(WebRequest.class))).thenReturn("/phoneAlert?firestation=1");
		//WHEN
		ResponseEntity<ErrorMessage> responseEntity = controllerExceptionHandler.badRequestException(ex, request);
		//THEN
		assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).asString().contains("Correct parameter value is an integer");
	}
	
	@Test
	@Tag("resourceNotFoundException test")
	@DisplayName("resourceNotFoundExceptionTest should return a ResponseEntity with error message and HttpStatus.NOT_FOUND")
	public void resourceNotFoundExceptionTest() {
		//GIVEN
		ResourceNotFoundException ex = new ResourceNotFoundException("No Firestation wtih this number 1");
		when(requestService.requestToString(any(WebRequest.class))).thenReturn("/phoneAlert?firestation=1");
		//WHEN
		ResponseEntity<ErrorMessage> responseEntity = controllerExceptionHandler.resourceNotFoundException(ex, request);
		//THEN
		assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
		assertThat(responseEntity.getBody()).asString().contains("No Firestation wtih this number 1");
	}

	@Test
	@Tag("resourceConflictException test")
	@DisplayName("resourceConflictExceptionTest should return a ResponseEntity with error message and HttpStatus.NOT_FOUND")
	public void resourceConflictExceptionTest() {
		//GIVEN
		ResourceConflictException ex = new ResourceConflictException("Firestation 1 already exists");
		when(requestService.requestToString(any(WebRequest.class))).thenReturn("/phoneAlert?firestation=1");
		//WHEN
		ResponseEntity<ErrorMessage> responseEntity = controllerExceptionHandler.resourceConflictException(ex, request);
		//THEN
		assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.CONFLICT);
		assertThat(responseEntity.getBody()).asString().contains("Firestation 1 already exists");
	}

}
