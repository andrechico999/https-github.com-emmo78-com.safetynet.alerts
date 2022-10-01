package com.safetynet.alerts.service;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.lenient;

import org.mockito.ArgumentMatchers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Nested;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.dto.PersonAddressNameDTO;
import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.dto.service.PersonDTOService;
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
	private ModelMapper modelMapper;
	private WebRequest request;
	
	@BeforeEach
	public void setUpForEachTests() {
		Address address1 = new Address("address1", "city", "12345", new HashMap<Integer, Firestation>(), new HashMap<String, Person>());
		Address address2 = new Address("address2", "city", "12345", new HashMap<Integer, Firestation>(), new HashMap<String, Person>());
		
		Person person1 = new Person("FirstName1 LastName1", "FirstName1", "LastName1", "123-456-789", "person1@email.com", new Medicalrecord(), 11, new Address());
		Person person2 = new Person("FirstName2 LastName1", "FirstName2", "LastName1", "123-456-789", "person1@email.com", new Medicalrecord(), 22, new Address());
		Person person3 = new Person("FirstName3 LastName3", "FirstName3", "LastName3", "345-678-901", "person3@email.com", new Medicalrecord(), 33, new Address());
		Person person4 = new Person("FirstName4 LastName1", "FirstName4", "LastName1", "456-789-012", "person4@email.com", new Medicalrecord(), 44, new Address());
		
		person1.setAddress(address1); //address1.attach(this) So address1 has person 1,2,3
		person2.setAddress(address1); //address1.attach(this) and person 1,2 same lastName, phone, email 
		person3.setAddress(address1); //address1.attach(this)
		person4.setAddress(address2); //address2.attach(this)
		
		personsTest = new HashMap<>();
		personsTest.put(person1.getId(), person1);
		personsTest.put(person2.getId(), person2);
		personsTest.put(person3.getId(), person3);
		personsTest.put(person4.getId(), person4);
		personService.setPersons(personsTest);

		modelMapper = new ModelMapper();
		
		requestMock = new MockHttpServletRequest();
		requestMock.setServerName("http://localhost:8080");
		
	}
	
	@AfterEach
	public void unSetForEachTests() {
		personsTest = null;
		personService.setPersons(personsTest);
		requestMock = null;
		request = null;
		modelMapper = null;
	}
	
	@Nested
	@Tag("GET Person Tests")
	@DisplayName("findPersonsByFirstNameAndLastName tests")
	public class FindPersonsByFirstNameAndLastNameTestClass {
		//@SuppressWarnings("unchecked")
		@Test
		@Tag("Nominal Case")
		@DisplayName("findPersonsByFirstNameAndLastNameTest should return list of people with the same name living at the address of the specified person")
		public void findPersonsByFirstNameAndLastNameTestShouldReturnPeopleLivingWithSoPersonAddressNameDTO() {
			
			//GIVEN
			requestMock.setRequestURI("/personInfo");
			requestMock.setParameter("firstName", "FirstName1");
			requestMock.setParameter("lastName", "LastName1"); 
			requestMock.setMethod("GET");
			request = new ServletWebRequest(requestMock);
			when(requestService.upperCasingFirstLetter(any(String.class))).thenReturn("FirstName1").thenReturn("LastName1");

			modelMapper.typeMap(Person.class, PersonAddressNameDTO.class).addMappings(mapper -> {
				mapper.map(src -> src.getAddress().getAddress(), PersonAddressNameDTO::setAddress);
				mapper.map(src -> src.getMedicalrecord().getMedications(), PersonAddressNameDTO::setMedications);
				mapper.map(src -> src.getMedicalrecord().getAllergies(), PersonAddressNameDTO::setAllergies);
			});
			
			when(personDTOService.personsAddressNameToDTO(ArgumentMatchers.<Person>anyList())).then(invocation -> {
				List<Person> personsAddressName = invocation.getArgument(0);
				return personsAddressName.stream().map(person -> modelMapper.map(person, PersonAddressNameDTO.class)).collect(Collectors.toList());
			});
			
			List<PersonAddressNameDTO> personsAddressNameDTOexpected = personsTest.values().stream().filter(person -> (person.getLastName().equals("LastName1")&&person.getAddress().getAddress().equals("address1"))).map(person -> modelMapper.map(person, PersonAddressNameDTO.class)).collect(Collectors.toList());
					
			//WEN
			List<PersonAddressNameDTO> personsAddressNameDTOresult = personService.findPersonsByFirstNameAndLastName("FirstName1", "LastName1", request);
			//THEN
			assertThat(personsAddressNameDTOresult).containsExactlyInAnyOrderElementsOf(personsAddressNameDTOexpected);
		}
	}
}
