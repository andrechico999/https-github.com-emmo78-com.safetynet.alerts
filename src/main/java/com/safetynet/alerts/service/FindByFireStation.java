package com.safetynet.alerts.service;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

public interface FindByFireStation {
	JsonNode findPersonsByFireStation(int stationNum); 
	JsonNode fiindPhoneNumbersByFireStation(int stationNum);
	JsonNode findAddressPersonsByFiresations(List<Integer> stationNumbers);
}
