package com.safetynet.alerts.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Person {

	private String id;
	private String firstName;
	
	@EqualsAndHashCode.Include //for convenience to find same named person
	private String lastName;
	
	private String phone;
	private String email;
	private Medicalrecord medicalrecord;
	private int age;
	
	@Setter(AccessLevel.NONE)
	private Address address;
	
	public Person () {
		address = new Address(null);
	}
	
	public void buildId() {
		id = firstName+" "+lastName;
	}
	
	public void setAddress(Address address) {
		this.address = address;
		this.address.attachPerson(this);
	}
}
