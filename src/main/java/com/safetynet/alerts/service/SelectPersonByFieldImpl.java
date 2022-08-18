package com.safetynet.alerts.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.Person;

@Service
public class SelectPersonByFieldImpl implements SelectPersonByField {

	@Override
	public Map<String, Person> selectPersonsByName(String lastName, Map<String, Person> persons) {
		Map<String, Person> personsCloned = new HashMap<>();
		persons.forEach((id,person)-> personsCloned.put(id, person));
		personsCloned.entrySet().removeIf(entry -> !entry.getValue().getLastName().equals(lastName));
		return personsCloned;
	}
}
