package com.safetynet.alerts.service;

import java.util.Map;

import com.safetynet.alerts.model.Person;

public interface SelectPersonByField {
	Map<String, Person> selectPersonsByName(String lastName, Map<String, Person> persons);
}
