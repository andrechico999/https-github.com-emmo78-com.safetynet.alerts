package com.safetynet.alerts.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.Person;

@Service
public class SelectRemovePersonByFieldImpl implements SelectRemovePersonByField {

	@Override
	public Map<String, Person> selectRemovePersonsByName(String lastName, Map<String, Person> persons) {
		Map<String, Person> personsSelected = new HashMap<>();
		Iterator<Map.Entry<String, Person>> personsIterator = persons.entrySet().iterator();
		while (personsIterator.hasNext()) {
			Map.Entry<String, Person> entry = personsIterator.next();
			if (entry.getValue().getLastName().equals(lastName)) {
				personsSelected.put(entry.getKey(), entry.getValue());
				personsIterator.remove();
			}
		}
		return personsSelected;
	}

	@Override
	public Map<String, Person> selectRemovePersonsUnderEqualAge(int age, Map<String, Person> persons) {
		Map<String, Person> personsSelected = new HashMap<>();
		Iterator<Map.Entry<String, Person>> personsIterator = persons.entrySet().iterator();
		while (personsIterator.hasNext()) {
			Map.Entry<String, Person> entry = personsIterator.next();
			if (entry.getValue().getAge() <= age) {
				personsSelected.put(entry.getKey(), entry.getValue());
				personsIterator.remove();
			}
		}
		return personsSelected;
	}
}
