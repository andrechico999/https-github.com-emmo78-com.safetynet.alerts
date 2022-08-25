package com.safetynet.alerts.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.safetynet.alerts.model.Fields;
import com.safetynet.alerts.model.Person;

@Service
public class DataProcessingServiceImpl implements DataProcessingService {
	private ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public JsonNode buildObjectNodePerson(Person person, List<Fields> fields) {
		JsonNode objectNodePerson = mapper.createObjectNode();
		fields.forEach(field -> {
			switch (field) {
			case firstName:
				((ObjectNode) objectNodePerson).put(field.toString(), person.getFirstName());
				break;
			case lastName:
				((ObjectNode) objectNodePerson).put(field.toString(), person.getLastName());
				break;
			case phone:
				((ObjectNode) objectNodePerson).put(field.toString(), person.getPhone());
				break;
			case email:
				((ObjectNode) objectNodePerson).put(field.toString(), person.getEmail());
				break;
			case age:
				((ObjectNode) objectNodePerson).put(field.toString(), person.getAge());
				break;
			case address:
				((ObjectNode) objectNodePerson).put(field.toString(), person.getAddress().getAddress());
				break;
			case medicalrecords:
				((ObjectNode) objectNodePerson).set("medications", mapper.valueToTree(person.getMedicalrecord().getMedications()));
				((ObjectNode) objectNodePerson).set("allergies", mapper.valueToTree(person.getMedicalrecord().getAllergies()));
			default:
				break;
			}
		});
		return objectNodePerson; 
	}

	@Override
	public String upperCasingFirstLetter(String word) {
		return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
	}



}
