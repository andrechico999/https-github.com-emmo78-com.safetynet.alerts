package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.JsonNode;

public interface GetFromFile {
	JsonNode returnJsonEntityFromFile(EntityNames entityName);
	JsonNode readJsonRootFromFile();
}
