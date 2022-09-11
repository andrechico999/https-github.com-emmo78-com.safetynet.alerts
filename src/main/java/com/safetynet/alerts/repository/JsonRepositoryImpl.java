package com.safetynet.alerts.repository;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.model.Address;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Medicalrecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.StringService;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;


@Service
@Getter
@Setter
public class JsonRepositoryImpl implements JsonRepository {

	@Autowired
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	ObjectMapper objectMapper;
	
    @Autowired
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
    private ModelMapper modelMapper;
	
	@Autowired
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	GetFromFile getFromFile;
	
	private Map<String, Address> allAddressS;
	private Map<Integer, Firestation> firestations;
	private Map<String, Person> persons;
	private Map<String, Medicalrecord> medicalrecords;
	
	@Override
	@PostConstruct
	public void jsonNodeServiceImpl() {
		allAddressS = new HashMap<>();
		firestations = new HashMap<>();
		medicalrecords = new HashMap<>();
		
		persons = convertPersonsDTO(getPersonsFromFile());
		firestations = convertFireStations();
		medicalrecords = convertMedicalrecords();
		setPersonsMedicalrecords(persons);
	}
		
	@Override
	public List<PersonDTO> getPersonsFromFile() {
		return objectMapper.convertValue(getFromFile.returnJsonEntityFromFile(EntityNames.persons), new TypeReference<List<PersonDTO>>() {});
	}
	
	@Override
	public Map<String, Person> convertPersonsDTO(List<PersonDTO> personsDTO) {
		return personsDTO.stream().map(this::convertPersonDTO).map(this::setAddress).collect(Collectors.toMap(person -> person.getId(), person -> person));

	}	

	@Override
	public Person convertPersonDTO(PersonDTO personDTO) {
		modelMapper.typeMap(PersonDTO.class, Person.class).addMappings(mapper -> {
			mapper.<String>map(PersonDTO::getAddress, (dest, v) -> dest.getAddress().setAddress(v));
			mapper.<String>map(PersonDTO::getCity, (dest, v) -> dest.getAddress().setCity(v));
			mapper.<String>map(PersonDTO::getZip, (dest, v) -> dest.getAddress().setZip(v));
			/*mapper.skip(Person::setAddress);
			mapper.skip(Person::setId);
			mapper.skip(Person::setMedicalrecord);
			mapper.skip(Person::setAge);
			mapper.<Person>skip((dest,v) -> dest.getAddress().attachPerson(v));
			mapper.<Firestation>skip((dest,v) -> dest.getAddress().putFirestation(v));*/
			});
		Person person = modelMapper.map(personDTO, Person.class);
		person.buildId();
		return person;
	}
	
	@Override
	public Person setAddress(Person person) {
		String stAddress = person.getAddress().getAddress();
		Optional<Address> addressOpt = Optional.ofNullable(allAddressS.get(stAddress)); //put pointer yet in Map or null in opt
		Address address = addressOpt.orElseGet(() -> {//get the pointer yet in Map or put a new one in Map
			Address newAddress = new Address(stAddress); 
			allAddressS.put(stAddress, newAddress);
			return newAddress;
			});
			address.setCity(person.getAddress().getCity());
			address.setZip(person.getAddress().getZip());
			person.setAddress(address); ////this.address.attachPerson(this);
		return person;
	}
	

	@Override
	public Map<Integer, Firestation> convertFireStations() {
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
	public Map<String, Medicalrecord> convertMedicalrecords() {

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
	
	@Override
	public boolean setPersonsMedicalrecords(Map<String, Person> persons) {
		persons.forEach((id,person) ->{
			person.setMedicalrecord(medicalrecords.get(id));
			setAge(person);
		});
		return true; //I consider there is always a medical record for a person
	}

	@Override
	public boolean setAge(Person person) {
		person.setAge(Period.between(person.getMedicalrecord().getBirthdate(),LocalDate.now()).getYears());
		return true; // I consider birth date is always before now
	}

}
