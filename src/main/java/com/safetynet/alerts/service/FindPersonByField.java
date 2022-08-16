package com.safetynet.alerts.service;

import java.util.Map;

import com.safetynet.alerts.model.Person;

public interface FindPersonByField {
	void selectPersonsByName(String lastName, Map<String, Person> persons);
}
