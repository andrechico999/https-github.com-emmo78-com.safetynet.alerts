package com.safetynet.alerts.model;

import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Address {
	
	@EqualsAndHashCode.Include
	private String address;

	private String city;
	private String zip;
	
	@Setter(AccessLevel.NONE)
	private Map<Integer, Firestation> firestations;

	@Setter(AccessLevel.NONE)
	private Map<String, Person> persons;

	public Address() { //needed by modelMapper for personDTO to Person
		persons = new HashMap<>();
		firestations= new HashMap<>();
	}

	public Address(String address) {
		this.address = address;
		persons = new HashMap<>();
		firestations= new HashMap<>();
	}
	
	public void attachPerson(Person person) {
		persons.put(person.getId(), person);
	}
	
	public void detachPerson(Person person) {
		persons.remove(person.getId());
	}

	public void putFirestation(Firestation firestation) {
		firestation.attachAddress(this);
		firestations.put(firestation.getStationNumber(), firestation);
	}

	public void removeFirestation(Firestation firestation) {
		firestations.remove(firestation.getStationNumber());
	}
	
	
	
}
