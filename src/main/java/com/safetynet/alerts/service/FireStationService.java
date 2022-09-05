package com.safetynet.alerts.service;

import java.util.List;

import com.safetynet.alerts.model.Person;

public interface FirestationService {
	List<Person> findPersonsByFirestation(int stationNum); 
	List<Person> findAddressPersonsByFiresations(List<Integer> stationNumbers);
}
