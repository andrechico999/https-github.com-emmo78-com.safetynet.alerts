package com.safetynet.alerts.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.safetynet.alerts.dto.AddressPersonDTO;
import com.safetynet.alerts.dto.FirestationAddressPersonsDTO;
import com.safetynet.alerts.dto.FirestationDTO;
import com.safetynet.alerts.dto.FirestationPersonDTO;
import com.safetynet.alerts.dto.FirestationPersonDTOPerson;
import com.safetynet.alerts.dto.FirestationPersonPhoneDTO;
import com.safetynet.alerts.exception.BadRequestException;
import com.safetynet.alerts.exception.ResourceConflictException;
import com.safetynet.alerts.service.FirestationService;


@ExtendWith(MockitoExtension.class)
public class FirestationControllerTest {

	@InjectMocks
	private FirestationController firestationController;
	
	@Mock
	private FirestationService firestationService;
	
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
    @DisplayName("GET /firestation?stationNumber=<station_number> tests")
    class FirestationStationNumber {
		
		@Test
		@Tag("NominalCase")
	    @DisplayName("firestationStationNumberTest should return HTTPStatus.OK and a List containing a FirestationPersonDTO")
		public void firestationStationNumberTestShouldReturnHTTPStatusOKAndListFirestationPersonDTO() {
			//GIVEN
			Optional<String> stationNumberOpt = Optional.of("1");
			List<FirestationPersonDTO> firestationPersonsDTO = new ArrayList<>();
			FirestationPersonDTOPerson firestationPersonDTO = new FirestationPersonDTOPerson();
			firestationPersonDTO.setFirstName("FirstName");
			firestationPersonDTO.setLastName("LastName");
			firestationPersonDTO.setAddress("Address");
			firestationPersonDTO.setPhone("123-456-789");
			firestationPersonsDTO.add(firestationPersonDTO);	
			try {
				when(firestationService.findPersonsByFirestation("1", request)).thenReturn(firestationPersonsDTO);
			} catch (ResourceNotFoundException | BadRequestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResponseEntity<List<FirestationPersonDTO>> responseEntity = null;
			
			//WHEN
			try {
				responseEntity = firestationController.firestationStationNumber(stationNumberOpt, request);
			} catch (ResourceNotFoundException | BadRequestException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//THEN
			assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
			assertThat(responseEntity.getBody()).contains(firestationPersonDTO);
		}
		
		@Test
		@Tag("CornerCase")
	    @DisplayName("firestationStationNumberTest should throw BadRequestException")
		public void firestationStationNumberTestShouldThrowBadRequestException() {
			//GIVEN
			Optional<String> stationNumberOpt = Optional.ofNullable(null);
			//WHEN
			//THEN
			assertThrows(BadRequestException.class, () -> firestationController.firestationStationNumber(stationNumberOpt, request));
		}
	}

	@Nested
    @Tag("GET")
    @DisplayName("GET /phoneAlert?firestation=<firestation_number> tests")
    class PhoneAlertFirestationNumber {
		
		@Test
		@Tag("NominalCase")
	    @DisplayName("phoneAlertFirestationNumberTest should return HTTPStatus.OK and a List containing a FirestationPersonPhoneDTO")
		public void phoneAlertFirestationNumberTestShouldReturnHTTPStatusOKAndListFirestationPersonPhoneDTO() {
			//GIVEN
			Optional<String> stationNumberOpt = Optional.of("1");
			List<FirestationPersonPhoneDTO> firestationPersonPhonesDTO = new ArrayList<>();
			FirestationPersonPhoneDTO firestationPersonPhoneDTO = new FirestationPersonPhoneDTO();
			firestationPersonPhoneDTO.setPhone("123-456-789");
			firestationPersonPhonesDTO.add(firestationPersonPhoneDTO);	
			try {
				when(firestationService.findPersonPhonesByFirestation("1", request)).thenReturn(firestationPersonPhonesDTO);
			} catch (ResourceNotFoundException | BadRequestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResponseEntity<List<FirestationPersonPhoneDTO>> responseEntity = null;
			
			//WHEN
			try {
				responseEntity = firestationController.phoneAlertFirestationNumber(stationNumberOpt, request);
			} catch (ResourceNotFoundException | BadRequestException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//THEN
			assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
			assertThat(responseEntity.getBody()).contains(firestationPersonPhoneDTO);
		}
		
		@Test
		@Tag("CornerCase")
	    @DisplayName("phoneAlertFirestationNumberTest should throw BadRequestException")
		public void phoneAlertFirestationNumberTestShouldThrowBadRequestException() {
			//GIVEN
			Optional<String> stationNumberOpt = Optional.ofNullable(null);
			//WHEN
			//THEN
			assertThrows(BadRequestException.class, () -> firestationController.phoneAlertFirestationNumber(stationNumberOpt, request));
		}
	}

	@Nested
    @Tag("GET")
    @DisplayName("GET /flood/stations?stations=<a list of station_numbers> tests")
    class FloodStationNumbers {
		
		@Test
		@Tag("NominalCase")
	    @DisplayName("floodStationNumbersTest should return HTTPStatus.OK and a List containing a FirestationAddressPersonsDTO")
		public void floodStationNumbersTestShouldReturnHTTPStatusOKAndListFirestationAddressPersonsDTO() {
			//GIVEN
			
			Optional<List<String>> stationNumbersOpt = Optional.of(Arrays.asList("1", "2", "3", "4"));
			List<FirestationAddressPersonsDTO> firestationAddressPersonsDTO = new ArrayList<>();
			FirestationAddressPersonsDTO firestationAddressPersonDTO = new FirestationAddressPersonsDTO();
			firestationAddressPersonDTO.setAddress("Address");
			firestationAddressPersonDTO.setCity("City");
			firestationAddressPersonDTO.setZip("12345");
			firestationAddressPersonDTO.setHouseHold(new ArrayList<AddressPersonDTO>());
			firestationAddressPersonsDTO.add(firestationAddressPersonDTO);	
			try {
				when(firestationService.findAddressPersonsByFiresations(stationNumbersOpt.get(), request)).thenReturn(firestationAddressPersonsDTO);
			} catch (ResourceNotFoundException | BadRequestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResponseEntity<List<FirestationAddressPersonsDTO>> responseEntity = null;
			
			//WHEN
			try {
				responseEntity = firestationController.floodStationNumbers(stationNumbersOpt, request);
			} catch (ResourceNotFoundException | BadRequestException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//THEN
			assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
			assertThat(responseEntity.getBody()).contains(firestationAddressPersonDTO);
		}
		
		@Test
		@Tag("CornerCase")
	    @DisplayName("floodStationNumbersTest should throw BadRequestException")
		public void floodStationNumbersTestShouldThrowBadRequestException() {
			//GIVEN
			Optional<List<String>> stationNumberOpt = Optional.ofNullable(null);
			//WHEN
			//THEN
			assertThrows(BadRequestException.class, () -> firestationController.floodStationNumbers(stationNumberOpt, request));
		}
	}
	
	@Nested
    @Tag("POST")
    @DisplayName("POST /firestation create tests")
    class createFirestationTests {
		
		@Test
		@Tag("NominalCase")
	    @DisplayName("createFirestationTest should return HTTPStatus.OK and the FirestationDTO created")
		public void createFirestationTestShouldReturnHTTPStatusOKAndCreatedFirestation() {
			//GIVEN
			FirestationDTO firestationDTO = new FirestationDTO();
			firestationDTO.setAddress("Address");
			firestationDTO.setStation("1");
			Optional<FirestationDTO> firestationDTOOpt = Optional.of(firestationDTO);
			try {
				when(firestationService.addMappingAddressToFirestation(firestationDTO, request)).thenReturn(firestationDTO);
			} catch (ResourceConflictException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResponseEntity<FirestationDTO> responseEntity = null;
			
			//WHEN
			try {
				responseEntity = firestationController.createFirestation(firestationDTOOpt, request);
			} catch (ResourceConflictException | BadRequestException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//THEN
			assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
			assertThat(responseEntity.getBody()).isEqualTo(firestationDTO);
		}
		
		@Test
		@Tag("CornerCase")
	    @DisplayName("createFirestationTest should throw BadRequestException")
		public void createFirestationTestShouldThrowBadRequestException() {
			//GIVEN
			Optional<FirestationDTO> firestationDTOOpt = Optional.ofNullable(null);
			//WHEN
			//THEN
			assertThrows(BadRequestException.class, () -> firestationController.createFirestation(firestationDTOOpt, request));
		}
	}
	
	@Nested
    @Tag("PUT")
    @DisplayName("PUT /firestation update tests")
    class updateFirestationTests {
		
		@Test
		@Tag("NominalCase")
	    @DisplayName("updateFirestationTest should return HTTPStatus.OK and the FirestationDTO updated")
		public void updateFirestationTestShouldReturnHTTPStatusOKAndUpdatedFirestation() {
			//GIVEN
			FirestationDTO firestationDTO = new FirestationDTO();
			firestationDTO.setAddress("Address");
			firestationDTO.setStation("1");
			Optional<FirestationDTO> firestationDTOOpt = Optional.of(firestationDTO);

			try {
				when(firestationService.updateMappingAddressToFirestation(firestationDTO, request)).thenReturn(firestationDTO);
			} catch (ResourceNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResponseEntity<FirestationDTO> responseEntity = null;
			
			//WHEN
			try {
				responseEntity = firestationController.updateFirestation(firestationDTOOpt, request);
			} catch (ResourceNotFoundException | BadRequestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//THEN
			assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
			assertThat(responseEntity.getBody()).isEqualTo(firestationDTO);
		}
		
		@Test
		@Tag("CornerCase")
	    @DisplayName("updateFirestationTest should throw BadRequestException")
		public void updateFirestationTestShouldThrowBadRequestException() {
			//GIVEN
			Optional<FirestationDTO> firestationDTOOpt = Optional.ofNullable(null);
			//WHEN
			//THEN
			assertThrows(BadRequestException.class, () -> firestationController.updateFirestation(firestationDTOOpt, request));
		}
	}
	
	@Nested
    @Tag("DELETE")
    @DisplayName("DELETE /firestation delete tests")
    class deleteFirestationTests {
		
		@Test
		@Tag("NominalCase")
	    @DisplayName("deleteFirestationTest should return HTTPStatus.OK and a new FirestationDTO")
		public void deleteFirestationTestShouldReturnHTTPStatusOKAndFieldsNullFirestation() {
			//GIVEN
			FirestationDTO firestationDTO = new FirestationDTO();
			firestationDTO.setAddress("Address");
			firestationDTO.setStation("1");
			Optional<FirestationDTO> firestationDTOOpt = Optional.of(firestationDTO);
			try {
				when(firestationService.deleteMappingAddressToFirestation(firestationDTO, request)).thenReturn(new FirestationDTO());
			} catch (ResourceNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResponseEntity<FirestationDTO> responseEntity = null;
			
			//WHEN
			try {
				responseEntity = firestationController.deleteFirestation(firestationDTOOpt, request);
			} catch (ResourceNotFoundException | BadRequestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//THEN
			assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
			assertThat(responseEntity.getBody()).isEqualTo(new FirestationDTO());
		}
		
		@Test
		@Tag("CornerCase")
	    @DisplayName("deleteFirestationTest should throw BadRequestException")
		public void deleteFirestationTestShouldThrowBadRequestException() {
			//GIVEN
			Optional<FirestationDTO> firestationDTOOpt = Optional.ofNullable(null);
			//WHEN
			//THEN
			assertThrows(BadRequestException.class, () -> firestationController.deleteFirestation(firestationDTOOpt, request));
		}
	}
}
