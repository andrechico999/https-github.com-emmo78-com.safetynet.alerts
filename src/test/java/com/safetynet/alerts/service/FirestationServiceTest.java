package com.safetynet.alerts.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.dto.FirestationAddressPersonsDTO;
import com.safetynet.alerts.dto.FirestationDTO;
import com.safetynet.alerts.dto.FirestationPersonDTO;
import com.safetynet.alerts.dto.FirestationPersonDTOPerson;
import com.safetynet.alerts.dto.FirestationPersonDTOStats;
import com.safetynet.alerts.dto.FirestationPersonPhoneDTO;
import com.safetynet.alerts.dto.service.FirestationDTOService;
import com.safetynet.alerts.exception.BadRequestException;
import com.safetynet.alerts.exception.ResourceConflictException;
import com.safetynet.alerts.model.Address;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Medicalrecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.JsonRepository;
import com.safetynet.alerts.repository.WriteToFile;

@ExtendWith(MockitoExtension.class)
class FirestationServiceTest {

	@InjectMocks
	private FirestationServiceImpl firestationService;

	@Mock
	private JsonRepository jsonRepository;

	@Mock
	private FirestationDTOService firestationDTOService;

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private WriteToFile fileWriter;

	@Mock
	private RequestService requestService;

	private Person person1;
	private Person person2;
	private Person person3;
	private Person person4;
	private Person person5;
	private Person person6;

	private Map<Integer, Firestation> firestationsTest;
	private Map<String, Address> allAddressSTest;
	private MockHttpServletRequest requestMock;
	private WebRequest request;

	private static ModelMapper modelMapper;

	@BeforeAll
	public static void setUpForAllTests() {
		modelMapper = new ModelMapper();
		modelMapper.typeMap(FirestationDTO.class, Firestation.class).addMapping(FirestationDTO::getStation,
				Firestation::setStationNumber); // ModelMapper Handling Mismatches
		modelMapper.typeMap(Firestation.class, FirestationDTO.class).addMapping(Firestation::getStationNumber,
				FirestationDTO::setStation);
	}

	@AfterAll
	public static void unSetForAllTests() {
		modelMapper = null;
	}

	@BeforeEach
	public void setUpForEachTests() {
		Address address1 = new Address("address1", "city", "12345", new HashMap<Integer, Firestation>(),
				new HashMap<String, Person>());
		Address address2 = new Address("address2", "city", "12345", new HashMap<Integer, Firestation>(),
				new HashMap<String, Person>());
		Address address3 = new Address("address3", "city", "12345", new HashMap<Integer, Firestation>(),
				new HashMap<String, Person>());
		Address address4 = new Address("address4", "city", "12345", new HashMap<Integer, Firestation>(),
				new HashMap<String, Person>());

		person1 = new Person("FirstName1 LastName1", "FirstName1", "LastName1", "123-456-789", "person1@email.com",
				new Medicalrecord(), 11, new Address());
		person2 = new Person("FirstName2 LastName1", "FirstName2", "LastName1", "123-456-789", "person1@email.com",
				new Medicalrecord(), 22, new Address());
		person3 = new Person("FirstName3 LastName3", "FirstName3", "LastName3", "345-678-901", "person3@email.com",
				new Medicalrecord(), 33, new Address());
		person4 = new Person("FirstName4 LastName1", "FirstName4", "LastName1", "456-789-012", "person4@email.com",
				new Medicalrecord(), 44, new Address());
		person5 = new Person("FirstName5 LastName5", "FirstName5", "LastName5", "567-890-123", "person5@email.com",
				new Medicalrecord(), 55, new Address());
		person6 = new Person("FirstName6 LastName6", "FirstName6", "LastName6", "678-901-234", "person6@email.com",
				new Medicalrecord(), 66, new Address());

		Firestation firestation1 = new Firestation(1, new HashMap<String, Address>());
		Firestation firestation2 = new Firestation(2, new HashMap<String, Address>());

		person1.setAddress(address1); // address1.attach(this) So address1 has person 1,2,3
		person2.setAddress(address1); // address1.attach(this) and person 1,2 same lastName, phone, email
		person3.setAddress(address1); // address1.attach(this) and person 4 same lastName
		person4.setAddress(address2); // address2.attach(this)
		person5.setAddress(address3); // address3.attach(this)
		person6.setAddress(address4); // address4.attach(this)

		address1.putFirestation(firestation1); // firestation1.attachAddress(this); persons 1,2,3
		address2.putFirestation(firestation1); // firestation1.attachAddress(this); person 4
		address3.putFirestation(firestation1); // firestation1.attachAddress(this); person 5
		address3.putFirestation(firestation2); // firestation2.attachAddress(this); so address 3 map to fire stations 1
												// and 2
		// So no mapping for address 4 so person 6 not covered by a fire station

		firestationsTest = new HashMap<>();
		firestationsTest.put(firestation1.getStationNumber(), firestation1);
		firestationsTest.put(firestation2.getStationNumber(), firestation2);
		firestationService.setFirestations(firestationsTest);

		allAddressSTest = new HashMap<>();
		allAddressSTest.put(address1.getAddress(), address1);
		allAddressSTest.put(address2.getAddress(), address2);
		allAddressSTest.put(address3.getAddress(), address3);
		allAddressSTest.put(address4.getAddress(), address4);
		firestationService.setAllAddressS(allAddressSTest);

		requestMock = new MockHttpServletRequest();
		requestMock.setServerName("http://localhost:8080");
	}

	@AfterEach
	public void unSetForEachTests() {
		firestationsTest = null;
		firestationService.setFirestations(firestationsTest);
		allAddressSTest = null;
		firestationService.setAllAddressS(allAddressSTest);

		person1 = null;
		person2 = null;
		person3 = null;
		person4 = null;
		person5 = null;
		person6 = null;

		requestMock = null;
		request = null;
	}

	@Nested
	@Tag("GET")
	@DisplayName("GET /firestation?stationNumber=<station_number> tests")
	public class FindPersonsByFirestationTestClass {

		@Test
		@Tag("NominalCase")
		@DisplayName("findPersonsByFirestationTest should return list of people covered with a count of the number of adults and children")
		public void findPersonsByFirestationTestShouldReturnPeoplecoveredWithCountOfAdultsAndChildren() {

			// GIVEN
			requestMock.setRequestURI("/firestation");
			requestMock.setQueryString("?stationNumber=1"); // Request fire station 1
			requestMock.setMethod("GET");
			request = new ServletWebRequest(requestMock);
			modelMapper.typeMap(Person.class, FirestationPersonDTOPerson.class)
					.addMapping(src -> src.getAddress().getAddress(), FirestationPersonDTOPerson::setAddress);
			when(firestationDTOService.firestationPersonsToDTO(ArgumentMatchers.<Person>anyList())).then(invocation -> {
				List<Person> firestationPersons = invocation.getArgument(0);
				List<FirestationPersonDTO> firestationPersonsDTO = firestationPersons.stream()
						.map(person -> modelMapper.map(person, FirestationPersonDTOPerson.class))
						.collect(Collectors.toList());
				firestationPersonsDTO.add(new FirestationPersonDTOStats(2, 1));
				return firestationPersonsDTO;
			});
			when(requestService.requestToString(any(WebRequest.class))).thenReturn(
					requestMock.getMethod() + " : " + requestMock.getRequestURI() + requestMock.getQueryString());

			List<FirestationPersonDTO> firestationPersonsDTOExpected = new ArrayList<>();
			firestationPersonsDTOExpected.add(modelMapper.map(person1, FirestationPersonDTOPerson.class));
			firestationPersonsDTOExpected.add(modelMapper.map(person2, FirestationPersonDTOPerson.class));
			firestationPersonsDTOExpected.add(modelMapper.map(person3, FirestationPersonDTOPerson.class));
			firestationPersonsDTOExpected.add(modelMapper.map(person4, FirestationPersonDTOPerson.class));
			firestationPersonsDTOExpected.add(modelMapper.map(person5, FirestationPersonDTOPerson.class));
			firestationPersonsDTOExpected.add(new FirestationPersonDTOStats(2, 1));

			// WHEN
			List<FirestationPersonDTO> firestationPersonsDTOResult = null;
			try {
				firestationPersonsDTOResult = firestationService.findPersonsByFirestation("1", request);
			} catch (ResourceNotFoundException | BadRequestException e) {
				e.printStackTrace();
			}

			// THEN
			assertThat(firestationPersonsDTOResult).containsExactlyInAnyOrderElementsOf(firestationPersonsDTOExpected);
		}

		@Test
		@Tag("CornerCase")
		@DisplayName("findPersonsByFirestationTest should throw a BadRequestException")
		public void findPersonsByFirestationTestShouldThrowBadRequestException() {

			// GIVEN
			requestMock.setRequestURI("/firestation");
			requestMock.setQueryString("?stationNumber=a"); // Bad request fire station "a"
			requestMock.setMethod("GET");
			request = new ServletWebRequest(requestMock);

			// WHEN
			// THEN
			assertThrows(BadRequestException.class, () -> firestationService.findPersonsByFirestation("a", request));
		}

		@Test
		@Tag("CornerCase")
		@DisplayName("findPersonsByFirestationTest should throw a ResourceNotFoundException")
		public void findPersonsByFirestationTestShouldThrowResourceNotFoundException() {

			// GIVEN
			requestMock.setRequestURI("/firestation");
			requestMock.setQueryString("?stationNumber=3"); // Bad request fire station 3
			requestMock.setMethod("GET");
			request = new ServletWebRequest(requestMock);

			// WHEN
			// THEN
			assertThrows(ResourceNotFoundException.class,
					() -> firestationService.findPersonsByFirestation("3", request));
		}
	}

	@Nested
	@Tag("GET")
	@DisplayName("GET /phoneAlert?firestation=<firestation_number>")
	public class FindPersonPhonesByFirestationTestClass {

		@Test
		@Tag("NominalCase")
		@DisplayName("findPersonPhonesByFirestationTest should return distinct phone numbers served by the fire station")
		public void findPersonPhonesByFirestationTestShouldReturnDistinctPhoneNumbersServedByFirestation() {

			// GIVEN
			requestMock.setRequestURI("/phoneAlert");
			requestMock.setQueryString("?firestation=1"); // Request fire station 1
			requestMock.setMethod("GET");
			request = new ServletWebRequest(requestMock);
			when(firestationDTOService.firestationPersonsToPhonesDTO(ArgumentMatchers.<Person>anyList()))
					.then(invocation -> {
						List<Person> firestationPersons = invocation.getArgument(0);
						return firestationPersons.stream()
								.map(person -> modelMapper.map(person, FirestationPersonPhoneDTO.class)).distinct()
								.collect(Collectors.toList());
					});
			when(requestService.requestToString(any(WebRequest.class))).thenReturn(
					requestMock.getMethod() + " : " + requestMock.getRequestURI() + requestMock.getQueryString());

			List<FirestationPersonPhoneDTO> firestationPersonPhonesDTOExpected = new ArrayList<>();
			firestationPersonPhonesDTOExpected.add(modelMapper.map(person1, FirestationPersonPhoneDTO.class)); 
			// distinct : person 2 has same person 1
			firestationPersonPhonesDTOExpected.add(modelMapper.map(person3, FirestationPersonPhoneDTO.class));
			firestationPersonPhonesDTOExpected.add(modelMapper.map(person4, FirestationPersonPhoneDTO.class));
			firestationPersonPhonesDTOExpected.add(modelMapper.map(person5, FirestationPersonPhoneDTO.class));

			// WHEN
			List<FirestationPersonPhoneDTO> firestationPersonPhonesDTOResult = null;
			try {
				firestationPersonPhonesDTOResult = firestationService.findPersonPhonesByFirestation("1", request);
			} catch (ResourceNotFoundException | BadRequestException e) {
				e.printStackTrace();
			}

			// THEN
			assertThat(firestationPersonPhonesDTOResult)
					.containsExactlyInAnyOrderElementsOf(firestationPersonPhonesDTOExpected);
		}

		@Test
		@Tag("CornerCase")
		@DisplayName("findPersonPhonesByFirestationTest should throw a BadRequestException")
		public void findPersonsByFirestationTestShouldThrowBadRequestException() {

			// GIVEN
			requestMock.setRequestURI("/phoneAlert");
			requestMock.setQueryString("?firestation=a"); // Bad request fire station "a"
			requestMock.setMethod("GET");
			request = new ServletWebRequest(requestMock);

			// WHEN
			// THEN
			assertThrows(BadRequestException.class,
					() -> firestationService.findPersonPhonesByFirestation("a", request));
		}
	}

	@Nested
	@Tag("GET")
	@DisplayName("GET /flood/stations?stations=<a list of station_numbers> tests")
	public class FindAddressPersonsByFiresationsTestClass {

		@Test
		@Tag("NominalCase")
		@DisplayName("findAddressPersonsByFiresationsTest should return list of distinct households served by the barracks")
		public void findAddressPersonsByFiresationsTestShouldReturnListOfDistinctHouseholdsServed() {

			// GIVEN
			requestMock.setRequestURI("/flood/stations");
			requestMock.setQueryString("?stations=1,2,3"); // Request fire stations 1,2,3 will return for 1,2 because no
															// 3
			requestMock.setMethod("GET");
			request = new ServletWebRequest(requestMock);
			when(firestationDTOService.firestationsAddressToDTO(ArgumentMatchers.<Address>anyList()))
					.then(invocation -> {
						List<Address> firestationsAddress = invocation.getArgument(0);
						return firestationsAddress.stream()
								.map(address -> modelMapper.map(address, FirestationAddressPersonsDTO.class))
								.collect(Collectors.toList());
					});
			when(requestService.requestToString(any(WebRequest.class))).thenReturn(
					requestMock.getMethod() + " : " + requestMock.getRequestURI() + requestMock.getQueryString());

			List<FirestationAddressPersonsDTO> firestationsAddressPersonsDTOExpected = new ArrayList<>();
			firestationsAddressPersonsDTOExpected
					.add(modelMapper.map(allAddressSTest.get("address1"), FirestationAddressPersonsDTO.class));
			firestationsAddressPersonsDTOExpected
					.add(modelMapper.map(allAddressSTest.get("address2"), FirestationAddressPersonsDTO.class));
			firestationsAddressPersonsDTOExpected
					.add(modelMapper.map(allAddressSTest.get("address3"), FirestationAddressPersonsDTO.class));
			// Distinct because served by fire station 1 and 2

			// WHEN
			List<FirestationAddressPersonsDTO> firestationsAddressPersonsDTOResult = null;
			try {
				firestationsAddressPersonsDTOResult = firestationService
						.findAddressPersonsByFiresations(Arrays.asList("1", "2", "3"), request);
			} catch (ResourceNotFoundException | BadRequestException e) {
				e.printStackTrace();
			}

			// THEN
			assertThat(firestationsAddressPersonsDTOResult)
					.containsExactlyInAnyOrderElementsOf(firestationsAddressPersonsDTOExpected);
		}

		@Test
		@Tag("CornerCase")
		@DisplayName("findAddressPersonsByFiresationsTest should throw a BadRequestException")
		public void findAddressPersonsByFiresationsTestShouldThrowBadRequestException() {

			// GIVEN
			requestMock.setRequestURI("/flood/stations");
			requestMock.setQueryString("?stations=1,a,3"); // Bad request fire station "a"
			requestMock.setMethod("GET");
			request = new ServletWebRequest(requestMock);

			// WHEN
			// THEN
			assertThrows(BadRequestException.class,
					() -> firestationService.findAddressPersonsByFiresations(Arrays.asList("1", "a", "3"), request));
		}

		@Test
		@Tag("CornerCase")
		@DisplayName("findAddressPersonsByFiresationsTest should throw a ResourceNotFoundException")
		public void findAddressPersonsByFiresationsTestShouldThrowResourceNotFoundException() {

			// GIVEN
			requestMock.setRequestURI("/flood/stations");
			requestMock.setQueryString("?stations=3,4,5"); // Request for stations 3,4,5 not existing
			requestMock.setMethod("GET");
			request = new ServletWebRequest(requestMock);

			// WHEN
			// THEN
			assertThrows(ResourceNotFoundException.class,
					() -> firestationService.findAddressPersonsByFiresations(Arrays.asList("3", "4", "6"), request));
		}
	}

	@Nested
	@Tag("POST")
	@DisplayName("POST /firestation create tests")
	public class CreateFirestationTestClass {

		@Test
		@Tag("NominalCase")
		@DisplayName("createFirestationTest should create mapping address to firestation in IoC and return it")
		public void createFirestationTestShouldCreateMappingAddressFirestationInIoCAndReturnIt() {
			// GIVEN
			requestMock.setRequestURI("/firestation");
			requestMock.setMethod("POST");
			request = new ServletWebRequest(requestMock);
			FirestationDTO firestationDTOExpected = new FirestationDTO("address4", "2");
			when(firestationDTOService.convertFirestationFromDTO(any(FirestationDTO.class))).then(invocation -> {
				FirestationDTO firestationDTO = invocation.getArgument(0, FirestationDTO.class);
				Firestation firestation = modelMapper.map(firestationDTO, Firestation.class);
				firestation.attachAddress(new Address(firestationDTO.getAddress()));
				return firestation;
			});
			when(requestService.requestToString(any(WebRequest.class)))
					.thenReturn(requestMock.getMethod() + " : " + requestMock.getRequestURI());
			when(firestationDTOService.convertFirestationToDTO(any(Firestation.class), any(String.class)))
					.then(invocation -> {
						FirestationDTO firestationDTO = modelMapper.map(invocation.getArgument(0, Firestation.class),
								FirestationDTO.class);
						firestationDTO.setAddress(invocation.getArgument(1, String.class));
						return firestationDTO;
					});
			// WHEN
			FirestationDTO firestationDTOResult = null;
			try {
				firestationDTOResult = firestationService.addMappingAddressToFirestation(firestationDTOExpected,
						request);
			} catch (ResourceConflictException | ResourceNotFoundException e) {
				e.printStackTrace();
			}
			// THEN
			assertThat(firestationDTOResult).isEqualTo(firestationDTOExpected);
			assertThat(allAddressSTest.get("address4").getFirestations()).containsKey(2);
			assertThat(firestationsTest.get(2).getAddressS()).containsKey("address4");
		}

		@Test
		@Tag("CornerCase")
		@DisplayName("createFirestationTest should throw a ResourceConflictException")
		public void createFirestationTestShouldThrowResourceConflictException() {
			// GIVEN
			request = new ServletWebRequest(requestMock);
			FirestationDTO firestationDTOExpected = new FirestationDTO("address2", "2");
			when(firestationDTOService.convertFirestationFromDTO(any(FirestationDTO.class))).then(invocation -> {
				FirestationDTO firestationDTO = invocation.getArgument(0, FirestationDTO.class);
				Firestation firestation = modelMapper.map(firestationDTO, Firestation.class);
				firestation.attachAddress(new Address(firestationDTO.getAddress()));
				return firestation;
			});
			// WHEN
			// THEN
			assertThrows(ResourceConflictException.class,
					() -> firestationService.addMappingAddressToFirestation(firestationDTOExpected, request));
		}

		@ParameterizedTest(name = "{0} {1} , firestation {2} {3}")
		@CsvSource({ "address5, notexist, 1, exist", "address1,    exist, 3, notexist",
				"address5, notexist, 3, notexist" })
		@Tag("CornerCase")
		@DisplayName("createFirestationTest should throw a ResourceNotFoundException")
		public void updateFirestationTestShouldThrowResourceNotFoundException(String address, String foundA,
				String station, String foundF) {
			// GIVEN
			request = new ServletWebRequest(requestMock);
			FirestationDTO firestationDTOExpected = new FirestationDTO(address, station);
			when(firestationDTOService.convertFirestationFromDTO(any(FirestationDTO.class))).then(invocation -> {
				FirestationDTO firestationDTO = invocation.getArgument(0, FirestationDTO.class);
				Firestation firestation = modelMapper.map(firestationDTO, Firestation.class);
				firestation.attachAddress(new Address(firestationDTO.getAddress()));
				return firestation;
			});
			// WHEN
			// THEN
			assertThrows(ResourceNotFoundException.class,
					() -> firestationService.addMappingAddressToFirestation(firestationDTOExpected, request));
		}
	}

	@Nested
	@Tag("PUT")
	@DisplayName("PUT /firestation update tests")
	public class UpdateFirestationTestClass {

		@Test
		@Tag("NominalCase")
		@DisplayName("updateFirestationTest should update mapping address to one firestation in IoC and return it")
		public void updateFirestationTestShouldUpdateMappingAddressToOneFirestationAndReturnIt() {
			// GIVEN
			requestMock.setRequestURI("/firestation");
			requestMock.setMethod("PUT");
			request = new ServletWebRequest(requestMock);
			FirestationDTO firestationDTOExpected = new FirestationDTO("address3", "2");
			when(firestationDTOService.convertFirestationFromDTO(any(FirestationDTO.class))).then(invocation -> {
				FirestationDTO firestationDTO = invocation.getArgument(0, FirestationDTO.class);
				Firestation firestation = modelMapper.map(firestationDTO, Firestation.class);
				firestation.attachAddress(new Address(firestationDTO.getAddress()));
				return firestation;
			});
			when(requestService.requestToString(any(WebRequest.class)))
					.thenReturn(requestMock.getMethod() + " : " + requestMock.getRequestURI());
			when(firestationDTOService.convertFirestationToDTO(any(Firestation.class), any(String.class)))
					.then(invocation -> {
						FirestationDTO firestationDTO = modelMapper.map(invocation.getArgument(0, Firestation.class),
								FirestationDTO.class);
						firestationDTO.setAddress(invocation.getArgument(1, String.class));
						return firestationDTO;
					});
			// WHEN
			FirestationDTO firestationDTOResult = null;
			try {
				firestationDTOResult = firestationService.updateMappingAddressToFirestation(firestationDTOExpected,
						request);
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
			}
			// THEN
			assertThat(firestationDTOResult).isEqualTo(firestationDTOExpected);
			assertThat(allAddressSTest.get("address3").getFirestations()).containsKey(2).doesNotContainKey(1);
			assertThat(firestationsTest.get(2).getAddressS()).containsKey("address3");
		}

		@ParameterizedTest(name = "{0} {1} , firestation {2} {3}")
		@CsvSource({ "address5, notexist, 1, exist", "address1,    exist, 3, notexist",
				"address5, notexist, 3, notexist" })
		@Tag("CornerCase")
		@DisplayName("updateFirestationTest should throw a ResourceNotFoundException")
		public void updateFirestationTestShouldThrowResourceNotFoundException(String address, String foundA,
				String station, String foundF) {
			// GIVEN
			request = new ServletWebRequest(requestMock);
			FirestationDTO firestationDTOExpected = new FirestationDTO(address, station);
			when(firestationDTOService.convertFirestationFromDTO(any(FirestationDTO.class))).then(invocation -> {
				FirestationDTO firestationDTO = invocation.getArgument(0, FirestationDTO.class);
				Firestation firestation = modelMapper.map(firestationDTO, Firestation.class);
				firestation.attachAddress(new Address(firestationDTO.getAddress()));
				return firestation;
			});
			// WHEN
			// THEN
			assertThrows(ResourceNotFoundException.class,
					() -> firestationService.updateMappingAddressToFirestation(firestationDTOExpected, request));
		}
	}

	@Nested
	@Tag("DELETE")
	@DisplayName("DELETE /firestation delete tests")
	public class DeleteFirestationTestClass {

		@Test
		@Tag("NominalCase")
		@DisplayName("deleteFirestationTest address should delete mapping of address in Ioc and return a firestation with field address null")
		public void deleteFirestationTestAddressShouldDeleteMappingOfAddressReturnFirestationNullAdddressField() {
			// GIVEN
			requestMock.setRequestURI("/firestation");
			requestMock.setMethod("DELETE");
			request = new ServletWebRequest(requestMock);
			FirestationDTO firestationDTOExpected = new FirestationDTO(null, "1");
			when(firestationDTOService.convertFirestationFromDTO(any(FirestationDTO.class))).then(invocation -> {
				FirestationDTO firestationDTO = invocation.getArgument(0, FirestationDTO.class);
				Firestation firestation = modelMapper.map(firestationDTO, Firestation.class);
				firestation.attachAddress(new Address(firestationDTO.getAddress()));
				return firestation;
			});
			when(requestService.requestToString(any(WebRequest.class)))
					.thenReturn(requestMock.getMethod() + " : " + requestMock.getRequestURI());
			when(firestationDTOService.convertFirestationToDTO(any(Firestation.class), isNull())).then(invocation -> {
				Firestation firestation = invocation.getArgument(0, Firestation.class);
				FirestationDTO firestationDTO = modelMapper.map(firestation ,FirestationDTO.class);
				firestationDTO.setAddress(null);			
				return firestationDTO;
			});

			// WHEN
			FirestationDTO firestationDTOResult = null;
			try {
				firestationDTOResult = firestationService.deleteMappingAddressToFirestation(firestationDTOExpected, request);
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
			}
			// THEN
			assertThat(firestationDTOResult).isEqualTo(firestationDTOExpected);
			assertThat(firestationsTest.get(1).getAddressS()).isEmpty();
			assertThat(allAddressSTest.values().stream().flatMap(address->address.getFirestations().keySet().stream()).collect(Collectors.toSet())).doesNotContain(1);
		}
		
		@Test
		@Tag("NominalCase")
		@DisplayName("deleteFirestationTest firestation should delete mapping of firestation return station 0")
		public void deleteFirestationTestFirestationShouldDeleteMappingOfFirestationReturnStation0() {
			// GIVEN
			requestMock.setRequestURI("/firestation");
			requestMock.setMethod("DELETE");
			request = new ServletWebRequest(requestMock);
			FirestationDTO firestationDTOToDelete = new FirestationDTO("address3", null);
			FirestationDTO firestationDTOExpected = new FirestationDTO("address3", "0");
			when(firestationDTOService.convertFirestationFromDTO(any(FirestationDTO.class))).then(invocation -> {
				FirestationDTO firestationDTO = invocation.getArgument(0, FirestationDTO.class);
				Firestation firestation = modelMapper.map(firestationDTO, Firestation.class);
				firestation.attachAddress(new Address(firestationDTO.getAddress()));
				return firestation;
			});
			when(requestService.requestToString(any(WebRequest.class)))
					.thenReturn(requestMock.getMethod() + " : " + requestMock.getRequestURI());
			when(firestationDTOService.convertFirestationToDTO(any(Firestation.class), any(String.class)))
			.then(invocation -> {
				FirestationDTO firestationDTO = modelMapper.map(invocation.getArgument(0, Firestation.class),
						FirestationDTO.class);
				firestationDTO.setAddress(invocation.getArgument(1, String.class));
				return firestationDTO;
			});

			// WHEN
			FirestationDTO firestationDTOResult = null;
			try {
				firestationDTOResult = firestationService.deleteMappingAddressToFirestation(firestationDTOToDelete, request);
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
			}
			// THEN
			assertThat(firestationDTOResult).isEqualTo(firestationDTOExpected);
			assertThat(allAddressSTest.get("address3").getFirestations()).isEmpty();
			assertThat(firestationsTest.values().stream().flatMap(firestation -> firestation.getAddressS().keySet().stream()).collect(Collectors.toSet())).doesNotContain("address3");
		}

		@Test
		@Tag("NominalCase")
		@DisplayName("deleteFirestationTest mapping should delete mapping of address to firestation return new firestation")
		public void deleteFirestationTestMappingShouldDeleteMappingOfFirestationReturnStation0() {
			// GIVEN
			requestMock.setRequestURI("/firestation");
			requestMock.setMethod("DELETE");
			request = new ServletWebRequest(requestMock);
			FirestationDTO firestationDTOToDelete = new FirestationDTO("address3", "1");
			FirestationDTO firestationDTOExpected = new FirestationDTO(null, "0");
			when(firestationDTOService.convertFirestationFromDTO(any(FirestationDTO.class))).then(invocation -> {
				FirestationDTO firestationDTO = invocation.getArgument(0, FirestationDTO.class);
				Firestation firestation = modelMapper.map(firestationDTO, Firestation.class);
				firestation.attachAddress(new Address(firestationDTO.getAddress()));
				return firestation;
			});
			when(requestService.requestToString(any(WebRequest.class)))
					.thenReturn(requestMock.getMethod() + " : " + requestMock.getRequestURI());
			when(firestationDTOService.convertFirestationToDTO(any(Firestation.class), any(String.class)))
			.then(invocation -> {
				FirestationDTO firestationDTO = modelMapper.map(invocation.getArgument(0, Firestation.class),
						FirestationDTO.class);
				firestationDTO.setAddress(null);
				return firestationDTO;
			});

			// WHEN
			FirestationDTO firestationDTOResult = null;
			try {
				firestationDTOResult = firestationService.deleteMappingAddressToFirestation(firestationDTOToDelete, request);
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
			}
			// THEN
			assertThat(firestationDTOResult).isEqualTo(firestationDTOExpected);
			assertThat(allAddressSTest.get("address3").getFirestations()).doesNotContainKey(1).containsKey(2);
			assertThat(firestationsTest.get(1).getAddressS()).doesNotContainKey("address3").containsKey("address1");
		}

		@ParameterizedTest(name = "{0} {1} , firestation {2} {3}")
		@CsvSource({ "address5, notexist, 1, exist", "address1,    exist, 3, notexist",
				"address5, notexist, 3, notexist" })
		@Tag("CornerCase")
		@DisplayName("deleteFirestationTest should throw a ResourceNotFoundException")
		public void deleteFirestationTestShouldThrowResourceNotFoundException(String address, String foundA,
				String station, String foundF) {
			// GIVEN
			request = new ServletWebRequest(requestMock);
			FirestationDTO firestationDTOExpected = new FirestationDTO(address, station);
			when(firestationDTOService.convertFirestationFromDTO(any(FirestationDTO.class))).then(invocation -> {
				FirestationDTO firestationDTO = invocation.getArgument(0, FirestationDTO.class);
				Firestation firestation = modelMapper.map(firestationDTO, Firestation.class);
				firestation.attachAddress(new Address(firestationDTO.getAddress()));
				return firestation;
			});
			// WHEN
			// THEN
			assertThrows(ResourceNotFoundException.class,
					() -> firestationService.deleteMappingAddressToFirestation(firestationDTOExpected, request));
		}
		
		@Test
		@Tag("CornerCase")
		@DisplayName("deleteFirestationTest NoMapping should throw a ResourceNotFoundException")
		public void deleteFirestationTestNoMapShouldThrowResourceNotFoundException() {
			// GIVEN
			request = new ServletWebRequest(requestMock);
			FirestationDTO firestationDTOExpected = new FirestationDTO("address4","2");
			when(firestationDTOService.convertFirestationFromDTO(any(FirestationDTO.class))).then(invocation -> {
				FirestationDTO firestationDTO = invocation.getArgument(0, FirestationDTO.class);
				Firestation firestation = modelMapper.map(firestationDTO, Firestation.class);
				firestation.attachAddress(new Address(firestationDTO.getAddress()));
				return firestation;
			});
			// WHEN
			// THEN
			assertThrows(ResourceNotFoundException.class,
					() -> firestationService.deleteMappingAddressToFirestation(firestationDTOExpected, request));
		}
		
		@Test
		@Tag("CornerCase")
		@DisplayName("deleteFirestationTest NoAddress Mapping should throw a ResourceNotFoundException")
		public void deleteFirestationTestNoAddressMapShouldThrowResourceNotFoundException() {
			// GIVEN
			request = new ServletWebRequest(requestMock);
			FirestationDTO firestationDTOExpected = new FirestationDTO("address4",null);
			when(firestationDTOService.convertFirestationFromDTO(any(FirestationDTO.class))).then(invocation -> {
				FirestationDTO firestationDTO = invocation.getArgument(0, FirestationDTO.class);
				Firestation firestation = modelMapper.map(firestationDTO, Firestation.class);
				firestation.attachAddress(new Address(firestationDTO.getAddress()));
				return firestation;
			});
			// WHEN
			// THEN
			assertThrows(ResourceNotFoundException.class,
					() -> firestationService.deleteMappingAddressToFirestation(firestationDTOExpected, request));
		}
		
		@Test
		@Tag("CornerCase")
		@DisplayName("deleteFirestationTest NoStation Mapping should throw a ResourceNotFoundException")
		public void deleteFirestationTestNoStationMapShouldThrowResourceNotFoundException() {
			// GIVEN
			request = new ServletWebRequest(requestMock);
			FirestationDTO firestationDTOExpected = new FirestationDTO(null, "2");
			firestationsTest.get(2).detachAddress(allAddressSTest.get("address3"));
			when(firestationDTOService.convertFirestationFromDTO(any(FirestationDTO.class))).then(invocation -> {
				FirestationDTO firestationDTO = invocation.getArgument(0, FirestationDTO.class);
				Firestation firestation = modelMapper.map(firestationDTO, Firestation.class);
				firestation.attachAddress(new Address(firestationDTO.getAddress()));
				return firestation;
			});
			// WHEN
			// THEN
			assertThrows(ResourceNotFoundException.class,
					() -> firestationService.deleteMappingAddressToFirestation(firestationDTOExpected, request));
		}
	}
}
