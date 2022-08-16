package com.safetynet.alerts.service;

import java.util.Map;

import com.safetynet.alerts.model.Address;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Medicalrecord;
import com.safetynet.alerts.model.Person;

public interface ConvertJsonToClass {
	Map<Integer, Firestation> convertFireStations(Map<String, Address> allAddressS);
	Map<String, Person> convertPersons(Map<String, Address> allAddressS);
	Map<String, Medicalrecord> convertMedicalrecords();
}
