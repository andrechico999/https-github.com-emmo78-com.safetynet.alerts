package com.safetynet.alerts.service;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.safetynet.alerts.model.Fields;
import com.safetynet.alerts.model.Person;

public interface DataProcessingService {
	JsonNode buildObjectNodePerson(Person person, List<Fields> fields);
	String upperCasingFirstLetter(String word);
}

