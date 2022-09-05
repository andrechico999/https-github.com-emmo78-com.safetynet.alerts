package com.safetynet.alerts.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.safetynet.alerts.model.Address;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Medicalrecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.EntityNames;
import com.safetynet.alerts.repository.GetFromFile;


@Service
public class JsonNodeServiceImpl implements JsonNodeService {

	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	GetFromFile getFromFile;
	
	@PostConstruct
	public void doSomething() {
		
	}
	
	@Override
	public Map<Integer, Firestation> convertFireStations(Map<String, Address> allAddressS) {
		Map<Integer, Firestation> firestations = new HashMap<>();
		JsonNode jsonArrayFirestations = getFromFile.returnJsonEntityFromFile(EntityNames.firestations);
		
		((ArrayNode) jsonArrayFirestations).forEach(jsonObjectFirestation -> {
			int stationNumber = ((ObjectNode) jsonObjectFirestation).get("station").asInt();
			Optional<Firestation> firestationOpt = Optional.ofNullable(firestations.get(stationNumber));//put pointer yet in Map or null in opt
			Firestation firestation = firestationOpt.orElseGet(() -> { //get the pointer yet in Map or put a new one in Map
				Firestation newFirestation = new Firestation(stationNumber);
				firestations.put(stationNumber, newFirestation);
				return newFirestation;
				});
			String stAddress = ((ObjectNode) jsonObjectFirestation).get("address").asText();
			Optional<Address> addressOpt = Optional.ofNullable(allAddressS.get(stAddress));//put pointer yet in Map or null in opt
			Address address = addressOpt.orElseGet(() -> { //get the pointer yet in Map or put a new one in Map
				Address newAddress = new Address(stAddress);
				allAddressS.put(stAddress, newAddress);
				return newAddress;
				});
			address.putFirestation(firestation); //firestation.attachAddress(this); //Update objects pointed yet in Map don't need to put theme again
		});
		return firestations;
	}

	@Override
	public Map<String, Person> convertPersons(Map<String, Address> allAddressS) {
		Map<String, Person> persons = new HashMap<>();
		JsonNode jsonArrayPersons = getFromFile.returnJsonEntityFromFile(EntityNames.persons);
		
		((ArrayNode) jsonArrayPersons).forEach(jsonObjectPerson -> {
			String firstName = ((ObjectNode) jsonObjectPerson).get("firstName").asText();
			String lastName = ((ObjectNode) jsonObjectPerson).get("lastName").asText();
			String phone = ((ObjectNode) jsonObjectPerson).get("phone").asText();
			String email = ((ObjectNode) jsonObjectPerson).get("email").asText();
			Person person = new Person(firstName, lastName, phone, email); //initialize id
			String stAddress = ((ObjectNode) jsonObjectPerson).get("address").asText();
			Optional<Address> addressOpt = Optional.ofNullable(allAddressS.get(stAddress)); //put pointer yet in Map or null in opt
			Address address = addressOpt.orElseGet(() -> { //get the pointer yet in Map or put a new one in Map
				Address newAddress = new Address(stAddress);
				allAddressS.put(stAddress, newAddress);
				return newAddress;
				});
			if (address.getCity() == null) { 
				address.setCity(((ObjectNode) jsonObjectPerson).get("city").asText());
			} //Update objects pointed yet in Map don't need to put theme again
			if (address.getZip() == null) {
				address.setZip(((ObjectNode) jsonObjectPerson).get("zip").asText());
			}
			person.setAddress(address);//this.address.attachPerson(this);
			persons.put(person.getId(), person);
		});
		return persons;
	}

	@Override
	public Map<String, Medicalrecord> convertMedicalrecords() {
		Map<String, Medicalrecord> medicalrecords = new HashMap<>();
		JsonNode jsonArrayMedicalrecords = getFromFile.returnJsonEntityFromFile(EntityNames.medicalrecords);
		
		((ArrayNode) jsonArrayMedicalrecords).forEach(jsonObjectMedicalrecord -> {
			String firstName = ((ObjectNode) jsonObjectMedicalrecord).get("firstName").asText();
			String lastName = ((ObjectNode) jsonObjectMedicalrecord).get("lastName").asText();
			String id = firstName+" "+lastName;
			String stBirthdate = ((ObjectNode) jsonObjectMedicalrecord).get("birthdate").asText();
			Medicalrecord medicalrecord = new Medicalrecord(LocalDate.parse(stBirthdate, DateTimeFormatter.ofPattern("MM/dd/yyyy")));
			medicalrecord.setMedications(objectMapper.convertValue(jsonObjectMedicalrecord.get("medications"), new TypeReference<List<String>>() {}));
			medicalrecord.setAllergies(objectMapper.convertValue(jsonObjectMedicalrecord.get("allergies"), new TypeReference<List<String>>() {}));
			medicalrecords.put(id, medicalrecord);
		});
		return medicalrecords;
	}

}
