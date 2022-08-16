package com.safetynet.alerts.service;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

public interface FindByFireStation {
	List<Map<String,String>> findPersonsByFireStation(int stationNum); 
	List<String> findPhoneNumbersByFireStation(int stationNum);
	List<Map<String,String>> findAddressPersonsByFiresations(List<Integer> stationNumbers);
}
