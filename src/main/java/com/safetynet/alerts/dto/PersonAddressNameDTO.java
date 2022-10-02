package com.safetynet.alerts.dto;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode //Needed for Unit Tests
public class PersonAddressNameDTO {
	private String lastName;
	private String address;
	private String age;
	private String email;
	private List<String> medications;
	private List<String> allergies;
}
