package com.safetynet.alerts.repository;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Repository
public class WriteToFileImpl implements WriteToFile {
	
	private final Logger logger = LoggerFactory.getLogger(WriteToFileImpl.class);
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@Override
	public void writeToFile(JsonNode jsonNode) {
		PrettyPrinter pp = new DefaultPrettyPrinter();
		((DefaultPrettyPrinter) pp).indentArraysWith(new DefaultIndenter("\t", "\012")); //\n = U+0A (UTF-8 Hex) = 012 in octal
		((DefaultPrettyPrinter) pp).indentObjectsWith(new DefaultIndenter("\t", "\012"));
		try {
			objectMapper.writer(pp).writeValue(new File("./resources/output/dataOut.json"), jsonNode);
			logger.debug("wrote json to file");
		} catch (IOException e) { // StreamWriteException and DatabindException are already caught by the alternative IOException
			logger.error("Unable to write Json to file\n\t"+e.toString());
		}
	}
}
