package com.safetynet.alerts.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface PersonService {
	JsonNode findPersonsByFirstNameAndLastName(String firstName, String lastName);
}
