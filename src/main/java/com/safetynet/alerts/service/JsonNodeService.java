package com.safetynet.alerts.service;

import java.util.Map;

import com.safetynet.alerts.model.Address;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Medicalrecord;
import com.safetynet.alerts.model.Person;

public interface JsonNodeService {
	public void jsonNodeServiceImpl(); //@PostConstruct
	Map<Integer, Firestation> convertFireStations(Map<String, Address> allAddressS);
	Map<String, Person> convertPersons(Map<String, Address> allAddressS);
	Map<String, Medicalrecord> convertMedicalrecords();
	boolean setPersonsMedicalrecords(Map<String, Person> persons);
	public boolean setAge(Person person);
}
