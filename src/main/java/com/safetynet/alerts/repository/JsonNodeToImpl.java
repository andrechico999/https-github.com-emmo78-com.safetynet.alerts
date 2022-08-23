package com.safetynet.alerts.repository;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class JsonNodeToImpl implements JsonNodeTo {
	private ObjectMapper mapper = new ObjectMapper();
	private PrettyPrinter pp = new DefaultPrettyPrinter();
	
	@Override
	public boolean writeToFile(JsonNode arrayNodePersons) {
		((DefaultPrettyPrinter) pp).indentArraysWith(new DefaultIndenter(" ", "\012")); //U+0A = \n
		((DefaultPrettyPrinter) pp).indentObjectsWith(new DefaultIndenter("", ""));
		try {
			mapper.writer(pp).writeValue(new File("./resources/output/dataOut.json"), arrayNodePersons);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String jsonToString(JsonNode arrayNodePersons) {
		String jsonString = "";
		((DefaultPrettyPrinter) pp).indentArraysWith(new DefaultIndenter("", "")); //U+0A = \n
		((DefaultPrettyPrinter) pp).indentObjectsWith(new DefaultIndenter("", ""));
		try {
			jsonString = mapper.writer(pp).writeValueAsString(arrayNodePersons);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonString;
	}
}
