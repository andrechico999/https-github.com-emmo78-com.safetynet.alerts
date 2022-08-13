package com.safetynet.alerts.model;

import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Address {

	@EqualsAndHashCode.Include
	private String address;
	
	private String city;
	private String zip;
	private Firestation firestation;

	@Setter(AccessLevel.NONE)
	private Map<String, Person> persons;
	
	public Address() {
		persons = new HashMap<>();
	}
	
	public Person attachPerson(Person person) {
		return persons.putIfAbsent(person.getId(), person);
	}
}
