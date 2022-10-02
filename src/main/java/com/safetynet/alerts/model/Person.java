package com.safetynet.alerts.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor //Needed for Unit Tests
public class Person {

	private String id;
	private String firstName;
	private String lastName;
	private String phone;
	private String email;

	@EqualsAndHashCode.Exclude
	private Medicalrecord medicalrecord;
	
	@EqualsAndHashCode.Exclude
	private int age;
	
	@EqualsAndHashCode.Exclude
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
