package com.safetynet.alerts.dto.service;

import java.util.List;

import com.safetynet.alerts.dto.AddressAdultChildDTO;
import com.safetynet.alerts.dto.AddressPersonDTO;
import com.safetynet.alerts.dto.AddressPersonEmailDTO;
import com.safetynet.alerts.model.Person;

public interface AddressDTOService {
	List<AddressAdultChildDTO> addressChildrenToDTO(List<Person> addressChildren);
	List<AddressPersonDTO> addressPersonsToDTO(List<Person> addressPersons);
	AddressPersonDTO addressPersonToDTO(Person addressPerson);
	List<AddressPersonEmailDTO> addressPersonEmailToDTO(List<Person> addressPersonEmail);
}
