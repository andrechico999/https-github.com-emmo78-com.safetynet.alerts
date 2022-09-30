package com.safetynet.alerts.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FirestationAddressPersonsDTO {
	
	private String address;
	private String city;
	private String zip;
	private List<AddressPersonDTO> houseHolds;
}
