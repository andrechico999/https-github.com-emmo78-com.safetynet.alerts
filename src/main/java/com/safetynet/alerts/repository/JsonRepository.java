package com.safetynet.alerts.repository;

import java.util.List;
import java.util.Map;

import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Medicalrecord;
import com.safetynet.alerts.model.Person;

public interface JsonRepository {
	public void jsonNodeServiceImpl(); //@PostConstruct
	List<PersonDTO> getPersonsFromFile();
	Map<String, Person> convertPersonsDTO(List<PersonDTO> personsDTO);
	Person convertPersonDTO(PersonDTO personDTO);
	Person setAddress(Person person);
	Map<Integer, Firestation> convertFireStations();
	Map<String, Medicalrecord> convertMedicalrecords();
	boolean setPersonsMedicalrecords(Map<String, Person> persons);
	public boolean setAge(Person person);
}
