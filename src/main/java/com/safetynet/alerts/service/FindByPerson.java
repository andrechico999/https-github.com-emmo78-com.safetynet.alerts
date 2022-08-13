package com.safetynet.alerts.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface FindByPerson {
	JsonNode findPersonsByFirstNameAndLastName(String firstName, String lastName);
}
