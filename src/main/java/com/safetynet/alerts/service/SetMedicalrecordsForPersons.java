package com.safetynet.alerts.service;

import java.util.Map;

import com.safetynet.alerts.model.Person;

public interface SetMedicalrecordsForPersons {
	boolean setPersonsMedicalrecords(Map<String, Person> persons);
	boolean setAge(Person person);
}
