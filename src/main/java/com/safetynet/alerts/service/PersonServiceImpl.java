package com.safetynet.alerts.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.JsonRepository;
import com.safetynet.alerts.repository.JsonRepositoryImpl;

@Service
public class PersonServiceImpl implements PersonService {

	@Autowired
	private JsonRepository jsonNodeService;
	
	@Autowired
	private StringService stringService;
	
	private Map<String, Person> persons;

	@PostConstruct
	public void personServiceImpl() {
		persons = ((JsonRepositoryImpl) jsonNodeService).getPersons(); 
	}

	
	@Override
	public List<Person> findPersonsByFirstNameAndLastName(String firstName, String lastName) {

		firstName = stringService.upperCasingFirstLetter(firstName);
		lastName = stringService.upperCasingFirstLetter(lastName);
		final String id =firstName +" "+lastName; //Local variable lastName defined in an enclosing scope must be final or effectively final
		return persons.get(id).getAddress().getPersons().values().stream().filter(person -> person.equals(persons.get(id))).collect(Collectors.toList());
	}
}
