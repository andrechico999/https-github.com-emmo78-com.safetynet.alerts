package com.safetynet.alerts.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.Medicalrecord;
import com.safetynet.alerts.model.Person;

@Service
public class SetMedicalrecordsForPersonsImpl implements SetMedicalrecordsForPersons {
	
	@Autowired
	ConvertJsonToClass convertJsonToClass;

	@Override
	public boolean setPersonsMedicalrecords(Map<String, Person> persons) {
		Map<String, Medicalrecord> medicalrecords = convertJsonToClass.convertMedicalrecords();
		persons.forEach((id,person) ->{
			person.setMedicalrecord(medicalrecords.get(id));
			setAge(person);
		});
		return true; //I consider there is always a medical record for a person
	}

	@Override
	public boolean setAge(Person person) {
		person.setAge(Period.between(person.getMedicalrecord().getBirthdate(),LocalDate.now()).getYears());
		return true; // I consider birthdate is alway befor now
	}
}
