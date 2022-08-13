package com.safetynet.alerts.service;

import java.util.Map;

import com.safetynet.alerts.model.Person;

public interface SetMedicalrecordsForPersons {
	boolean SetPersonsMedicalrecords(Map<String, Person> persons);
}
