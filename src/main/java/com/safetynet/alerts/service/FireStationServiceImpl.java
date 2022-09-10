package com.safetynet.alerts.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;

@Service
public class FirestationServiceImpl implements FirestationService {

	@Autowired
	private JsonNodeService jsonNodeService;

	private Map<Integer, Firestation> firestations;
	
	@PostConstruct
	public void firestationServiceImpl() {
		firestations = ((JsonNodeServiceImpl) jsonNodeService).getFirestations();
	}
		
	@Override
	public List<Person> findPersonsByFirestation(int stationNum) {
		return firestations.get(stationNum).getAddressS().values().stream().flatMap(address -> address.getPersons().values().stream()).collect(Collectors.toList());
	}

	@Override
	public List<Person> findAddressPersonsByFiresations(List<Integer> stationNumbers) {
		return stationNumbers.stream().flatMap(stationNumber -> firestations.get(stationNumber).getAddressS().values().stream()).distinct().flatMap(address -> address.getPersons().values().stream()).collect(Collectors.toList());
	}
}
