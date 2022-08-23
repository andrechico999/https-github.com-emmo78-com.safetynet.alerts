package com.safetynet.alerts.service;

public interface AddressService {
	String findChildrenByAddress(String address);
	String findPersonsByAddress(String address);
	String findemailPersonsByCity(String city); 
}
