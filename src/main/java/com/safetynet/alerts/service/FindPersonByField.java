package com.safetynet.alerts.service;

import java.util.Map;

import com.safetynet.alerts.model.Person;

public interface FindPersonByField {
	Map<String, Person> findPersonsByName(String name);
}
