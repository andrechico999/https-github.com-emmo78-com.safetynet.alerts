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
 * Handle Map of model objects in Spring IoC
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
	 * @param personsDTO : list of PersonDTO
	 * @return persons Map
	 */
	Map<String, Person> convertPersonsDTO(List<PersonDTO> personsDTO);
	
	/**
	 * use Map of addresses
	 * if person has a new address put it in Map of addresses else set the existent address to person (and attach person to the address) 
	 * @param person : the Person which an Address to set
	 * @return person with address seen (and perhaps set), output needed for stream
	 */
	Person setPersonAddress(Person person);
	
	/**
	 * get the ArrayNode of firestation ObjectNodes and deserialize it
	 * @return List of FirestationDTO
	 */
	List<FirestationDTO> getFirestationsFromFile();
	
	/**
	 * Map List of FirestationDTO to Map of Firestation
	 * @param firestationsDTO : Lsit of FirestationDTO
	 * @return firestations Map
	 */
	Map<Integer, Firestation> convertFirestations(List<FirestationDTO> firestationsDTO);
	
	/**
	 * if firestation not in given Map add it else get existing one
	 * then if the address is new put it in Map of addresses else get existing
	 * and then put the firestation in the address (and attach address to firestation)
	 * there are Maps so if existing put do no change (key Set)  
	 * @param firestation : firestation to update (or perhaps not)
	 * @param localFirestations : working Map
	 * @return firestation perhaps updated, output needed for stream
	 */
	Firestation updateFirestations(Firestation firestation, Map<Integer, Firestation> localFirestations);
	
	/**
	 * get the ArrayNode of medicalrecord ObjectNodes and deserialize it
	 * @return List of medicalrecordDTO
	 */
	List<MedicalrecordDTO> getMedicalrecordsFromFile();
	
	/**
	 * Map List of medicalrecordDTO to Map of Medicalrecord	
	 * @param medicalrecorsDTO : List of MedicalrecordDTO
	 * @return medicalrecords Map
	 */
	Map<String, Medicalrecord> convertMedicalrecords(List<MedicalrecordDTO> medicalrecorsDTO);
	
	/**
	 * Attach to each Person in Map his medical record
	 * @param persons : Map of Person to attach their medical record to  
	 */
	void setPersonsMedicalrecords(Map<String, Person> persons);
	
	/**
	 * Attach to Person his medical record, else a new empty one
	 * @param person : the person
	 * @param id : id of the person and medcial record
	 */
	void setPersonMedicalrecord(Person person, String id);
	
	/**
	 * calculate age from date of birth
	 * @param person : to set Age
	 */
	public void setAge(Person person);
	
	/**
	 * Attach to Medicalrecord his person if exist
	 * @param id : common person and medical record identifier
	 */
	void setMedicalrecordToPerson(String id);
	
}
