package com.safetynet.alerts;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.util.RawValue;
import com.safetynet.alerts.model.Address;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Medicalrecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.EntityNames;
import com.safetynet.alerts.repository.GetFromFile;
import com.safetynet.alerts.repository.GetFromFileImpl;
import com.safetynet.alerts.repository.WriteToFile;
import com.safetynet.alerts.repository.WriteToFileImpl;
import com.safetynet.alerts.service.ConvertJsonToClass;
import com.safetynet.alerts.service.ConvertJsonToClassImpl;
import com.safetynet.alerts.service.FindByAddress;
import com.safetynet.alerts.service.FindByFireStation;
import com.safetynet.alerts.service.SelectRemovePersonByField;
import com.safetynet.alerts.service.SetMedicalrecordsForPersons;

@Service
public class CommandLR {

	@Autowired
	FindByAddress fba;

	@Bean
	public CommandLineRunner readFromFile() {
		return args -> {
			try {
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		};
	}
}
