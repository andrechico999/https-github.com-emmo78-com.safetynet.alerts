package com.safetynet.alerts.service;

import java.util.List;

public interface FindByPerson {
	List<String> findPersonsByFirstNameAndLastName(String firstName, String lastName);
}
