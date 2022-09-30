package com.safetynet.alerts.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.safetynet.alerts.dto.PersonAddressNameDTO;
import com.safetynet.alerts.exception.BadRequestException;
import com.safetynet.alerts.service.PersonService;


@ExtendWith(MockitoExtension.class)
public class PersonControllerTest {

	@InjectMocks
	private PersonController personController;
	
	@Mock
	private PersonService personService;
	
	MockHttpServletRequest requestMock;
	WebRequest request;
	
	@BeforeEach
	public void setUpPerTest() {
		requestMock = new MockHttpServletRequest();
		request = new ServletWebRequest(requestMock);
	}
	
	@AfterEach
    public void undefPerTest() {
		requestMock = null;
		request = null;
	}
	
	@Nested
    @Tag("personInfoFirstNameLastNameTest")
    @DisplayName("personInfoFirstNameLastNameTest")
    class NominalCases {
		
		@Test
		@Tag("Nominal case")
	    @DisplayName("personInfoFirstNameLastNameTest should return HTTPStatus.OK and a List containing a PersonAddressNameDTO with lastName set")
		public void personInfoFirstNameLastNameTestShouldReturnHTTPStatusOK() {
			//GIVEN
			Optional<String> firstNameOpt = Optional.of("FirstName");
			Optional<String> lastNameOpt = Optional.of("LastName");
			List<PersonAddressNameDTO> personsAddressNameDTO = new ArrayList<>();
			PersonAddressNameDTO personAddressNameDTO = new PersonAddressNameDTO();
			personAddressNameDTO.setLastName("LastName");
			personsAddressNameDTO.add(personAddressNameDTO);	
			when(personService.findPersonsByFirstNameAndLastName(firstNameOpt.get(), lastNameOpt.get(), request)).thenReturn(personsAddressNameDTO);
			ResponseEntity<List<PersonAddressNameDTO>> responseEntity = null;
			
			//WHEN
			try {
				responseEntity = personController.personInfoFirstNameLastName(firstNameOpt, lastNameOpt, request);
			} catch (ResourceNotFoundException | BadRequestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//THEN
			assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
			assertThat(responseEntity.getBody()).contains(personAddressNameDTO);
		}
		
		@Test
		@Tag("Corner case")
	    @DisplayName("personInfoFirstNameLastNameTest should throw BadRequestException")
		public void personInfoFirstNameLastNameTestShouldThrowBadRequestException() {
			//GIVEN
			Optional<String> firstNameOpt = Optional.ofNullable(null);
			Optional<String> lastNameOpt = Optional.ofNullable(null);
			//WHEN
			//THEN
			assertThrows(BadRequestException.class, () -> personController.personInfoFirstNameLastName(firstNameOpt, lastNameOpt, request));
		}
		
	}
}
