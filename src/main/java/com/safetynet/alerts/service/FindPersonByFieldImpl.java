package com.safetynet.alerts.service;

import java.util.Map;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.Person;

@Service
public class FindPersonByFieldImpl implements FindPersonByField {

	@Override
	public void selectPersonsByName(String lastName, Map<String, Person> persons) {
		persons.entrySet().removeIf(entry -> !entry.getValue().getLastName().equals(lastName));
	}

}
