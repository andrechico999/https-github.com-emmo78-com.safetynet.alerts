package com.safetynet.alerts.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressPersonDTOPerson implements AddressPersonDTO {
	private String lastName;
	private String phone;
	private String age;
	private List<String> medications;
	private List<String> allergies;
}
