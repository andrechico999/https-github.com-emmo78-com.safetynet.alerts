package com.safetynet.alerts.service;

import java.util.List;

public interface PersonService {
	List<String> findPersonsByFirstNameAndLastName(String firstName, String lastName);
}
