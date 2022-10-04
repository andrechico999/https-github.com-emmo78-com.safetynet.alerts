package com.safetynet.alerts.dto;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class FirestationAddressPersonsDTO {
	
	private String address;
	private String city;
	private String zip;
	@EqualsAndHashCode.Exclude
	private List<AddressPersonDTO> houseHold;
}
