package com.safetynet.alerts.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import com.safetynet.alerts.repository.JsonNodeTo;

@Service
public class FireStationServiceImpl implements FireStationService {

	@Autowired
	private JsonNodeService jsNodeService;
	
	@Autowired
	private MedicalrecordPersonService medrecPService;
	
	@Autowired
	private JsonNodeTo jsonNodeTo;
	
	@Autowired
	private DataProcessingService dataProcService;
	
	private Map<String, Address> allAddressS;
	private Map<Integer, Firestation> firestations;
	private Map<String, Person> persons;
		
	@Override
	public String findPersonsByFireStation(int stationNum) {
		JsonNode arrayNodeFsPersons = JsonNodeFactory.instance.arrayNode();
		int numAdult = 0; //Local variable length defined in an enclosing scope must be final or effectively final
		int numChildren = 0;
		allAddressS = new HashMap<>();
		firestations = jsNodeService.convertFireStations(allAddressS);
		persons = jsNodeService.convertPersons(allAddressS);
		medrecPService.setPersonsMedicalrecords(persons);
				
		Iterator<Address> itAddress = firestations.get(stationNum).getAddressS().values().iterator();
		while (itAddress.hasNext()) {
			Iterator<Person> itPerson =	itAddress.next().getPersons().values().iterator();
			while (itPerson.hasNext()) {
				Person person = itPerson.next();
				((ArrayNode) arrayNodeFsPersons).add(dataProcService.buildObjectNodePerson(person, new ArrayList<Fields>(Arrays.asList(Fields.firstName, Fields.lastName, Fields.address, Fields.phone)))); 
				if (person.getAge() > 18) {
					 numAdult++;
				} else {
					numChildren++;
				}
			}			
		}
		
		JsonNode objectNodeStats = JsonNodeFactory.instance.objectNode();
		((ObjectNode) objectNodeStats).put("Adult",String.valueOf(numAdult)).put("Children",String.valueOf(numChildren));
		((ArrayNode) arrayNodeFsPersons).add(objectNodeStats);
		
		jsonNodeTo.writeToFile(arrayNodeFsPersons);
		allAddressS = null;
		firestations = null;
		persons = null;
		return jsonNodeTo.jsonToString(arrayNodeFsPersons);
	}

	@Override
	public String findPhoneNumbersByFireStation(int stationNum) {
		JsonNode arrayNodeFsPhones = JsonNodeFactory.instance.arrayNode();
		Set<String> phonesSet = new HashSet<>();
		allAddressS = new HashMap<>();
		firestations = jsNodeService.convertFireStations(allAddressS);
		persons = jsNodeService.convertPersons(allAddressS);
		
		firestations.get(stationNum).getAddressS().values().forEach(address -> 
			address.getPersons().values().forEach(person ->
				phonesSet.add(person.getPhone())));
		
		phonesSet.forEach(phone -> {
			JsonNode objectNodePhone = JsonNodeFactory.instance.objectNode();
			((ArrayNode) arrayNodeFsPhones).add(((ObjectNode) objectNodePhone).put(Fields.phone.toString(), phone));
		});
		
		jsonNodeTo.writeToFile(arrayNodeFsPhones);
		allAddressS = null;
		firestations = null;
		persons = null;		
		return jsonNodeTo.jsonToString(arrayNodeFsPhones);
	}

	@Override
	public String findAddressPersonsByFiresations(List<Integer> stationNumbers) {
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
			address.getPersons().values().forEach(person -> ((ArrayNode) arrayNodeFSsAddressPersons).add(dataProcService.buildObjectNodePerson(person, new ArrayList<>(Arrays.asList(Fields.address, Fields.lastName, Fields.phone, Fields.age, Fields.lastName, Fields.medicalrecords))))));
		
		jsonNodeTo.writeToFile(arrayNodeFSsAddressPersons);
		allAddressS = null;
		firestations = null;
		persons = null;		
		return jsonNodeTo.jsonToString(arrayNodeFSsAddressPersons);
	}
}
