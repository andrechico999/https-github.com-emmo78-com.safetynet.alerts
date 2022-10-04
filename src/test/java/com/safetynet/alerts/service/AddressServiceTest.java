package com.safetynet.alerts.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.dto.AddressAdultChildDTO;
import com.safetynet.alerts.dto.AddressChildDTO;
import com.safetynet.alerts.dto.AddressPersonDTO;
import com.safetynet.alerts.dto.AddressPersonDTOPerson;
import com.safetynet.alerts.dto.AddressPersonEmailDTO;
import com.safetynet.alerts.dto.service.AddressDTOServiceImpl;
import com.safetynet.alerts.model.Address;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Medicalrecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.JsonRepository;
import com.safetynet.alerts.repository.WriteToFile;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {
	
	@InjectMocks
	private AddressServiceImpl addressService;
	
	@Mock
	private JsonRepository jsonRepository;

	@Mock
	private AddressDTOServiceImpl addressDTOService;

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

	private Map<String, Address> allAddressSTest;
	private MockHttpServletRequest requestMock;
	private WebRequest request;
	private ModelMapper modelMapper;
	
	@BeforeEach
	public void setUpForEachTests() {
		Address address1 = new Address("address1", "City", "12345", new HashMap<Integer, Firestation>(),
				new HashMap<String, Person>());
		Address address2 = new Address("address2", "City", "12345", new HashMap<Integer, Firestation>(),
				new HashMap<String, Person>());
		Address address3 = new Address("address3", "City", "12345", new HashMap<Integer, Firestation>(),
				new HashMap<String, Person>());
		Address address4 = new Address("address4", "City", "12345", new HashMap<Integer, Firestation>(),
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

		allAddressSTest = new HashMap<>();
		allAddressSTest.put(address1.getAddress(), address1);
		allAddressSTest.put(address2.getAddress(), address2);
		allAddressSTest.put(address3.getAddress(), address3);
		allAddressSTest.put(address4.getAddress(), address4);
		addressService.setAllAddressS(allAddressSTest);

		requestMock = new MockHttpServletRequest();
		requestMock.setServerName("http://localhost:8080");
		
		modelMapper = new ModelMapper();
	}

	@AfterEach
	public void unSetForEachTests() {
		allAddressSTest = null;
		addressService.setAllAddressS(allAddressSTest);

		person1 = null;
		person2 = null;
		person3 = null;
		person4 = null;
		person5 = null;
		person6 = null;

		requestMock = null;
		request = null;
		
		modelMapper = null;
	}

	@Nested
	@Tag("GET")
	@DisplayName("GET /childAlert?address=<address>")
	public class FindChildrenByAddressTestClass {
		
		@Test
		@Tag("NominalCase")
		@DisplayName("findChildrenByAddressTest should return list of children (age <= 18) living at this address")
		public void findChildrenByAddressTestShouldReturnListOfChildrenLivingAtThisEddress () {

			// GIVEN
			requestMock.setRequestURI("/childAlert");
			requestMock.setQueryString("?address=address1"); // Request address1
			requestMock.setMethod("GET");
			request = new ServletWebRequest(requestMock);
			when(addressDTOService.addressChildrenToDTO(ArgumentMatchers.<Person>anyList())).then(invocation -> {
				List<Person> addressChildren = invocation.getArgument(0);
				return addressChildren.stream()
						.map(child -> modelMapper.map(child, AddressChildDTO.class))
						.collect(Collectors.toList());
			});
			when(requestService.requestToString(any(WebRequest.class))).thenReturn(
					requestMock.getMethod() + " : " + requestMock.getRequestURI() + requestMock.getQueryString());
			
			// WHEN
			List<AddressAdultChildDTO> addressChildrenDTOResult = null;
			try {
				addressChildrenDTOResult = addressService.findChildrenByAddress("address1", request);
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
			}

			// THEN
			assertThat(addressChildrenDTOResult).containsExactly(modelMapper.map(person1, AddressChildDTO.class));
		}
		
		@Test
		@Tag("CornerCase")
		@DisplayName("findChildrenByAddressTest should throw a ResourceNotFoundException")
		public void findChildrenByAddressTestShouldThrowResourceNotFoundException() {

			// GIVEN
			requestMock.setRequestURI("/childAlert");
			requestMock.setQueryString("?address=address5"); // Request address5 doesn't exist
			requestMock.setMethod("GET");
			request = new ServletWebRequest(requestMock);
			// WHEN
			// THEN
			assertThrows(ResourceNotFoundException.class,
					() -> addressService.findChildrenByAddress("address5", request));
		}
	}
		
	@Nested
	@Tag("GET")
	@DisplayName("GET/fire?address=<address>")
	public class FindPersonsByAddressTestClass {
		
		@Test
		@Tag("NominalCase")
		@DisplayName("findPersonsByAddressTest should return list of inhabitants living at the given address  as well as the number(s) of the fire station serving it")
		public void findChildrenByAddressTestShouldReturnListOfInhabitantsLivingAtAndStationsServingThisEddress () {

			// GIVEN
			requestMock.setRequestURI("/fire?");
			requestMock.setQueryString("?address=address3"); // Request address1
			requestMock.setMethod("GET");
			request = new ServletWebRequest(requestMock);
//			@SuppressWarnings("unchecked")
//			ArgumentCaptor<List<String>> stationNumbers = ArgumentCaptor.forClass(List.class);

			
			doAnswer(invocation -> null).when(addressDTOService).setStationNumbers(anyList());
			when(addressDTOService.addressPersonsToDTO(ArgumentMatchers.<Person>anyList())).then(invocation -> {
				List<Person> addressPersons = invocation.getArgument(0);
				return addressPersons.stream()
						.map(person -> modelMapper.map(person, AddressPersonDTOPerson.class))
						.collect(Collectors.toList());
			});
			when(requestService.requestToString(any(WebRequest.class))).thenReturn(
					requestMock.getMethod() + " : " + requestMock.getRequestURI() + requestMock.getQueryString());
		
//			List<String> stationNumbersExpected = Arrays.asList("1", "2");
			
			// WHEN
			List<AddressPersonDTO> addressPersonsDTOResult = null;
			try {
				addressPersonsDTOResult = addressService.findPersonsByAddress("address3", request);
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
			}

			// THEN
			assertThat(addressPersonsDTOResult).containsExactly(modelMapper.map(person5, AddressPersonDTOPerson.class));
//			assertThat(stationNumbers.capture()).containsExactlyInAnyOrderElementsOf(stationNumbersExpected);
		}
		
		@Test
		@Tag("CornerCase")
		@DisplayName("findPersonsByAddressTest should throw a ResourceNotFoundException")
		public void findPersonsByAddressTestShouldThrowResourceNotFoundException() {

			// GIVEN
			requestMock.setRequestURI("/childAlert");
			requestMock.setQueryString("?address=address5"); // Request address5 doesn't exist
			requestMock.setMethod("GET");
			request = new ServletWebRequest(requestMock);
			// WHEN
			// THEN
			assertThrows(ResourceNotFoundException.class,
					() -> addressService.findPersonsByAddress("address5", request));
		}
		
		@Test
		@Tag("NominalCase")
		@DisplayName("findFirestationssByAddress should return list of fire station serving the address")
		public void findFirestationssByAddressShouldReturnListOfFireStationServingTheAddress () {

			// GIVEN
			List<Firestation> stationNumbersExpected = allAddressSTest.get("address3").getFirestations().values().stream().collect(Collectors.toList()); 
			// WHEN
			List<Firestation> stationNumbersResulted = null;
			try {
				stationNumbersResulted = addressService.findFirestationssByAddress("address3");
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
			}

			// THEN
			assertThat(stationNumbersResulted).containsExactlyInAnyOrderElementsOf(stationNumbersExpected);
		}
		
		@Test
		@Tag("CornerCase")
		@DisplayName("findFirestationssByAddress should throw a ResourceNotFoundException")
		public void findFirestationssByAddressShouldThrowResourceNotFoundException() {

			// GIVEN
			// WHEN
			// THEN
			assertThrows(ResourceNotFoundException.class,
					() -> addressService.findFirestationssByAddress("address5"));
		}

	}
	@Nested
	@Tag("GET")
	@DisplayName("GET /communityEmail?city=<city>")
	public class FindemailPersonsByCityTestClass {
		
		@Test
		@Tag("NominalCase")
		@DisplayName("findemailPersonsByCityTest should return list of distinct email addresses of all the inhabitants of the city")
		public void findemailPersonsByCityTestShouldReturnListOfDistinctEmailAddressesOfAllInhabitants() {

			// GIVEN
			requestMock.setRequestURI("/communityEmail");
			requestMock.setQueryString("?city=city"); // Request City
			requestMock.setMethod("GET");
			request = new ServletWebRequest(requestMock);
			when(requestService.upperCasingFirstLetter(any(String.class))).thenReturn("City");
			when(addressDTOService.addressPersonEmailToDTO(ArgumentMatchers.<Person>anyList())).then(invocation -> {
				List<Person> addressPersonEmail = invocation.getArgument(0);
				return addressPersonEmail.stream()
						.map(person -> modelMapper.map(person, AddressPersonEmailDTO.class)).distinct()
						.collect(Collectors.toList());
			});
			when(requestService.requestToString(any(WebRequest.class))).thenReturn(
					requestMock.getMethod() + " : " + requestMock.getRequestURI() + requestMock.getQueryString());
			
			List<AddressPersonEmailDTO> addressPersonEmailDTOExpected = new ArrayList<>();
			addressPersonEmailDTOExpected.add(modelMapper.map(person1, AddressPersonEmailDTO.class));
			addressPersonEmailDTOExpected.add(modelMapper.map(person3, AddressPersonEmailDTO.class));
			addressPersonEmailDTOExpected.add(modelMapper.map(person4, AddressPersonEmailDTO.class));
			addressPersonEmailDTOExpected.add(modelMapper.map(person5, AddressPersonEmailDTO.class));
			addressPersonEmailDTOExpected.add(modelMapper.map(person6, AddressPersonEmailDTO.class));
			
			// WHEN
			List<AddressPersonEmailDTO> addressPersonEmailDTOResult = null;
			try {
				addressPersonEmailDTOResult = addressService.findemailPersonsByCity("City", request);
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
			}

			// THEN
			assertThat(addressPersonEmailDTOResult).containsExactlyInAnyOrderElementsOf(addressPersonEmailDTOExpected);
		}
		
		@Test
		@Tag("CornerCase")
		@DisplayName("findemailPersonsByCityTest should throw a ResourceNotFoundException")
		public void findemailPersonsByCityTestShouldThrowResourceNotFoundException() {

			// GIVEN
			requestMock.setRequestURI("/communityEmail");
			requestMock.setQueryString("?city=city"); // Request City
			requestMock.setMethod("GET");
			request = new ServletWebRequest(requestMock);
			when(requestService.upperCasingFirstLetter(any(String.class))).thenReturn("Cita");
			// WHEN
			// THEN
			assertThrows(ResourceNotFoundException.class,
					() -> addressService.findemailPersonsByCity("Cita", request));
		}
	}
	

}
