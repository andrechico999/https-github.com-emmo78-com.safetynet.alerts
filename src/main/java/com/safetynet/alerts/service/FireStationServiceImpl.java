package com.safetynet.alerts.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
	
	@Autowired
	private WriteToFile jsonNode;
	
	@Autowired
	private DataProcessingService dataProcService;
	
	private Map<String, Address> allAddressS;
	private Map<Integer, Firestation> firestations;
	private Map<String, Person> persons;
		
	@Override
	public List<Person> findPersonsByFireStation(int stationNum) {
		List<Person> firestationPersons = new ArrayList<>();
		allAddressS = new HashMap<>();
		firestations = jsNodeService.convertFireStations(allAddressS);
		persons = jsNodeService.convertPersons(allAddressS);
		medrecPService.setPersonsMedicalrecords(persons);

		firestations.get(stationNum).getAddressS().values().forEach(address ->
			address.getPersons().values().forEach(person -> firestationPersons.add(person)));
		
		allAddressS = null;
		firestations = null;
		persons = null;		
		return firestationPersons;
	}

	@Override
	public List<Person> findPhoneNumbersByFireStation(int stationNum) {
		List<Person> firestationPersonPhones = new ArrayList<>();
		allAddressS = new HashMap<>();
		firestations = jsNodeService.convertFireStations(allAddressS);
		persons = jsNodeService.convertPersons(allAddressS);
		
		firestations.get(stationNum).getAddressS().values().forEach(address -> 
			address.getPersons().values().forEach(person ->
				phonesSet.add(person.getPhone())));
		
		
		

		allAddressS = null;
		firestations = null;
		persons = null;		
		return arrayNodeFsPhones;
	}

	@Override
	public JsonNode findAddressPersonsByFiresations(List<Integer> stationNumbers) {
		JsonNode arrayNodeFSsAddressPersons = JsonNodeFactory.instance.arrayNode();
		Map<String, Address> addressS = new HashMap<>();
		allAddressS = new HashMap<>();
		firestations = jsNodeService.convertFireStations(allAddressS);
		persons = jsNodeService.convertPersons(allAddressS);
		medrecPService.setPersonsMedicalrecords(persons);
				
		stationNumbers.forEach(stationNumber ->
			firestations.get(stationNumber).getAddressS().values().forEach(address ->
				addressS.put(address.getAddress(),address)));
		
		addressS.values().forEach(address -> 
			address.getPersons().values().forEach(person -> ((ArrayNode) arrayNodeFSsAddressPersons).add(dataProcService.buildObjectNodePerson(person, new ArrayList<>(Arrays.asList(Fields.address, Fields.lastName, Fields.phone, Fields.age, Fields.medicalrecords))))));
		
		jsonNode.writeToFile(arrayNodeFSsAddressPersons);
		allAddressS = null;
		firestations = null;
		persons = null;		
		return arrayNodeFSsAddressPersons;
	}
}
