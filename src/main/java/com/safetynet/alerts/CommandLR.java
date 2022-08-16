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
import com.safetynet.alerts.service.FindByFireStation;
import com.safetynet.alerts.service.FindPersonByField;
import com.safetynet.alerts.service.SetMedicalrecordsForPersons;

@Service
public class CommandLR {
	
	@Autowired
	ConvertJsonToClass cj;
	
	@Autowired
	SetMedicalrecordsForPersons smrp;
	
	@Autowired
	FindPersonByField fpbf;
	
	@Autowired
	FindByFireStation fbf;
	
	@Bean
	public CommandLineRunner readFromFile() {
		return args -> {
			try {ObjectMapper mapper = new ObjectMapper();


			
			//Map<String, List<Map<String,?>>> map2 = mapper.readValue(new File("./resources/input/data.json"), Map.class);

			//JsonNode jsonNodeRoot= mapper.valueToTree(map2);
						
			/*
			Map<String, Address> allAddressS = new HashMap<>();
			Map<Integer, Firestation> firestations = cj.convertFireStations(allAddressS);
			Map<String, Person> persons =cj.convertPersons(allAddressS);
			smrp.setPersonsMedicalrecords(persons);
			fpbf.selectPersonsByName("Boyd", persons);
			
			persons.forEach((id,p) ->{
				System.out.print(p.getId()+" : "+p.getMedicalrecord().getBirthdate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
				System.out.println();
			});
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			int age = Period.between(LocalDate.parse("02/21/1972", formatter), LocalDate.parse("02/21/1973", formatter)).getYears();
			System.out.println("age = "+age);
			*/
			
			//JsonNode jsonNodeRoot = readJsonRootFromFile();
			
			//List<Person> persons = mapper.convertValue(jsonArrayPersons, new TypeReference<List<Person>>() {});
			//persons.forEach(p -> System.out.println(p.toString()));
			
			
			//List<String> listFields = Arrays.asList("address");//(PersonFields.firstName.toString(), PersonFields.lastName.toString());
			//List<String> listValues = Arrays.asList("1509 Culver St");//("John", "Boyd");
			//jsonArrayPersons=returnTuplesByFields(jsonArrayPersons, 0, listFields, listValues);
			//jsonArrayPersons=attachMedicalrecordsToPersons(jsonArrayPersons, jsonArrayMedicalRecords);
			
			//PrettyPrinter pp = new DefaultPrettyPrinter();
			//((DefaultPrettyPrinter) pp).indentArraysWith(new DefaultIndenter("",""));
			//((DefaultPrettyPrinter) pp).indentObjectsWith(new DefaultIndenter("","\n"));
			//mapper.writer(pp).writeValue(new File("./resources/input/dataOut.json"), fbf.findPersonsByFireStation(1));
				
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
	


	

}
