package com.safetynet.alerts.repository;

import java.util.List;
import java.util.Map;

import com.safetynet.alerts.dto.FirestationDTO;
import com.safetynet.alerts.dto.MedicalrecordDTO;
import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Medicalrecord;
import com.safetynet.alerts.model.Person;

/**
 * Handle Map of model objects in Spring IOC
 * Need DTO Services to convert DTO to model object.
 * Each Map contains reference values to objects
 * 
 * allAddressS Map : contains the addresses. Key is the String address
 * firestations Map : contains the firestations. Key is the Integer station Number
 * persons Map : contains the persons. Key is the Sting id
 * medicalrecords Map : contains the medicalrecords. Key is the String id
 * 
 * All succes are log level DEBUG
 * All exception are log level ERROR "message"+e.toString() 
 *  
 * @author Olivier MOREL
 *
 */
public interface JsonRepository {
	
	/**
	 * PostConstruct
	 * fill the Map of references to model objects
	 */
	void jsonNodeServiceImpl(); //@PostConstruct
	
	/**
	 * get the ArrayNode of person ObjectNodes and deserialize it
	 * @return List of PersonDTO
	 */
	List<PersonDTO> getPersonsFromFile();
	
	/**
	 * Map List of PersonDTO to Map of Person
	 * @param personsDTO
	 * @return persons Map
	 */
	Map<String, Person> convertPersonsDTO(List<PersonDTO> personsDTO);
	
	/**
	 * use Map of addresses
	 * if person has a new address put it in Map of addresses else set the existent address to person (and attach person to the address) 
	 * @param person
	 * @return person with address seen (and perhaps set) needed for stream
	 */
	Person setPersonAddress(Person person);
	
	/**
	 * get the ArrayNode of firestation ObjectNode and deserialize it
	 * @return List of FirestationDTO
	 */
	List<FirestationDTO> getFirestationsFromFile();
	
	/**
	 * Map List of FirestationDTO to Map of Firestation
	 * @param firestationssDTO
	 * @return firestations Map
	 */
	Map<Integer, Firestation> convertFirestations(List<FirestationDTO> firestationssDTO);
	
	/**
	 * if Firetsation already in firestations Map update it else add it
	 * @param firestation
	 * @param localFirestations 
	 * @return
	 */
	Firestation updateFirestations(Firestation firestation, Map<Integer, Firestation> localFirestations);
	List<MedicalrecordDTO> getMedicalrecordsFromFile();
	Map<String, Medicalrecord> convertMedicalrecords(List<MedicalrecordDTO> medicalrecorsDTO);
	void setPersonsMedicalrecords(Map<String, Person> persons);
	void setPersonMedicalrecord(Person person, String id);
	public boolean setAge(Person person);
	void setMedicalrecordToPerson(String id);
	
}
