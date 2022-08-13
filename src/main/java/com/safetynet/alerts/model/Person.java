package com.safetynet.alerts.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Person {

	@EqualsAndHashCode.Include
	private String id;

	private String firstName;
	private String lastName;
	private Address address;
	private String phone;
	private String email;
	private Medicalrecord medicalrecord;	
}
