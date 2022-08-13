package com.safetynet.alerts.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface FindByAddress {
	JsonNode findChildrenByAddress(String address);
	JsonNode findPersonsByAdaddress(String address);
	JsonNode findemailPersonsByCity(String city); 
}
