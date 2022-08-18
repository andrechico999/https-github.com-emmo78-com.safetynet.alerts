package com.safetynet.alerts.service;

import java.util.Map;

import com.safetynet.alerts.model.Person;

public interface SelectRemovePersonByField {
	Map<String, Person> selectRemovePersonsByName(String lastName, Map<String, Person> persons);
	Map<String, Person> selectRemovePersonsUnderEqualAge(int age, Map<String, Person> persons);
}
