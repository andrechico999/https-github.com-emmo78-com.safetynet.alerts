package com.safetynet.alerts.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.safetynet.alerts.model.Address;
import com.safetynet.alerts.model.Fields;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.WriteToFile;

@Service
public class FirestationServiceImpl implements FirestationService {

	@Autowired
	private JsonNodeService jsNodeService;
	
	@Autowired
	private MedicalrecordPersonService medrecPService;
	
	private Map<String, Address> allAddressS;
	private Map<Integer, Firestation> firestations;
	private Map<String, Person> persons;
		
	@Override
	public List<Person> findPersonsByFirestation(int stationNum) {
		
		allAddressS = new HashMap<>();
		firestations = jsNodeService.convertFireStations(allAddressS);
		persons = jsNodeService.convertPersons(allAddressS);
		medrecPService.setPersonsMedicalrecords(persons);

		return firestations.get(stationNum).getAddressS().values().stream().flatMap(address -> address.getPersons().values().stream()).collect(Collectors.toList());
	}

	@Override
	public List<Person> findAddressPersonsByFiresations(List<Integer> stationNumbers) {

		allAddressS = new HashMap<>();
		firestations = jsNodeService.convertFireStations(allAddressS);
		persons = jsNodeService.convertPersons(allAddressS);
		medrecPService.setPersonsMedicalrecords(persons);
				
		return stationNumbers.stream().flatMap(stationNumber -> firestations.get(stationNumber).getAddressS().values().stream()).distinct().flatMap(address -> address.getPersons().values().stream()).collect(Collectors.toList());
	}
}
