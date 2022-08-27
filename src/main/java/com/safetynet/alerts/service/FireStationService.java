package com.safetynet.alerts.service;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.safetynet.alerts.model.Person;

public interface FirestationService {
	List<Person> findPersonsByFireStation(int stationNum); 
	List<Person> findPhoneNumbersByFireStation(int stationNum);
	JsonNode findAddressPersonsByFiresations(List<Integer> stationNumbers);
}
