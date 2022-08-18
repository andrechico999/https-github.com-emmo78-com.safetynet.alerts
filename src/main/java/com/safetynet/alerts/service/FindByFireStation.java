package com.safetynet.alerts.service;

import java.util.List;

public interface FindByFireStation {
	List<String> findPersonsByFireStation(int stationNum); 
	List<String> findPhoneNumbersByFireStation(int stationNum);
	List<String> findAddressPersonsByFiresations(List<Integer> stationNumbers);
}
