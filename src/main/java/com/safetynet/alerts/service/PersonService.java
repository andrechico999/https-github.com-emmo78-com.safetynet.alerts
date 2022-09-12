package com.safetynet.alerts.service;

import java.util.List;

import com.safetynet.alerts.dto.PersonAddressNameDTO;
import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.model.Person;

public interface PersonService {
	List<PersonAddressNameDTO> findPersonsByFirstNameAndLastName(String firstName, String lastName);
	List<PersonAddressNameDTO> personsAddressNameToDTO(List<Person> personsAddressName);
	PersonDTO createPerson(PersonDTO personDTO);
	PersonDTO updatePerson(PersonDTO personDTO);
	PersonDTO deletePerson(PersonDTO personDTO);
	PersonDTO convertPersonToDTO (Person person);
}
