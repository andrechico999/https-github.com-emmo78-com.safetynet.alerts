package com.safetynet.alerts.dto.service;

import java.util.List;

import com.safetynet.alerts.dto.FirestationDTO;
import com.safetynet.alerts.dto.FirestationPersonDTO;
import com.safetynet.alerts.dto.FirestationPersonPhoneDTO;
import com.safetynet.alerts.dto.FirestationAddressPersonsDTO;
import com.safetynet.alerts.model.Address;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;

public interface FirestationDTOService {

	Firestation convertFirestationFromDTO(FirestationDTO firestationDTO);
	FirestationDTO convertFirestationToDTO(Firestation firestation, String address);
	List<FirestationPersonDTO> firestationPersonsToDTO(List<Person> firestationPersons);
	List<FirestationPersonPhoneDTO> firestationPersonsToPhonesDTO(List<Person> firestationPersons);
	List<FirestationAddressPersonsDTO> firestationsAddressToDTO (List<Address> firestationsAddress);
}
