package com.safetynet.alerts.service;

import java.util.List;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;

public interface AddressService {
	List<Person> findChildrenByAddress(String address);
	List<Person> findPersonsByAddress(String address);
	List<Person> findemailPersonsByCity(String city);
	List<Firestation> findFirestationssByAddress(String address);
}
