package com.safetynet.alerts.service;

import java.util.List;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import com.safetynet.alerts.dto.FirestationDTO;
import com.safetynet.alerts.dto.FirestationPersonDTO;
import com.safetynet.alerts.dto.FirestationPersonPhoneDTO;
import com.safetynet.alerts.dto.FirestationsPersonDTO;
import com.safetynet.alerts.exception.BadRequestException;
import com.safetynet.alerts.model.Person;

/**
 * GET (Read) persons by firestation
 * POST, PUT, DELETE (Add, Update, Delete) Mapping Address to Firestation
 * @author Olivier MOREL
 *
 */
public interface FirestationService {
	List<FirestationPersonDTO> findPersonsByFirestation(String stationNum) throws BadRequestException, ResourceNotFoundException;
	List<FirestationPersonPhoneDTO> findPersonPhonesByFirestation (String stationNum) throws BadRequestException, ResourceNotFoundException;
	List<Person> findPersonsByStationNumber (int stationNumber) throws ResourceNotFoundException;
	List<FirestationsPersonDTO> findAddressPersonsByFiresations(List<String> stationNumbers) throws BadRequestException, ResourceNotFoundException;
	FirestationDTO addMappingAddressToFirestation(FirestationDTO firestationDTO) throws ResourceNotFoundException;
	FirestationDTO updateMappingAddressToFirestation(FirestationDTO firestationDTO) throws ResourceNotFoundException;
	FirestationDTO deleteMappingAddressToFirestation(FirestationDTO firestationDTO) throws ResourceNotFoundException;
}
