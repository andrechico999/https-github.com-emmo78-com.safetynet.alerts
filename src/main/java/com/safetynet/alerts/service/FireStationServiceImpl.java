package com.safetynet.alerts.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.Address;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;

@Service
public class FirestationServiceImpl implements FirestationService {

	@Autowired
	private JsonNodeService convertJsonToClass;
	
	@Autowired
	private MedicalrecordPersonService medrecPService;
	
	private Map<String, Address> allAddressS;
	private Map<Integer, Firestation> firestations;
	private Map<String, Person> persons;
		
	@Override
	public List<Person> findPersonsByFirestation(int stationNum) {
		
		allAddressS = new HashMap<>();
		firestations = convertJsonToClass.convertFireStations(allAddressS);
		persons = convertJsonToClass.convertPersons(allAddressS);
		medrecPService.setPersonsMedicalrecords(persons);

		return firestations.get(stationNum).getAddressS().values().stream().flatMap(address -> address.getPersons().values().stream()).collect(Collectors.toList());
	}

	@Override
	public List<Person> findAddressPersonsByFiresations(List<Integer> stationNumbers) {

		allAddressS = new HashMap<>();
		firestations = convertJsonToClass.convertFireStations(allAddressS);
		persons = convertJsonToClass.convertPersons(allAddressS);
		medrecPService.setPersonsMedicalrecords(persons);
				
		return stationNumbers.stream().flatMap(stationNumber -> firestations.get(stationNumber).getAddressS().values().stream()).distinct().flatMap(address -> address.getPersons().values().stream()).collect(Collectors.toList());
	}
}
