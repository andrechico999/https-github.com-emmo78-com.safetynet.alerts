package com.safetynet.alerts.repository;

import java.io.File;
import java.io.IOException;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class JsonNodeToImpl implements JsonNodeTo {
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public boolean writeToFile(JsonNode arrayNodePersons) {
		PrettyPrinter pp = new DefaultPrettyPrinter();
		((DefaultPrettyPrinter) pp).indentArraysWith(new DefaultIndenter("\t", "\012")); //\n = U+0A (UTF-8 Hex) = 012 in octal
		((DefaultPrettyPrinter) pp).indentObjectsWith(new DefaultIndenter("\t", "\012"));
		try {
			mapper.writer(pp).writeValue(new File("./resources/output/dataOut.json"), arrayNodePersons);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
