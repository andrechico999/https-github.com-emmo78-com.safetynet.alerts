package com.safetynet.alerts.repository;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

@Repository
public class GetFromFileImpl implements GetFromFile {

	@Override
	public JsonNode returnJsonEntityFromFile(EntityNames entityName) {
		
		JsonNode jsonObjRoot = readJsonRootFromFile();

		JsonNode jsonArrayEntity= JsonNodeFactory.instance.arrayNode();
		((ArrayNode) jsonArrayEntity).addAll((ArrayNode) jsonObjRoot.get(entityName.toString()));  
			
		return jsonArrayEntity;
	}

	@Override
	public JsonNode readJsonRootFromFile() {
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonObjectRoot = mapper.createObjectNode();

		try {
			jsonObjectRoot = mapper.readTree(new File("./resources/input/data.json"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObjectRoot;
	}

}
