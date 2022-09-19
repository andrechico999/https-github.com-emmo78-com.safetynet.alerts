package com.safetynet.alerts.dto.service;

import java.util.List;

import com.safetynet.alerts.dto.PersonAddressNameDTO;
import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.model.Person;

public interface PersonDTOService {

	Person convertPersonFromDTO(PersonDTO personDTO);
	PersonDTO convertPersonToDTO (Person person);
	List<PersonAddressNameDTO> personsAddressNameToDTO(List<Person> personsAddressName);
}
