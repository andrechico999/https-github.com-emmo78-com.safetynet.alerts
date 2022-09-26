package com.safetynet.alerts.service;

import java.util.List;

import com.safetynet.alerts.dto.FirestationDTO;
import com.safetynet.alerts.dto.FirestationPersonDTO;
import com.safetynet.alerts.dto.FirestationPersonPhoneDTO;
import com.safetynet.alerts.dto.FirestationsPersonDTO;
import com.safetynet.alerts.model.Person;

/**
 * GET (Read) persons by firestation
 * POST, PUT, DELETE (Add, Update, Delete) Mapping Address to Firestation
 * @author Olivier MOREL
 *
 */
public interface FirestationService {
	List<FirestationPersonDTO> findPersonsByFirestation(String stationNum);
	List<FirestationPersonPhoneDTO> findPersonPhonesByFirestation (String stationNum);
	List<Person> findPersonsByStationNumber (int stationNumber);
	List<FirestationsPersonDTO> findAddressPersonsByFiresations(List<String> stationNumbers);
	FirestationDTO addMappingAddressToFirestation(FirestationDTO firestationDTO);
	FirestationDTO updateMappingAddressToFirestation(FirestationDTO firestationDTO);
	FirestationDTO deleteMappingAddressToFirestation(FirestationDTO firestationDTO);
}
