package com.safetynet.alerts.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
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
	
	public Person (String firstName, String lastName, String phone, String email) {
		this.firstName=firstName;
		this.lastName=lastName;
		this.phone=phone;
		this.email=email;
		id = this.firstName+" "+this.lastName;		
	}

	public void setAddress(Address address) {
		this.address = address;
		this.address.attachPerson(this);
	}
}
