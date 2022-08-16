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
public class WriteToFileImpl implements WriteToFile {

	@Override
	public boolean writeToFile(JsonNode jsonNode) {
		ObjectMapper mapper = new ObjectMapper();
		PrettyPrinter pp = new DefaultPrettyPrinter();
		((DefaultPrettyPrinter) pp).indentArraysWith(new DefaultIndenter("",""));
		((DefaultPrettyPrinter) pp).indentObjectsWith(new DefaultIndenter("","\n"));
		try {
			mapper.writer(pp).writeValue(new File("./resources/output/dataOut.json"), jsonNode);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
