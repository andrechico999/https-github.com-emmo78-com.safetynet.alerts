package com.safetynet.alerts.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

import com.safetynet.alerts.dto.MedicalrecordDTO;
import com.safetynet.alerts.exception.BadRequestException;
import com.safetynet.alerts.exception.ResourceConflictException;
import com.safetynet.alerts.service.MedicalrecordService;


@ExtendWith(MockitoExtension.class)
public class MedicalrecordControllerTest {

	@InjectMocks
	private MedicalrecordController medicalrecordController;
	
	@Mock
	private MedicalrecordService medicalrecordService;
	
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
    @Tag("createMedicalrecord tests")
    @DisplayName("createMedicalrecord tests")
    class createMedicalrecordTests {
		
		@Test
		@Tag("Nominal case HTTPStatus.OK")
	    @DisplayName("createMedicalrecordTest should return HTTPStatus.OK and the medical record created")
		public void createMedicalrecordTestShouldReturnHTTPStatusOKAndMedcialrecordCreated() {
			//GIVEN
			MedicalrecordDTO medicalrecordDTO = new MedicalrecordDTO();
			medicalrecordDTO.setFirstName("FirstName");
			medicalrecordDTO.setLastName("LastName");
			medicalrecordDTO.setBirthdate(LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
			medicalrecordDTO.setMedications(new ArrayList<String>());
			medicalrecordDTO.setAllergies(new ArrayList<String>());
			Optional<MedicalrecordDTO> medicalrecordDTOOpt = Optional.of(medicalrecordDTO);
			try {
				when(medicalrecordService.createMedicalrecord(medicalrecordDTO, request)).thenReturn(medicalrecordDTO);
			} catch (ResourceConflictException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResponseEntity<MedicalrecordDTO> responseEntity = null;
			
			//WHEN
			try {
				responseEntity = medicalrecordController.createMedicalrecord(medicalrecordDTOOpt, request);
			} catch (ResourceConflictException | BadRequestException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//THEN
			assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
			assertThat(responseEntity.getBody()).isEqualTo(medicalrecordDTO);
		}
		
		@Test
		@Tag("Corner case BadRequestException")
	    @DisplayName("createMedicalrecordTest should throw BadRequestException")
		public void createMedicalrecordTestShouldThrowBadRequestException() {
			//GIVEN
			Optional<MedicalrecordDTO> medicalrecordDTOOpt = Optional.ofNullable(null);
			//WHEN
			//THEN
			assertThrows(BadRequestException.class, () -> medicalrecordController.createMedicalrecord(medicalrecordDTOOpt, request));
		}
	}
	
	@Nested
    @Tag("updateMedicalrecord tests")
    @DisplayName("updateMedicalrecord tests")
    class updateMedicalrecordTests {
		
		@Test
		@Tag("Nominal case HTTPStatus.OK")
	    @DisplayName("updateMedicalrecordTest should return HTTPStatus.OK and the medical record updated")
		public void updateMedicalrecordTestShouldReturnHTTPStatusOKAndUpdatedMedicalrecord() {
			//GIVEN
			MedicalrecordDTO medicalrecordDTO = new MedicalrecordDTO();
			medicalrecordDTO.setFirstName("FirstName");
			medicalrecordDTO.setLastName("LastName");
			medicalrecordDTO.setBirthdate(LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
			medicalrecordDTO.setMedications(new ArrayList<String>());
			medicalrecordDTO.setAllergies(new ArrayList<String>());
			Optional<MedicalrecordDTO> medicalrecordDTOOpt = Optional.of(medicalrecordDTO);
			try {
				when(medicalrecordService.updateMedicalrecord(medicalrecordDTO, request)).thenReturn(medicalrecordDTO);
			} catch (ResourceNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResponseEntity<MedicalrecordDTO> responseEntity = null;
			
			//WHEN
			try {
				responseEntity = medicalrecordController.updateMedicalrecord(medicalrecordDTOOpt, request);
			} catch (ResourceNotFoundException | BadRequestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//THEN
			assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
			assertThat(responseEntity.getBody()).isEqualTo(medicalrecordDTO);
		}
		
		@Test
		@Tag("Corner case BadRequestException")
	    @DisplayName("updateMedicalrecordTest should throw BadRequestException")
		public void updateMedicalrecordTestShouldThrowBadRequestException() {
			//GIVEN
			Optional<MedicalrecordDTO> medicalrecordDTOOpt = Optional.ofNullable(null);
			//WHEN
			//THEN
			assertThrows(BadRequestException.class, () -> medicalrecordController.updateMedicalrecord(medicalrecordDTOOpt, request));
		}
	}
	
	@Nested
    @Tag("deleteMedicalrecord tests")
    @DisplayName("deleteMedicalrecord tests")
    class deleteMedicalrecordTests {
		
		@Test
		@Tag("Nominal case HTTPStatus.OK")
	    @DisplayName("deleteMedicalrecordTest should return HTTPStatus.OK and a Medicalrecord with null field")
		public void deleteMedicalrecordTestShouldReturnHTTPStatusOKAndANullFieldsMedicalrecord() {
			//GIVEN
			MedicalrecordDTO medicalrecordDTO = new MedicalrecordDTO();
			medicalrecordDTO.setFirstName("FirstName");
			medicalrecordDTO.setLastName("LastName");
			medicalrecordDTO.setBirthdate(LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
			medicalrecordDTO.setMedications(new ArrayList<String>());
			medicalrecordDTO.setAllergies(new ArrayList<String>());
			Optional<MedicalrecordDTO> medicalrecordDTOOpt = Optional.of(medicalrecordDTO);
			try {
				when(medicalrecordService.deleteMedicalrecord(medicalrecordDTO, request)).thenReturn(new MedicalrecordDTO());
			} catch (ResourceNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResponseEntity<MedicalrecordDTO> responseEntity = null;
			
			//WHEN
			try {
				responseEntity = medicalrecordController.deleteMedicalrecord(medicalrecordDTOOpt, request);
			} catch (ResourceNotFoundException | BadRequestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//THEN
			assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
			assertThat(responseEntity.getBody()).isEqualTo(new MedicalrecordDTO());
		}
		
		@Test
		@Tag("Corner case BadRequestException")
	    @DisplayName("deleteMedicalrecordTest should throw BadRequestException")
		public void deleteMedicalrecordTestShouldThrowBadRequestException() {
			//GIVEN
			Optional<MedicalrecordDTO> medicalrecordDTOOpt = Optional.ofNullable(null);
			//WHEN
			//THEN
			assertThrows(BadRequestException.class, () -> medicalrecordController.deleteMedicalrecord(medicalrecordDTOOpt, request));
		}
	}
}
