package com.safetynet.alerts.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.Address;
import com.safetynet.alerts.model.Person;

@Service
public class PersonServiceImpl implements PersonService {

	@Autowired
	private JsonNodeService jsNodeService;
	
	@Autowired
	private MedicalrecordPersonService medrecPService;
	
	@Autowired
	private DataProcessingService dataProcService;
	
	private Map<String, Address> allAddressS;
	private Map<String, Person> persons;

	@Override
	public List<Person> findPersonsByFirstNameAndLastName(String firstName, String lastName) {

		firstName = dataProcService.upperCasingFirstLetter(firstName);
		lastName = dataProcService.upperCasingFirstLetter(lastName);
		final String id =firstName +" "+lastName; //Local variable lastName defined in an enclosing scope must be final or effectively final
		allAddressS = new HashMap<>();
		jsNodeService.convertFireStations(allAddressS);
		persons = jsNodeService.convertPersons(allAddressS);
		medrecPService.setPersonsMedicalrecords(persons);
		
		return persons.get(id).getAddress().getPersons().values().stream().filter(person -> person.equals(persons.get(id))).collect(Collectors.toList());
	}
}
