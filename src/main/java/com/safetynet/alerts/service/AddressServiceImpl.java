package com.safetynet.alerts.service;

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
public class AddressServiceImpl implements AddressService {

	@Autowired
	private JsonNodeService jsNodeService;
	
	@Autowired
	private MedicalrecordPersonService medrecPService;
	
	@Autowired
	private WriteToFile jsonNodeTo;
	
	@Autowired
	private DataProcessingService dataProcService;
	
	private Map<String, Address> allAddressS;
	private Map<String, Person> persons;

	@Override
	public List<Person> findChildrenByAddress(String address) {
		
		allAddressS = new HashMap<>();
		jsNodeService.convertFireStations(allAddressS);
		persons = jsNodeService.convertPersons(allAddressS);
		medrecPService.setPersonsMedicalrecords(persons);
		
		List<Person> addressPersonChildren = allAddressS.get(address).getPersons().values().stream().filter(person -> person.getAge() <= 18).sorted((p1, p2) -> p1.getLastName().compareTo(p2.getLastName())).collect(Collectors.toList());
		addressPersonChildren.addAll(allAddressS.get(address).getPersons().values().stream().filter(person -> addressPersonChildren.contains(person)&&(person.getAge() > 18)).sorted((p1, p2) -> p1.getLastName().compareTo(p2.getLastName())).collect(Collectors.toList()));
		return addressPersonChildren ;
	}

	@Override
	public List<Person> findPersonsByAddress(String address) {
		allAddressS = new HashMap<>();
		jsNodeService.convertFireStations(allAddressS);
		persons = jsNodeService.convertPersons(allAddressS);
		medrecPService.setPersonsMedicalrecords(persons);
		
		return allAddressS.get(address).getPersons().values().stream().collect(Collectors.toList());
	}

	@Override
	public List<Person> findemailPersonsByCity(String city) {
		JsonNode arrayNodeEmails = JsonNodeFactory.instance.arrayNode();
		Set<String> emailsSet = new HashSet<>();
		allAddressS = new HashMap<>();
		jsNodeService.convertFireStations(allAddressS);
		persons = jsNodeService.convertPersons(allAddressS);
		
		// cityAddress.size = 0 if City not found
		List<Address> cityAddressS = allAddressS.values().stream().filter(address -> address.getCity().equals(dataProcService.upperCasingFirstLetter(city))).collect(Collectors.toList());
		
		cityAddressS.forEach(address -> 
			address.getPersons().values().forEach(person ->
				emailsSet.add(person.getEmail())));
		emailsSet.forEach(email -> {
			JsonNode objectNodeEmail = JsonNodeFactory.instance.objectNode();
			((ArrayNode) arrayNodeEmails).add(((ObjectNode) objectNodeEmail).put(Fields.email.toString(), email));
		});
		
		jsonNodeTo.writeToFile(arrayNodeEmails);
		allAddressS = null;
		persons = null;		
		return null;
	}

	@Override
	public List<Firestation> findFirestationssByAddress(String address) {
		return allAddressS.get(address).getFirestations().values().stream().collect(Collectors.toList());
	}
}
