package com.safetynet.alerts.repository;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

@Repository
public class GetFromFileImpl implements GetFromFile {

	@Autowired
	ObjectMapper objectMapper;	
	
	@Override
	public JsonNode returnJsonEntityFromFile(EntityNames entityName) {
		
		JsonNode jsonObjRoot = readJsonRootFromFile();

		JsonNode jsonArrayEntity= JsonNodeFactory.instance.arrayNode();
		((ArrayNode) jsonArrayEntity).addAll((ArrayNode) jsonObjRoot.get(entityName.toString()));  
			
		return jsonArrayEntity;
	}

	@Override
	public JsonNode readJsonRootFromFile() {
		
		JsonNode jsonObjectRoot = objectMapper.createObjectNode();

		try {
			jsonObjectRoot = objectMapper.readTree(new File("./resources/input/data.json"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObjectRoot;
	}

}
