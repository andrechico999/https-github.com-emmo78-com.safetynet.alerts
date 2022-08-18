package com.safetynet.alerts.repository;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class WriteToStirngAndFileImpl implements WriteToStringAndFile {

	@Override
	public boolean writeToFile(List<String> dataOut) {
		ObjectMapper mapper = new ObjectMapper();
		PrettyPrinter pp = new DefaultPrettyPrinter();
		((DefaultPrettyPrinter) pp).indentArraysWith(new DefaultIndenter(" ","\n"));
		((DefaultPrettyPrinter) pp).indentObjectsWith(new DefaultIndenter("",""));
		try {
			mapper.writer(pp).writeValue(new File("./resources/output/dataOut.json"), dataOut);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
