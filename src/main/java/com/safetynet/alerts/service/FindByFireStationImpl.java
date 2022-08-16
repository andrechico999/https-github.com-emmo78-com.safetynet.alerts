package com.safetynet.alerts.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.safetynet.alerts.model.Address;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.WriteToFile;

@Service
public class FindByFireStationImpl implements FindByFireStation {

	@Autowired
	private ConvertJsonToClass convJsToClass;
	
	@Autowired
	private SetMedicalrecordsForPersons setMedrecForP;
	
	@Autowired
	private WriteToFile writeToFile;
	
	private Map<String, Address> allAddressS;
	private Map<Integer, Firestation> firestations;
	private Map<String, Person> persons;
	ObjectMapper mapper;
	
	@Override
	public List<Map<String,String>> findPersonsByFireStation(int stationNum) {
		List<Map<String,String>> listPersons = new ArrayList<>();
		int numAdult = 0;
		int numChildren = 0;
		allAddressS = new HashMap<>();
		firestations = convJsToClass.convertFireStations(allAddressS);
		persons = convJsToClass.convertPersons(allAddressS);
		setMedrecForP.setPersonsMedicalrecords(persons);
		mapper = new ObjectMapper();
		
		Iterator<Address> itAddress = firestations.get(stationNum).getAddressS().values().iterator();
		while (itAddress.hasNext()) {
			Iterator<Person> itPerson =	itAddress.next().getPersons().values().iterator();
			while (itPerson.hasNext()) {
				Person person = itPerson.next();
				Map<String,String> mapPerson = new HashMap<>();
				mapPerson.put("firstName", person.getFirstName());
				mapPerson.put("lastName", person.getLastName());
				mapPerson.put("address", person.getAddress().getAddress());
				mapPerson.put("phone", person.getPhone());
				listPersons.add(mapPerson);
				int age = Period.between(person.getMedicalrecord().getBirthdate(),LocalDate.now()).getYears();
				if (age > 18) {
					 numAdult++;
				} else {
					numChildren++;
				}
			}			
		}
		Map<String,String> mapStat = new HashMap<>();
		mapStat.put("Adult", String.valueOf(numAdult));
		mapStat.put("Children", String.valueOf(numChildren));
		listPersons.add(mapStat);
		JsonNode listPersonsJs = mapper.valueToTree(listPersons);
		writeToFile.writeToFile(listPersonsJs);
		allAddressS = null;
		firestations = null;
		persons = null;
		mapper = null;		
		return listPersons;
	}

	@Override
	public List<String> findPhoneNumbersByFireStation(int stationNum) {
		List<String> listPhones = new ArrayList<>();
		allAddressS = new HashMap<>();
		firestations = convJsToClass.convertFireStations(allAddressS);
		persons = convJsToClass.convertPersons(allAddressS);
		mapper = new ObjectMapper();
		
		Set<String> phones = new HashSet<>();
		firestations.get(stationNum).getAddressS().values().forEach(address -> {
			address.getPersons().values().forEach(person -> {
				phones.add(person.getPhone());
			});
		});
		listPhones = phones.stream().collect(Collectors.toList());
		JsonNode listPhonesJs = mapper.valueToTree(listPhones);		
		writeToFile.writeToFile(listPhonesJs);
		allAddressS = null;
		firestations = null;
		persons = null;		
		mapper = null;
		return listPhones;
	}

	@Override
	public List<Map<String,String>> findAddressPersonsByFiresations(List<Integer> stationNumbers) {
		List<Map<String,String>> listAddressPersons = new ArrayList<>();
		allAddressS = new HashMap<>();
		firestations = convJsToClass.convertFireStations(allAddressS);
		persons = convJsToClass.convertPersons(allAddressS);
		setMedrecForP.setPersonsMedicalrecords(persons);
		mapper = new ObjectMapper();
				
		Map<String, Address> addressS = new HashMap<>();
		stationNumbers.forEach(stationNumber ->
			firestations.get(stationNumber).getAddressS().values().forEach(address ->
				addressS.put(address.getAddress(),address)));
		
		addressS.values().forEach(address -> {
			address.getPersons().values().forEach(person -> {
				Map<String, String> mapPerson =	new HashMap<>();
				mapPerson.put("address", person.getAddress().getAddress());
				mapPerson.put("lastName", person.getLastName());
				mapPerson.put("phone", person.getPhone());
				mapPerson.put("age", String.valueOf(Period.between(person.getMedicalrecord().getBirthdate(),LocalDate.now()).getYears()));
				mapPerson.put("medications", person.getMedicalrecord().getMedications().toString());
				
				listAddressPersons.add(mapPerson);
			});
		});
		
		writeToFile.writeToFile(listAddressPersons);
		allAddressS = null;
		firestations = null;
		persons = null;		
		return listAddressPersons;
	}

}
