package com.safetynet.alerts.dto.service;

import java.util.List;

import com.safetynet.alerts.dto.AddressAdultChildDTO;
import com.safetynet.alerts.dto.AddressPersonDTO;
import com.safetynet.alerts.dto.AddressPersonEmailDTO;
import com.safetynet.alerts.model.Person;

/**
 * To map from and to DTO person get from address
 * @author Olivier MOREL
 *
 */
public interface AddressDTOService {
	List<AddressAdultChildDTO> addressChildrenToDTO(List<Person> addressChildren);
	AddressAdultChildDTO convertAddressChildrenToDTO(Person child);
	List<AddressPersonDTO> addressPersonsToDTO(List<Person> addressPersons);
	AddressPersonDTO addressPersonToDTO(Person addressPerson);
	List<AddressPersonEmailDTO> addressPersonEmailToDTO(List<Person> addressPersonEmail);
}
