package com.safetynet.alerts.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.safetynet.alerts.model.Address;
import com.safetynet.alerts.model.Fields;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.JsonNodeTo;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	private JsonNodeService jsNodeService;
	
	@Autowired
	private MedicalrecordPersonService medrecPService;
	
	@Autowired
	private JsonNodeTo jsonNodeTo;
	
	@Autowired
	private DataProcessingService dataProcService;
	
	@Autowired
	private PersonFieldService personFieldService;
	
	private Map<String, Address> allAddressS;
	private Map<String, Person> persons;

	@Override
	public JsonNode findChildrenByAddress(String address) {
		JsonNode arrayNodeAddressChildren = JsonNodeFactory.instance.arrayNode();
		Map<String, Person> addressPersons;
		Map<String, Person> addressChildren;
		List<Person> parentsOfChildren = new ArrayList<>();
		String childName;		
		Optional<String> childNameOpt;
		allAddressS = new HashMap<>();
		jsNodeService.convertFireStations(allAddressS);
		persons = jsNodeService.convertPersons(allAddressS);
		medrecPService.setPersonsMedicalrecords(persons);
		
		addressPersons = allAddressS.get(address).getPersons();
		addressChildren = personFieldService.selectRemovePersonsUnderEqualAge(18, addressPersons);
		while (addressChildren.size() > 0) {
			childName = null;
			childNameOpt = Optional.ofNullable(childName);
			Iterator<Map.Entry<String, Person>> adrdressChildrenIterator = addressChildren.entrySet().iterator();
			while (adrdressChildrenIterator.hasNext()) {
				Map.Entry<String, Person> entry = adrdressChildrenIterator.next();
				childName = entry.getValue().getLastName();
				if (childNameOpt.isEmpty()) {
					childNameOpt = Optional.of(childName);
					parentsOfChildren.addAll(personFieldService.selectRemovePersonsByName(childName, addressPersons).values());
					((ArrayNode) arrayNodeAddressChildren).add(dataProcService.buildObjectNodePerson(entry.getValue(), new ArrayList<Fields>(Arrays.asList(Fields.firstName, Fields.lastName, Fields.age))));
					adrdressChildrenIterator.remove();
				} else if(childName.equals(childNameOpt.get())) {
					((ArrayNode) arrayNodeAddressChildren).add(dataProcService.buildObjectNodePerson(entry.getValue(), new ArrayList<Fields>(Arrays.asList(Fields.firstName, Fields.lastName, Fields.age))));
					adrdressChildrenIterator.remove();
				}
			}
		}

		parentsOfChildren.forEach(person -> ((ArrayNode) arrayNodeAddressChildren).add(dataProcService.buildObjectNodePerson(person, new ArrayList<Fields>(Arrays.asList(Fields.firstName, Fields.lastName, Fields.age)))));

		jsonNodeTo.writeToFile(arrayNodeAddressChildren);
		allAddressS = null;
		persons = null;
		return arrayNodeAddressChildren;
	}

	@Override
	public JsonNode findPersonsByAddress(String address) {
		JsonNode arrayNodeAddressPersons = JsonNodeFactory.instance.arrayNode();
		JsonNode objectNodeStationNumber = JsonNodeFactory.instance.objectNode();
		allAddressS = new HashMap<>();
		jsNodeService.convertFireStations(allAddressS);
		persons = jsNodeService.convertPersons(allAddressS);
		medrecPService.setPersonsMedicalrecords(persons);
		
		allAddressS.get(address).getPersons().values().forEach(person -> ((ArrayNode) arrayNodeAddressPersons).add(dataProcService.buildObjectNodePerson(person, new ArrayList<Fields>(Arrays.asList(Fields.lastName, Fields.phone, Fields.age, Fields.medicalrecords)))));
		
		((ObjectNode) objectNodeStationNumber).set("Station number", new ArrayNode(JsonNodeFactory.instance, allAddressS.get(address).getFirestations().values().stream().map(firestation -> TextNode.valueOf(String.valueOf(firestation.getStationNumber()))).collect(Collectors.toList())));
		((ArrayNode) arrayNodeAddressPersons).add(objectNodeStationNumber);
		jsonNodeTo.writeToFile(arrayNodeAddressPersons);
		allAddressS = null;
		persons = null;
		return arrayNodeAddressPersons;
	}

	@Override
	public JsonNode findemailPersonsByCity(String city) {
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
		return arrayNodeEmails;
	}
}
