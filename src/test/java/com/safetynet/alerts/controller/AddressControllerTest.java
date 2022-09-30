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

import com.safetynet.alerts.dto.AddressAdultChildDTO;
import com.safetynet.alerts.dto.AddressChildDTO;
import com.safetynet.alerts.dto.AddressPersonDTO;
import com.safetynet.alerts.dto.AddressPersonDTOPerson;
import com.safetynet.alerts.dto.AddressPersonEmailDTO;
import com.safetynet.alerts.exception.BadRequestException;
import com.safetynet.alerts.service.AddressService;

@ExtendWith(MockitoExtension.class)
public class AddressControllerTest {
	@InjectMocks
	private AddressController addressController;

	@Mock
	private AddressService addressService;

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
	@Tag("childAlertAddress tests")
	@DisplayName("childAlertAddress tests")
	class AddressStationNumber {

		@Test
		@Tag("Nominal case HTTPStatus.OK")
		@DisplayName("childAlertAddressTest should return HTTPStatus.OK and a List containing an AddressChildDTO")
		public void childAlertAddressTestShouldReturnHTTPStatusOKAndListAddressAdultChildDTO() {
			// GIVEN
			Optional<String> addressOpt = Optional.of("Address");
			List<AddressAdultChildDTO> addressAdultChildsDTO = new ArrayList<>();
			AddressChildDTO addressChildDTO = new AddressChildDTO();
			addressChildDTO.setFirstName("FirstName");
			addressChildDTO.setLastName("LastName");
			addressChildDTO.setAge("5");
			addressChildDTO.setAdults(new ArrayList<AddressAdultChildDTO>());
			addressAdultChildsDTO.add(addressChildDTO);
			try {
				when(addressService.findChildrenByAddress("Address", request)).thenReturn(addressAdultChildsDTO);
			} catch (ResourceNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResponseEntity<List<AddressAdultChildDTO>> responseEntity = null;

			// WHEN
			try {
				responseEntity = addressController.childAlertAddress(addressOpt, request);
			} catch (ResourceNotFoundException | BadRequestException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// THEN
			assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
			assertThat(responseEntity.getBody()).contains(addressChildDTO);
		}

		@Test
		@Tag("Corner case BadRequestException")
		@DisplayName("childAlertAddressTest should throw BadRequestException")
		public void childAlertAddressTestShouldThrowBadRequestException() {
			// GIVEN
			Optional<String> addressOpt = Optional.ofNullable(null);
			// WHEN
			// THEN
			assertThrows(BadRequestException.class,
					() -> addressController.childAlertAddress(addressOpt, request));
		}
	}

	@Nested
	@Tag("fireAddress tests")
	@DisplayName("fireAddress tests")
	class FireAddress {

		@Test
		@Tag("Nominal case HTTPStatus.OK")
		@DisplayName("fireAddressTest should return HTTPStatus.OK and a List containing an AddressPersonDTO")
		public void fireAddressTestShouldReturnHTTPStatusOKAndListAddressPersonDTO() {
			// GIVEN
			Optional<String> addressOpt = Optional.of("Address");
			List<AddressPersonDTO> addressPersonsDTO = new ArrayList<>();
			AddressPersonDTOPerson addressPersonDTO = new AddressPersonDTOPerson();
			addressPersonDTO.setLastName("LastName");
			addressPersonDTO.setPhone("123-456-789");
			addressPersonDTO.setAge("25");
			addressPersonDTO.setMedications(new ArrayList<String>());
			addressPersonDTO.setAllergies(new ArrayList<String>());
			addressPersonsDTO.add(addressPersonDTO);
			try {
				when(addressService.findPersonsByAddress("Address", request)).thenReturn(addressPersonsDTO);
			} catch (ResourceNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResponseEntity<List<AddressPersonDTO>> responseEntity = null;

			// WHEN
			try {
				responseEntity = addressController.fireAddress(addressOpt, request);
			} catch (ResourceNotFoundException | BadRequestException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// THEN
			assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
			assertThat(responseEntity.getBody()).contains(addressPersonDTO);
		}

		@Test
		@Tag("Corner case BadRequestException")
		@DisplayName("fireAddressTest should throw BadRequestException")
		public void fireAddressTestShouldThrowBadRequestException() {
			// GIVEN
			Optional<String> addressOpt = Optional.ofNullable(null);
			// WHEN
			// THEN
			assertThrows(BadRequestException.class,
					() -> addressController.fireAddress(addressOpt, request));
		}
	}

	@Nested
	@Tag("communityEmailCity tests")
	@DisplayName("communityEmailCity tests")
	class CommunityEmailCity {

		@Test
		@Tag("Nominal case HTTPStatus.OK")
		@DisplayName("communityEmailCityTest should return HTTPStatus.OK and a List containing a AddressPersonEmailDTO")
		public void communityEmailCityTestShouldReturnHTTPStatusOKAndListAddressPersonEmailDTO() {
			// GIVEN
			Optional<String> cityOpt = Optional.of("City");
			List<AddressPersonEmailDTO> addressPersonEmailsDTO = new ArrayList<>();
			AddressPersonEmailDTO addressPersonEmailDTO = new AddressPersonEmailDTO();
			addressPersonEmailDTO.setEmail("email@email.com");
			addressPersonEmailsDTO.add(addressPersonEmailDTO);
			try {
				when(addressService.findemailPersonsByCity("City", request)).thenReturn(addressPersonEmailsDTO);
			} catch (ResourceNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResponseEntity<List<AddressPersonEmailDTO>> responseEntity = null;

			// WHEN
			try {
				responseEntity = addressController.communityEmailCity(cityOpt, request);
			} catch (ResourceNotFoundException | BadRequestException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// THEN
			assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
			assertThat(responseEntity.getBody()).contains(addressPersonEmailDTO);
		}

		@Test
		@Tag("Corner case BadRequestException")
		@DisplayName("communityEmailCityTest should throw BadRequestException")
		public void communityEmailCityTestShouldThrowBadRequestException() {
			// GIVEN
			Optional<String> cityOpt = Optional.ofNullable(null);
			// WHEN
			// THEN
			assertThrows(BadRequestException.class,
					() -> addressController.communityEmailCity(cityOpt, request));
		}
	}

}
