package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.JsonNode;

public interface WriteToFile {
	boolean writeToFile(JsonNode jsonNode);
}
