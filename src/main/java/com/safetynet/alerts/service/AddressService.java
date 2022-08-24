package com.safetynet.alerts.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface AddressService {
	JsonNode findChildrenByAddress(String address);
	JsonNode findPersonsByAddress(String address);
	JsonNode findemailPersonsByCity(String city); 
}
