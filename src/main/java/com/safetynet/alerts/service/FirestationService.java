package com.safetynet.alerts.service;

import java.util.List;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.context.request.WebRequest;

import com.safetynet.alerts.dto.FirestationDTO;
import com.safetynet.alerts.dto.FirestationPersonDTO;
import com.safetynet.alerts.dto.FirestationPersonPhoneDTO;
import com.safetynet.alerts.dto.FirestationAddressPersonsDTO;
import com.safetynet.alerts.exception.BadRequestException;
import com.safetynet.alerts.exception.ResourceConflictException;
import com.safetynet.alerts.model.Person;

/**
 * GET (Read) persons by firestation
 * POST, PUT, DELETE (Add, Update, Delete) Mapping Address to Firestation
 * @author Olivier MOREL
 *
 */
public interface FirestationService {
	/**
	 * return list of people covered by the corresponding fire station
	 * with a count of the number of adults and the number of children (age age under or equal 18)
	 * @param stationNum : the station number
	 * @param request : WebRequest parameters
	 * @return the list of persons
	 * @throws BadRequestException : "Correct request is to specify an integer for the station number"
	 * @throws ResourceNotFoundException : caused by findPersonsByStationNumber
	 */
	List<FirestationPersonDTO> findPersonsByFirestation(String stationNum, WebRequest request) throws BadRequestException, ResourceNotFoundException;
	
	/**
	 * return list of telephone numbers of residents served by the fire station
	 * no duplicate
	 * @param stationNum : the station number
	 * @param request : WebRequest parameters
	 * @return the list of phone numbers no duplicate
	 * @throws BadRequestException : "Correct request is to specify an integer for the station number"
	 * @throws ResourceNotFoundException : caused by findPersonsByStationNumber
	 */
	List<FirestationPersonPhoneDTO> findPersonPhonesByFirestation (String stationNum, WebRequest request) throws BadRequestException, ResourceNotFoundException;

	/**
	 * return list return list of persons covered by the corresponding fire station
	 * Single responsibility for find persons and person phones 
	 * @param stationNumber : the station number
	 * @return the list of persons
	 * @throws ResourceNotFoundException : "No fire station found"
	 */
	List<Person> findPersonsByStationNumber (int stationNumber) throws ResourceNotFoundException;
	
	/**
	 * return list of all households served by the barracks. This list must group people by address.
	 * @param stationNumber : the station number
	 * @param request : WebRequest parameters
	 * @return the list address with their household
	 * @throws BadRequestException : "Correct request is to specify a list of integer for the station numbers"
	 * @throws ResourceNotFoundException : "No fire station found"
	 */
	List<FirestationAddressPersonsDTO> findAddressPersonsByFiresations(List<String> stationNumbers, WebRequest request) throws BadRequestException, ResourceNotFoundException;

	/**
	 * add mapping address to fire station
	 * @param firestationDTO : json body POJO
	 * @param request : WebRequest parameters
	 * @return firestationDTO : the mapping
	 * @throws ResourceNotFoundException : "Non-existent address", "No fire station with this number" and "Address has already a firestation"
	 */
	FirestationDTO addMappingAddressToFirestation(FirestationDTO firestationDTO, WebRequest request) throws ResourceNotFoundException, ResourceConflictException;

	/**
	 * update mapping address to fire station
	 * @param firestationDTO : json body POJO
	 * @param request : WebRequest parameters
	 * @return firestationDTO : the mapping
	 * @throws ResourceNotFoundException : "Non-existent address" and "No fire station with this number"
	 */
	FirestationDTO updateMappingAddressToFirestation(FirestationDTO firestationDTO, WebRequest request) throws ResourceNotFoundException;
	
	/**
	 * delete all mapping to a fire station (address = null in given POJO, only station number given)
	 * delete all mapping from a address (address given and station number null (String) in POJO)
	 * delete a mapping from an address to a fire station
	 * 
	 * @param firestationDTO : json body POJO
	 * @param request : WebRequest parameters
	 * @return firestationDTO : the deleted mapping
	 * @throws ResourceNotFoundException : "Non-existent address" and "No fire station with this number"
	 */
	FirestationDTO deleteMappingAddressToFirestation(FirestationDTO firestationDTO, WebRequest request) throws ResourceNotFoundException;
}
