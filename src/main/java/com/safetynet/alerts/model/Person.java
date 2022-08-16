package com.safetynet.alerts.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Person {

	private String id;
	private String firstName;
	private String lastName;
	private String phone;
	private String email;
	@ToString.Exclude
	private Medicalrecord medicalrecord;
	
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
