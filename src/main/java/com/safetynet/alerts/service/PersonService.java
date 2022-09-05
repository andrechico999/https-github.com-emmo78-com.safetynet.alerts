package com.safetynet.alerts.service;

import java.util.List;

import com.safetynet.alerts.model.Person;

public interface PersonService {
	List<Person> findPersonsByFirstNameAndLastName(String firstName, String lastName);
}
