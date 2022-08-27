package com.safetynet.alerts.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.safetynet.alerts.model.Address;
import com.safetynet.alerts.model.Fields;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.WriteToFile;

@Service
public class PersonServiceImpl implements PersonService {

	@Autowired
	private JsonNodeService jsNodeService;
	
	@Autowired
	private MedicalrecordPersonService medrecPService;
	
	@Autowired
	private WriteToFile jsonNodeTo;
	
	@Autowired
	private DataProcessingService dataProcService;
	
	@Autowired
	private PersonFieldService personFieldService;
	
	private Map<String, Address> allAddressS;
	private Map<String, Person> persons;

	@Override
	public JsonNode findPersonsByFirstNameAndLastName(String firstName, String lastName) {
		JsonNode arrayNodeIdPersonsAddressOrName = JsonNodeFactory.instance.arrayNode();
		List<Person> idPersonsAddressOrName = new ArrayList<>();
		firstName = dataProcService.upperCasingFirstLetter(firstName);
		lastName = dataProcService.upperCasingFirstLetter(lastName);
		String id =firstName +" "+lastName;
		allAddressS = new HashMap<>();
		jsNodeService.convertFireStations(allAddressS);
		persons = jsNodeService.convertPersons(allAddressS);
		medrecPService.setPersonsMedicalrecords(persons);
		Person idPerson = persons.get(id);
		Map<String, Person> idPersonsAddress = idPerson.getAddress().getPersons();
		String personLastName;
		Optional<String> personLastNameOpt;

		idPersonsAddressOrName.add(idPerson);
		idPersonsAddress.remove(id);
		persons.remove(id);
		personFieldService.selectRemovePersonsByName(lastName, idPersonsAddress).forEach((idLambda, person)-> {
			idPersonsAddressOrName.add(person);
			persons.remove(idLambda);
		});
		
		while (idPersonsAddress.size() > 0) {
			personLastName = null;
			personLastNameOpt = Optional.ofNullable(personLastName);
			Iterator<Map.Entry<String, Person>> idPersonsAddressIterator = idPersonsAddress.entrySet().iterator();
			while (idPersonsAddressIterator.hasNext()) {
				Map.Entry<String, Person> entry = idPersonsAddressIterator.next();
				personLastName = entry.getValue().getLastName();
				if (personLastNameOpt.isEmpty()) {
					personLastNameOpt = Optional.of(personLastName);
					idPersonsAddressOrName.add(entry.getValue());
					idPersonsAddressIterator.remove();
				} else if (personLastName.equals(personLastNameOpt.get())) {
					idPersonsAddressOrName.add(entry.getValue());
					idPersonsAddressIterator.remove();
				}
			}
		}
				
		idPersonsAddressOrName.addAll(personFieldService.selectRemovePersonsByName(lastName, persons).values());
		
		idPersonsAddressOrName.forEach(person -> ((ArrayNode) arrayNodeIdPersonsAddressOrName).add(dataProcService.buildObjectNodePerson(person, new ArrayList<Fields>(Arrays.asList(Fields.lastName, Fields.address, Fields.age, Fields.email, Fields.medicalrecords)))));
				
		jsonNodeTo.writeToFile(arrayNodeIdPersonsAddressOrName);
		allAddressS = null;
		persons = null;
		return arrayNodeIdPersonsAddressOrName;
	}
}
