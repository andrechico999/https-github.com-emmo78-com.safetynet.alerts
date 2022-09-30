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

	public Firestation(int stationNumber) {
		this.stationNumber = stationNumber;
		addressS = new HashMap<>();
	}
	
	public void attachAddress(Address address) {
		addressS.put(address.getAddress(), address);
	}
	
	public void detachAddress(Address address) {
		addressS.remove(address.getAddress());
	}	
}
