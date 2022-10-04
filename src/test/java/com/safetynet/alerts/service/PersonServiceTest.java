package com.safetynet.alerts.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import com.safetynet.alerts.dto.PersonAddressNameDTO;
import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.dto.service.PersonDTOService;
import com.safetynet.alerts.exception.ResourceConflictException;
import com.safetynet.alerts.model.Address;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Medicalrecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.JsonRepository;
import com.safetynet.alerts.repository.WriteToFile;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

	@InjectMocks
	PersonServiceImpl personService;

	@Mock
	private JsonRepository jsonRepository;

	@Mock
	private RequestService requestService;

	@Mock
	private PersonDTOService personDTOService;

	@Mock
	private WriteToFile fileWriter;

	@Mock
	ObjectMapper objetcMapper;

	private Map<String, Person> personsTest;
	private MockHttpServletRequest requestMock;
	private WebRequest request;

	private static ModelMapper modelMapper;

	@BeforeAll
	public static void setUpForAllTests() {
		modelMapper = new ModelMapper();
		modelMapper.typeMap(PersonDTO.class, Person.class).addMappings(mapper -> {
			mapper.<String>map(PersonDTO::getAddress, (dest, v) -> dest.getAddress().setAddress(v));
			mapper.<String>map(PersonDTO::getCity, (dest, v) -> dest.getAddress().setCity(v));
			mapper.<String>map(PersonDTO::getZip, (dest, v) -> dest.getAddress().setZip(v));
		});

		modelMapper.typeMap(Person.class, PersonDTO.class).addMappings(mapper -> {
			mapper.<String>map(src -> src.getAddress().getAddress(), PersonDTO::setAddress);
			mapper.<String>map(src -> src.getAddress().getCity(), PersonDTO::setCity);
			mapper.<String>map(src -> src.getAddress().getZip(), PersonDTO::setZip);
		});
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

		Person person1 = new Person("FirstName1 LastName1", "FirstName1", "LastName1", "123-456-789",
				"person1@email.com", new Medicalrecord(), 11, new Address());
		Person person2 = new Person("FirstName2 LastName1", "FirstName2", "LastName1", "123-456-789",
				"person1@email.com", new Medicalrecord(), 22, new Address());
		Person person3 = new Person("FirstName3 LastName3", "FirstName3", "LastName3", "345-678-901",
				"person3@email.com", new Medicalrecord(), 33, new Address());
		Person person4 = new Person("FirstName4 LastName1", "FirstName4", "LastName1", "456-789-012",
				"person4@email.com", new Medicalrecord(), 44, new Address());

		person1.setAddress(address1); // address1.attach(this) So address1 has person 1,2,3
		person2.setAddress(address1); // address1.attach(this) and person 1,2 same lastName, phone, email
		person3.setAddress(address1); // address1.attach(this)
		person4.setAddress(address2); // address2.attach(this)

		personsTest = new HashMap<>();
		personsTest.put(person1.getId(), person1);
		personsTest.put(person2.getId(), person2);
		personsTest.put(person3.getId(), person3);
		personsTest.put(person4.getId(), person4);
		personService.setPersons(personsTest);

		requestMock = new MockHttpServletRequest();
		requestMock.setServerName("http://localhost:8080");
	}

	@AfterEach
	public void unSetForEachTests() {
		personsTest = null;
		personService.setPersons(personsTest);
		requestMock = null;
		request = null;
	}

	@Nested
	@Tag("GET")
	@DisplayName("GET /personInfo?firstName=<firstName>&lastName=<lastName> tests")
	public class FindPersonsByFirstNameAndLastNameTestClass {
		@Test
		@Tag("NominalCase")
		@DisplayName("findPersonsByFirstNameAndLastNameTest should return list of people with the same name living at the address of the specified person")
		public void findPersonsByFirstNameAndLastNameTestShouldReturnPeopleLivingWithPersonAddressNameDTO() {
			
			// GIVEN
			requestMock.setRequestURI("/personInfo");
			requestMock.setQueryString("?firstName=FirstName1&lastName=LastName1"); //So request person1
			requestMock.setMethod("GET");
			request = new ServletWebRequest(requestMock);
			when(requestService.upperCasingFirstLetter(any(String.class))).thenReturn("FirstName1")
					.thenReturn("LastName1");

			modelMapper.typeMap(Person.class, PersonAddressNameDTO.class).addMappings(mapper -> {
				mapper.map(src -> src.getAddress().getAddress(), PersonAddressNameDTO::setAddress);
				mapper.map(src -> src.getMedicalrecord().getMedications(), PersonAddressNameDTO::setMedications);
				mapper.map(src -> src.getMedicalrecord().getAllergies(), PersonAddressNameDTO::setAllergies);
			});

			when(personDTOService.personsAddressNameToDTO(ArgumentMatchers.<Person>anyList())).then(invocation -> {
				List<Person> personsAddressName = invocation.getArgument(0);
				return personsAddressName.stream().map(person -> modelMapper.map(person, PersonAddressNameDTO.class))
						.collect(Collectors.toList());
			});

			when(requestService.requestToString(any(WebRequest.class))).thenReturn(requestMock.getMethod()+" : "+requestMock.getRequestURI()+requestMock.getQueryString());

			//Select persons 1 and 2 with LastName1 and address1
			List<PersonAddressNameDTO> personsAddressNameDTOExpected = new ArrayList<>();
			personsAddressNameDTOExpected.add(modelMapper.map(personsTest.get("FirstName1 LastName1"), PersonAddressNameDTO.class));
			personsAddressNameDTOExpected.add(modelMapper.map(personsTest.get("FirstName2 LastName1"), PersonAddressNameDTO.class));

			// WEN
			List<PersonAddressNameDTO> personsAddressNameDTOResult=null;
			try {
				personsAddressNameDTOResult = personService.findPersonsByFirstNameAndLastName("FirstName1", "LastName1", request);
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
			}

			// THEN
			// Verifies that the actual group contains exactly the given values and nothing else, in any order.
			assertThat(personsAddressNameDTOResult).containsExactlyInAnyOrderElementsOf(personsAddressNameDTOExpected);
		}

		@Test
		@Tag("CornerCase")
		@DisplayName("findPersonsByFirstNameAndLastNameTest should throw a ResourceNotFoundException")
		public void findPersonsByFirstNameAndLastNameTestshouldThrowResourceNotFoundException() {
			// GIVEN
			request = new ServletWebRequest(requestMock);
			when(requestService.upperCasingFirstLetter(any(String.class))).thenReturn("FirstName5")
					.thenReturn("LastName1");
			// WHEN
			// THEN
			assertThrows(ResourceNotFoundException.class,
					() -> personService.findPersonsByFirstNameAndLastName("FirstName5", "LastName1", request));
		}
	}

	@Nested
	@Tag("POST")
	@DisplayName("POST /person create tests")
	public class CreatePersonTestClass {
		
		@Test
		@Tag("NominalCase")
		@DisplayName("createPersonTest should put the person in persons IoC and return it")
		public void createPersonTestShouldPutPersonInIoCAndReturnIt() {
			//GIVEN
			requestMock.setRequestURI("/person");
			requestMock.setMethod("POST");
			request = new ServletWebRequest(requestMock);
			PersonDTO personDTOExpected = new PersonDTO("FirstName5", "LastName5", "address5", "city", "12345", "567-890-123", "person5@email.com");
			Person personExpected = modelMapper.map(personDTOExpected, Person.class);
			personExpected.buildId();
			when(personDTOService.convertPersonFromDTO(any(PersonDTO.class))).then(invocation -> {
				Person person = modelMapper.map(invocation.getArgument(0, PersonDTO.class), Person.class);
				person.buildId();
				return person;
			});
			when(jsonRepository.setPersonAddress(any(Person.class))).then(invocation -> invocation.getArgument(0, Person.class));
			when(requestService.requestToString(any(WebRequest.class))).thenReturn(requestMock.getMethod()+" : "+requestMock.getRequestURI());
			when(personDTOService.convertPersonToDTO(any(Person.class))).then(invocation -> modelMapper.map(invocation.getArgument(0, Person.class), PersonDTO.class));
			//WHEN
			PersonDTO personDTOResult = null;
			try {
				personDTOResult = personService.createPerson(personDTOExpected, request);
			} catch (ResourceConflictException e) {
				e.printStackTrace();
			}
			//THEN
			assertThat(personDTOResult).isEqualTo(personDTOExpected);
			assertThat(personsTest).containsEntry("FirstName5 LastName5", personExpected);
		}
		
		@Test
		@Tag("CornerCase")
		@DisplayName("createPersonTest should throw a ResourceConflictException")
		public void createPersonTestShouldThrowResourceConflictException() {
			//GIVEN
			request = new ServletWebRequest(requestMock);
			PersonDTO personDTOexpected = new PersonDTO("FirstName1", "LastName1", "address1", "city", "12345", "123-456-789", "person1@email.com");
			when(personDTOService.convertPersonFromDTO(any(PersonDTO.class))).then(invocation -> {
				Person person = modelMapper.map(invocation.getArgument(0, PersonDTO.class), Person.class);
				person.buildId();
				return person;
			});
			//WHEN
			//THEN
			assertThrows(ResourceConflictException.class, () -> personService.createPerson(personDTOexpected, request));
		}
	}
	
	@Nested
	@Tag("PUT")
	@DisplayName("PUT /person update tests")
	public class UpdatePersonTestClass {
		
		@Test
		@Tag("NominalCase")
		@DisplayName("updatePersonTest should update in IoC and and return the person")
		public void updatePersonTestShouldUpdateAndReturnIt() {
			//GIVEN
			requestMock.setRequestURI("/person");
			requestMock.setMethod("PUT");
			request = new ServletWebRequest(requestMock);
			PersonDTO personDTOExpected = new PersonDTO("FirstName1", "LastName1", "address5", "city", "12345", "567-890-123", "person5@email.com");
			Person personExpected = modelMapper.map(personDTOExpected, Person.class);
			personExpected.buildId();
			when(personDTOService.convertPersonFromDTO(any(PersonDTO.class))).then(invocation -> {
				Person person = modelMapper.map(invocation.getArgument(0, PersonDTO.class), Person.class);
				person.buildId();
				return person;
			});
			when(jsonRepository.setPersonAddress(any(Person.class))).then(invocation -> invocation.getArgument(0, Person.class));
			when(requestService.requestToString(any(WebRequest.class))).thenReturn(requestMock.getMethod()+" : "+requestMock.getRequestURI());
			when(personDTOService.convertPersonToDTO(any(Person.class))).then(invocation -> modelMapper.map(invocation.getArgument(0, Person.class), PersonDTO.class));
			//WHEN
			PersonDTO personDTOResult = null;
			try {
				personDTOResult = personService.updatePerson(personDTOExpected, request);
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
			}
			//THEN
			assertThat(personDTOResult).isEqualTo(personDTOExpected);
			assertThat(personsTest).containsEntry("FirstName1 LastName1", personExpected);
		}
		
		@Test
		@Tag("CornerCase")
		@DisplayName("updatePersonTest should throw a ResourceNotFoundException")
		public void updatePersonTestShouldThrowResourceNotFoundException() {
			//GIVEN
			request = new ServletWebRequest(requestMock);
			PersonDTO personDTOexpected = new PersonDTO("FirstName5", "LastName5", "address5", "city", "12345", "567-890-123", "person5@email.com");
			when(personDTOService.convertPersonFromDTO(any(PersonDTO.class))).then(invocation -> {
				Person person = modelMapper.map(invocation.getArgument(0, PersonDTO.class), Person.class);
				person.buildId();
				return person;
			});
			//WHEN
			//THEN
			assertThrows(ResourceNotFoundException.class, () -> personService.updatePerson(personDTOexpected, request));
		}
	}

	@Nested
	@Tag("DELETE")
	@DisplayName("DELETE /person delete tests")
	public class DeletePersonTestClass {
		
		@Test
		@Tag("NominalCase")
		@DisplayName("deletePersonTest should delete in IoC and and return a new personDTO with null fields")
		public void deletePersonTestShouldDeletePersonAndReturnNullFieldsPerson() {
			//GIVEN
			requestMock.setRequestURI("/person");
			requestMock.setMethod("DELETE");
			request = new ServletWebRequest(requestMock);
			PersonDTO personDTOToDelete = new PersonDTO("FirstName1", "LastName1", "address1", "city", "12345", "123-456-789", "person1@email.com");
			Address addressExpected = personsTest.get("FirstName1 LastName1").getAddress();
			when(personDTOService.convertPersonFromDTO(any(PersonDTO.class))).then(invocation -> {
				Person person = modelMapper.map(invocation.getArgument(0, PersonDTO.class), Person.class);
				person.buildId();
				return person;
			});
			when(requestService.requestToString(any(WebRequest.class))).thenReturn(requestMock.getMethod()+" : "+requestMock.getRequestURI());
			when(personDTOService.convertPersonToDTO(any(Person.class))).then(invocation -> modelMapper.map(invocation.getArgument(0, Person.class), PersonDTO.class));
			//WHEN
			PersonDTO personDTOResult = null;
			try {
				personDTOResult = personService.deletePerson(personDTOToDelete, request);
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
			}
			//THEN
			assertThat(personDTOResult).isEqualTo(new PersonDTO());
			assertThat(personsTest).doesNotContainKey("FirstName1 LastName1");
			assertThat(addressExpected.getPersons()).doesNotContainKey("FirstName1 LastName1").containsKey("FirstName2 LastName1").containsKey("FirstName3 LastName3");
		}
		
		@Test
		@Tag("CornerCase")
		@DisplayName("deletePersonTest should throw a ResourceNotFoundException")
		public void deletePersonTestShouldThrowResourceNotFoundException() {
			//GIVEN
			request = new ServletWebRequest(requestMock);
			PersonDTO personDTOexpected = new PersonDTO("FirstName5", "LastName5", "address5", "city", "12345", "567-890-123", "person5@email.com");
			when(personDTOService.convertPersonFromDTO(any(PersonDTO.class))).then(invocation -> {
				Person person = modelMapper.map(invocation.getArgument(0, PersonDTO.class), Person.class);
				person.buildId();
				return person;
			});
			//WHEN
			//THEN
			assertThrows(ResourceNotFoundException.class, () -> personService.deletePerson(personDTOexpected, request));
		}
	}

}
