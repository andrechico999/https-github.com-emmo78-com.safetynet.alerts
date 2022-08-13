package com.safetynet.alerts.model;

import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Firestation {
	
	@EqualsAndHashCode.Include
	private int stationNumber;
	
	@Setter(AccessLevel.NONE)
	private Map<String, Address> addressS;
	
	public Firestation() {
		addressS = new HashMap<>();
	}
	
	public Address attachAddress(Address address) {
		return addressS.putIfAbsent(address.getAddress(), address);
	}
	
}
