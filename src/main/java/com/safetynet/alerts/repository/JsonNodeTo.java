package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.JsonNode;

public interface JsonNodeTo {
	boolean writeToFile(JsonNode arrayNodePersons);
}
