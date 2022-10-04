package com.safetynet.alerts.dto;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class AddressPersonDTOPerson implements AddressPersonDTO {
	private String lastName;
	private String phone;
	private String age;
	@EqualsAndHashCode.Exclude
	private List<String> medications;
	@EqualsAndHashCode.Exclude
	private List<String> allergies;
}
