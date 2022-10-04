package com.safetynet.alerts.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.dto.FirestationDTO;
import com.safetynet.alerts.exception.BadRequestException;
import com.safetynet.alerts.exception.ResourceConflictException;
import com.safetynet.alerts.service.RequestService;

@ExtendWith(MockitoExtension.class)
public class ControllerExceptionHandlerTest {

	@InjectMocks
	private ControllerExceptionHandler controllerExceptionHandler;
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	@Mock
	private RequestService requestService;
	
	private MockHttpServletRequest requestMock;
	private WebRequest request;
	
	@BeforeEach
	public void setUpPerTest() {
		requestMock = new MockHttpServletRequest();
		requestMock.setServerName("http://localhost:8080");
	}
	
	@AfterEach
    public void undefPerTest() {
		requestMock = null;
		request = null;
	}
	
	@Test
	@Tag("BadRequestException")
	@DisplayName("badRequestExceptionTest should return a ResponseEntity with error message and HttpStatus.BAD_REQUEST")
	public void badRequestExceptionTest() {
		//GIVEN
		requestMock.setRequestURI("/phoneAlert");
		requestMock.setParameter("firestation", "a");
		requestMock.setMethod("GET");
		request = new ServletWebRequest(requestMock);
		BadRequestException ex = new BadRequestException("Correct request is to specify an integer for the station number");
		when(requestService.requestToString(any(WebRequest.class))).thenReturn("uri=/phoneAlert?firestation=a");
		//WHEN
		ResponseEntity<ErrorMessage> responseEntity = controllerExceptionHandler.badRequestException(ex, request);
		//THEN
		assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).asString().contains("Correct request is to specify an integer for the station number");
	}
	
	@Test
	@Tag("ResourceNotFoundException")
	@DisplayName("resourceNotFoundExceptionTest should return a ResponseEntity with error message and HttpStatus.NOT_FOUND")
	public void resourceNotFoundExceptionTest() {
		//GIVEN
		requestMock.setRequestURI("/phoneAlert");
		requestMock.setParameter("firestation", "5");
		requestMock.setMethod("GET");
		request = new ServletWebRequest(requestMock);
		ResourceNotFoundException ex = new ResourceNotFoundException("No fire station found");
		when(requestService.requestToString(any(WebRequest.class))).thenReturn("uri=/phoneAlert?firestation=5");
		//WHEN
		ResponseEntity<ErrorMessage> responseEntity = controllerExceptionHandler.resourceNotFoundException(ex, request);
		//THEN
		assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
		assertThat(responseEntity.getBody()).asString().contains("No fire station found");
	}

	@Test
	@Tag("ResourceConflictException")
	@DisplayName("resourceConflictExceptionTest should return a ResponseEntity with error message and HttpStatus.CONFLICT")
	public void resourceConflictExceptionTest() {
		requestMock.setRequestURI("/firestation");
		FirestationDTO firestationDTO = new FirestationDTO();
		firestationDTO.setAddress("1509 Culver St");
		firestationDTO.setStation("3");
		try {
			requestMock.setContent(objectMapper.writeValueAsBytes(firestationDTO));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		requestMock.setMethod("POST");
		request = new ServletWebRequest(requestMock);

		//GIVEN
		ResourceConflictException ex = new ResourceConflictException("Address : 1509 Culver St has already a firestation");
		when(requestService.requestToString(any(WebRequest.class))).thenReturn("uri=/firestation");
		//WHEN
		ResponseEntity<ErrorMessage> responseEntity = controllerExceptionHandler.resourceConflictException(ex, request);
		//THEN
		assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.CONFLICT);
		assertThat(responseEntity.getBody()).asString().contains("Address : 1509 Culver St has already a firestation");
	}

}
