package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * This Interface gets the JsonNode Object root from file with Entities keys
 * And the return each entity ArrayNode asked
 * 
 * @author Olivier MORELL
 *
 */
public interface GetFromFile {
	/**
	 * Return the ArrayNode (value) mapped with the Entity key in the root ObjectNode
	 * log level DEBUG "read entity {} from file"
	 * @param entityName
	 * @return the Entity ArrayNode 
	 */
	JsonNode returnJsonEntityFromFile(EntityNames entityName);
	/**
	 * Return the root ObjectNode from file with entities key
	 * if Exception occurs log level ERROR "File data.json not found\n\t"+e.toString()
	 * @return a root ObjectNode
	 */
	JsonNode readJsonRootFromFile();
}
