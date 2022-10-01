package com.safetynet.alerts.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor //Needed for Unit Tests
public class Person {

	private String id;
	private String firstName;
	
	@EqualsAndHashCode.Include //for convenience to find same named person
	private String lastName;
	
	private String phone;
	private String email;
	private Medicalrecord medicalrecord;
	private int age;
	
	@Setter(AccessLevel.NONE) //This lets override the behaviour of @Setter on a class.
	private Address address;
	
	public Person () { //needed by modelMapper for personDTO to Person
		address = new Address();
		medicalrecord = new Medicalrecord();
	}
	
	public void buildId() {
		id = firstName+" "+lastName;
	}
	
	public void setAddress(Address address) {
		this.address = address;
		this.address.attachPerson(this);
	}
}
