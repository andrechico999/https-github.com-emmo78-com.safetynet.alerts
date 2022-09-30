package com.safetynet.alerts.repository;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class GetFromFileImpl implements GetFromFile {

	private final Logger logger = LoggerFactory.getLogger(GetFromFileImpl.class);
	
	@Value("${fileDataJson.path}")
	private String dataFileJson;
	
	@Autowired
	ObjectMapper objectMapper;	
	
	@Override
	public JsonNode returnJsonEntityFromFile(EntityNames entityName) {
		JsonNode jsonObjRoot = readJsonRootFromFile();
		logger.debug("read entity {} from file", entityName.toString());
		return jsonObjRoot.get(entityName.toString()); //return an ArrayNode
	}

	@Override
	public JsonNode readJsonRootFromFile() {
		JsonNode jsonObjectRoot = objectMapper.createObjectNode();
		try {
			jsonObjectRoot = objectMapper.readTree(new File(dataFileJson));
		} catch (IOException e) {
			logger.error("File data.json not found\n\t"+e.toString());
			System.exit(-1);
		}
		return jsonObjectRoot;
	}
}
