package com.safetynet.alerts.service;

import java.util.List;

public interface FindByAddress {
	List<String> findChildrenByAddress(String address);
	List<String> findPersonsByAddress(String address);
	List<String> findemailPersonsByCity(String city); 
}
