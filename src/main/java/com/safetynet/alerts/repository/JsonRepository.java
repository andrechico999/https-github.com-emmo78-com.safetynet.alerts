package com.safetynet.alerts.repository;

import java.util.List;
import java.util.Map;

import com.safetynet.alerts.dto.FirestationDTO;
import com.safetynet.alerts.dto.MedicalrecordDTO;
import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Medicalrecord;
import com.safetynet.alerts.model.Person;

public interface JsonRepository {
	void jsonNodeServiceImpl(); //@PostConstruct
	List<PersonDTO> getPersonsFromFile();
	Map<String, Person> convertPersonsDTO(List<PersonDTO> personsDTO);
	Person setPersonAddress(Person person);
	List<FirestationDTO> getFirestationsFromFile();
	Map<Integer, Firestation> convertFirestations(List<FirestationDTO> firestationssDTO);
	Firestation updateFirestations(Firestation firestation, Map<Integer, Firestation> localFirestations);
	List<MedicalrecordDTO> getMedicalrecordsFromFile();
	Map<String, Medicalrecord> convertMedicalrecords(List<MedicalrecordDTO> medicalrecorsDTO);
	void setPersonsMedicalrecords(Map<String, Person> persons);
	void setPersonMedicalrecord(Person person, String id);
	public boolean setAge(Person person);
	void setMedicalrecordToPerson(String id);
	
}
