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
import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.exception.BadRequestException;
import com.safetynet.alerts.exception.ResourceConflictException;
import com.safetynet.alerts.service.PersonService;


@ExtendWith(MockitoExtension.class)
public class PersonControllerTest {

	@InjectMocks
	private PersonController personController;
	
	@Mock
	private PersonService personService;
	
	private MockHttpServletRequest requestMock;
	private WebRequest request;
	
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
    @Tag("GET")
    @DisplayName("GET /personInfo?firstName=<firstName>&lastName=<lastName> tests")
    class PersonInfoTests {
		
		@Test
		@Tag("NominalCase")
	    @DisplayName("personInfoFirstNameLastNameTest should return HTTPStatus.OK and a List containing a PersonAddressNameDTO")
		public void personInfoFirstNameLastNameTestShouldReturnHTTPStatusOKAndListPersonAddressNameDTO() {
			//GIVEN
			Optional<String> firstNameOpt = Optional.of("FirstName");
			Optional<String> lastNameOpt = Optional.of("LastName");
			List<PersonAddressNameDTO> personsAddressNameDTO = new ArrayList<>();
			PersonAddressNameDTO personAddressNameDTO = new PersonAddressNameDTO();
			personAddressNameDTO.setLastName("LastName");
			personAddressNameDTO.setAddress("address1");
			personAddressNameDTO.setAge("21");
			personAddressNameDTO.setEmail("email@email.com");
			personsAddressNameDTO.add(personAddressNameDTO);	
			try {
				when(personService.findPersonsByFirstNameAndLastName(firstNameOpt.get(), lastNameOpt.get(), request)).thenReturn(personsAddressNameDTO);
			} catch (ResourceNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResponseEntity<List<PersonAddressNameDTO>> responseEntity = null;
			
			//WHEN
			try {
				responseEntity = personController.personInfoFirstNameLastName(firstNameOpt, lastNameOpt, request);
			} catch (ResourceNotFoundException | BadRequestException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//THEN
			assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
			assertThat(responseEntity.getBody()).contains(personAddressNameDTO);
		}
		
		@Test
		@Tag("CornerCase")
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

	@Nested
    @Tag("POST")
    @DisplayName("POST /person create tests")
    class createPersonTests {
		
		@Test
		@Tag("NominalCase")
	    @DisplayName("createPersonTest should return HTTPStatus.OK and the PersonDTO created")
		public void createPersonTestShouldReturnHTTPStatusOKAndCreatedPerson() {
			//GIVEN
			PersonDTO personDTO = new PersonDTO();
			personDTO.setFirstName("FirstName");
			personDTO.setLastName("LastName");
			personDTO.setAddress("Address");
			personDTO.setCity("City");
			personDTO.setZip("12345");
			personDTO.setPhone("012-345-678");
			personDTO.setEmail("email@email.com");
			Optional<PersonDTO> personDTOOpt = Optional.of(personDTO);
			try {
				when(personService.createPerson(personDTO, request)).thenReturn(personDTO);
			} catch (ResourceConflictException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResponseEntity<PersonDTO> responseEntity = null;
			
			//WHEN
			try {
				responseEntity = personController.createPerson(personDTOOpt, request);
			} catch (ResourceConflictException | BadRequestException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//THEN
			assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
			assertThat(responseEntity.getBody()).isEqualTo(personDTO);
		}
		
		@Test
		@Tag("CornerCase")
	    @DisplayName("createPersonTest should throw BadRequestException")
		public void createPersonTestShouldThrowBadRequestException() {
			//GIVEN
			Optional<PersonDTO> personDTOOpt = Optional.ofNullable(null);
			//WHEN
			//THEN
			assertThrows(BadRequestException.class, () -> personController.createPerson(personDTOOpt, request));
		}
	}
	
	@Nested
    @Tag("PUT")
    @DisplayName("PUT /person update tests")
    class updatePersonTests {
		
		@Test
		@Tag("NominalCase")
	    @DisplayName("updatePersonTest should return HTTPStatus.OK and the PersonDTO updated")
		public void updatePersonTestShouldReturnHTTPStatusOKAndUpdatedPerson() {
			//GIVEN
			PersonDTO personDTO = new PersonDTO();
			personDTO.setFirstName("FirstName");
			personDTO.setLastName("LastName");
			personDTO.setAddress("Address");
			personDTO.setCity("City");
			personDTO.setZip("12345");
			personDTO.setPhone("012-345-678");
			personDTO.setEmail("email@email.com");
			Optional<PersonDTO> personDTOOpt = Optional.of(personDTO);
			try {
				when(personService.updatePerson(personDTO, request)).thenReturn(personDTO);
			} catch (ResourceNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResponseEntity<PersonDTO> responseEntity = null;
			
			//WHEN
			try {
				responseEntity = personController.updatePerson(personDTOOpt, request);
			} catch (ResourceNotFoundException | BadRequestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//THEN
			assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
			assertThat(responseEntity.getBody()).isEqualTo(personDTO);
		}
		
		@Test
		@Tag("CornerCase")
	    @DisplayName("updatePersonTest should throw BadRequestException")
		public void updatePersonTestShouldThrowBadRequestException() {
			//GIVEN
			Optional<PersonDTO> personDTOOpt = Optional.ofNullable(null);
			//WHEN
			//THEN
			assertThrows(BadRequestException.class, () -> personController.updatePerson(personDTOOpt, request));
		}
	}
	
	@Nested
    @Tag("DELETE")
    @DisplayName("DELETE /person delete tests")
    class deletePersonTests {
		
		@Test
		@Tag("NominalCase")
	    @DisplayName("deletePersonTest should return HTTPStatus.OK and a new PersonDTO")
		public void deletePersonTestShouldReturnHTTPStatusOKAndFieldsNullPerson() {
			//GIVEN
			PersonDTO personDTO = new PersonDTO();
			personDTO.setFirstName("FirstName");
			personDTO.setLastName("LastName");
			personDTO.setAddress("Address");
			personDTO.setCity("City");
			personDTO.setZip("12345");
			personDTO.setPhone("012-345-678");
			personDTO.setEmail("email@email.com");
			Optional<PersonDTO> personDTOOpt = Optional.of(personDTO);
			try {
				when(personService.deletePerson(personDTO, request)).thenReturn(new PersonDTO());
			} catch (ResourceNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResponseEntity<PersonDTO> responseEntity = null;
			
			//WHEN
			try {
				responseEntity = personController.deletePerson(personDTOOpt, request);
			} catch (ResourceNotFoundException | BadRequestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//THEN
			assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
			assertThat(responseEntity.getBody()).isEqualTo(new PersonDTO());
		}
		
		@Test
		@Tag("CornerCase")
	    @DisplayName("deletePersonTest should throw BadRequestException")
		public void deletePersonTestShouldThrowBadRequestException() {
			//GIVEN
			Optional<PersonDTO> personDTOOpt = Optional.ofNullable(null);
			//WHEN
			//THEN
			assertThrows(BadRequestException.class, () -> personController.deletePerson(personDTOOpt, request));
		}
	}
}
