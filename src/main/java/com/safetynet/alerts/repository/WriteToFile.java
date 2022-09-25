package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Write to file
 * @author Olivier MOREL
 *
 */
public interface WriteToFile {
	/**
	 * Write a JsonNode to file
	 * log level DEBUG "wrote json to file"
	 * if exception occurs log level ERROR "Unable to write Json to file\n\t"+e.toString()
	 * @param jsonNode 
	 */
	void writeToFile(JsonNode jsonNode);
}
