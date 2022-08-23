package com.safetynet.alerts.service;

import java.util.List;

public interface FireStationService {
	String findPersonsByFireStation(int stationNum); 
	String findPhoneNumbersByFireStation(int stationNum);
	String findAddressPersonsByFiresations(List<Integer> stationNumbers);
}
