package com.safetynet.alerts.model;

import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Firestation {
	
	private int stationNumber;
	
	@Setter(AccessLevel.NONE)
	private Map<String, Address> addressS;
	
	public Firestation(int stationNumber) {
		this.stationNumber = stationNumber;
		addressS = new HashMap<>();
	}
	
	public void attachAddress(Address address) {
		addressS.put(address.getAddress(), address);
	}
	
}
