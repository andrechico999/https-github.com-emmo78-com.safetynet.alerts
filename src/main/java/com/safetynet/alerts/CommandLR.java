package com.safetynet.alerts;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.EntityNames;

@Service
public class CommandLR {
	
	@Bean
	public CommandLineRunner readFromFile() {
		return args -> {
			try {ObjectMapper mapper = new ObjectMapper();


			
			//Map<String, List<Map<String,?>>> map2 = mapper.readValue(new File("./resources/input/data.json"), Map.class);

			//JsonNode jsonNodeRoot= mapper.valueToTree(map2);
						
			//JsonNode jsonNodeRoot = readJsonRootFromFile();
			
			//JsonNode jsonArrayPersons = returnJsonEntityFromFile(EntityNames.persons);
			//List<Person> persons = mapper.convertValue(jsonArrayPersons, new TypeReference<List<Person>>() {});
			//persons.forEach(p -> System.out.println(p.toString()));
			//JsonNode jsonNodeFireStations = jsonNodeRoot.get("firestations");
			//JsonNode jsonArrayMedicalRecords = returnJsonEntityFromFile(EntityNames.medicalrecords);
			
			//List<String> listFields = Arrays.asList("address");//(PersonFields.firstName.toString(), PersonFields.lastName.toString());
			//List<String> listValues = Arrays.asList("1509 Culver St");//("John", "Boyd");
			//jsonArrayPersons=returnTuplesByFields(jsonArrayPersons, 0, listFields, listValues);
			//jsonArrayPersons=attachMedicalrecordsToPersons(jsonArrayPersons, jsonArrayMedicalRecords);
			
			//PrettyPrinter pp = new DefaultPrettyPrinter();
			//((DefaultPrettyPrinter) pp).indentArraysWith(new DefaultIndenter("",""));
			//((DefaultPrettyPrinter) pp).indentObjectsWith(new DefaultIndenter("","\n"));
			//mapper.writer(pp).writeValue(new File("./resources/input/dataOut.json"), jsonArrayPersons);
				
			//((ObjectNode) essaiNode).set("MedicalsRecors", jsonNodeMedicalRecords);
			//JsonNode essaiNode = jsonNodeRoot.deepCopy();
			//((ObjectNode) essaiNode).set("MedicalsRecors", jsonNodeMedicalRecords);
			//((ObjectNode) essaiNode).setAll((ObjectNode) jsonNodeRoot);
			
			JsonNode txtNode = TextNode.valueOf("essai");
			String txtValue = txtNode.textValue();
			
			
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
			//TypeReference<List<Data>> typeReference = new TypeReference<List<Data>>(){};
			//InputStream inputStream = TypeReference.class.getResourceAsStream("./resources/input/data.json");
		
		};
	}
	
	public JsonNode attachMedicalrecordsToPersons(JsonNode jsonArrayPersons, JsonNode jsonArrayMedicalrecords) {
		JsonNode jsonArrayPersonsMedicalrecords = JsonNodeFactory.instance.arrayNode();
		Iterator<JsonNode> jsonArrayIterator = jsonArrayPersons.elements();
		while(jsonArrayIterator.hasNext()) {
			JsonNode jsonObjPerson = jsonArrayIterator.next();
			List<String> personFields = Arrays.asList("firstName", "lastName");
			List<String> personValues = Arrays.asList(jsonObjPerson.get("firstName").asText(), jsonObjPerson.get("lastName").asText());
			JsonNode jsonObjMedicalrecord = returnTuplesByFields(jsonArrayMedicalrecords.deepCopy(), 0 , personFields, personValues).get(0);
			((ObjectNode) jsonObjPerson).set("medications", jsonObjMedicalrecord.get("medications"));
			((ObjectNode) jsonObjPerson).set("allergies", jsonObjMedicalrecord.get("allergies"));
			((ArrayNode) jsonArrayPersonsMedicalrecords).add(jsonObjPerson);
		}
		return jsonArrayPersonsMedicalrecords;
	}
	
	public JsonNode returnTuplesByFields(JsonNode jsonArrayEntity,int index, List<String> listFields, List<String> listValues) {
		if (index >= listFields.size()) {
			return jsonArrayEntity;
		} else {
			Iterator<JsonNode> jsonArrayIterator = jsonArrayEntity.elements();
			while(jsonArrayIterator.hasNext()) {
				JsonNode jsonObjTuple = jsonArrayIterator.next();
				if(!(jsonObjTuple.get(listFields.get(index)).asText().equals(listValues.get(index)))) {
					jsonArrayIterator.remove();
				}
			}
			index++;
			return returnTuplesByFields(jsonArrayEntity,index, listFields, listValues);
		}
	}
	
	public JsonNode returnJsonEntityFromFile(EntityNames entityName) {
		
		JsonNode jsonObjRoot = readJsonRootFromFile();

		JsonNode jsonArrayEntity= JsonNodeFactory.instance.arrayNode();
		((ArrayNode) jsonArrayEntity).addAll((ArrayNode) jsonObjRoot.get(entityName.toString()));  
			
		return jsonArrayEntity;
	}

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
	
	public boolean writeToFile(JsonNode jsonNode) {
		ObjectMapper mapper = new ObjectMapper();
		PrettyPrinter pp = new DefaultPrettyPrinter();
		((DefaultPrettyPrinter) pp).indentArraysWith(new DefaultIndenter("",""));
		((DefaultPrettyPrinter) pp).indentObjectsWith(new DefaultIndenter("","\n"));
		try {
			mapper.writer(pp).writeValue(new File("./resources/input/dataOut.json"), jsonNode);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
}
