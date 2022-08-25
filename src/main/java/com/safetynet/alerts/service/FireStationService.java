package com.safetynet.alerts.service;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

public interface FirestationService {
	JsonNode findPersonsByFireStation(int stationNum); 
	JsonNode findPhoneNumbersByFireStation(int stationNum);
	JsonNode findAddressPersonsByFiresations(List<Integer> stationNumbers);
}
