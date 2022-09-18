package com.safetynet.alerts.service;

import java.util.List;

import com.safetynet.alerts.dto.PersonAddressNameDTO;
import com.safetynet.alerts.dto.PersonDTO;

public interface PersonService {
	List<PersonAddressNameDTO> findPersonsByFirstNameAndLastName(String firstName, String lastName);
	PersonDTO createPerson(PersonDTO personDTO);
	PersonDTO updatePerson(PersonDTO personDTO);
	PersonDTO deletePerson(PersonDTO personDTO);
}
