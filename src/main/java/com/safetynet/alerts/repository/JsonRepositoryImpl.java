package com.safetynet.alerts.repository;

import java.time.LocalDate;
import java.time.Period;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.dto.FirestationDTO;
import com.safetynet.alerts.dto.MedicalrecordDTO;
import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.dto.service.FirestationDTOService;
import com.safetynet.alerts.dto.service.MedicalrecordDTOService;
import com.safetynet.alerts.dto.service.PersonDTOService;
import com.safetynet.alerts.model.Address;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Medicalrecord;
import com.safetynet.alerts.model.Person;

import lombok.AccessLevel;
import lombok.Getter;


@Service
@Getter
public class JsonRepositoryImpl implements JsonRepository {

	@Autowired
	@Getter(AccessLevel.NONE)
	ObjectMapper objectMapper;
	
    @Autowired
	@Getter(AccessLevel.NONE)
    private ModelMapper modelMapper;
	
	@Autowired
	@Getter(AccessLevel.NONE)
	PersonDTOService personDTOService;
    
	@Autowired
	@Getter(AccessLevel.NONE)
	FirestationDTOService firestationDTOService;
	
	@Autowired
	@Getter(AccessLevel.NONE)
	MedicalrecordDTOService medicalrecordDTOService;
	
	@Autowired
	@Getter(AccessLevel.NONE)
	GetFromFile getFromFile;
	
	private Map<String, Address> allAddressS;
	private Map<Integer, Firestation> firestations;
	private Map<String, Person> persons;
	private Map<String, Medicalrecord> medicalrecords;
	
	@Override
	@PostConstruct
	public void jsonNodeServiceImpl() {
		allAddressS = new HashMap<>();
		persons = convertPersonsDTO(getPersonsFromFile());
		firestations = convertFirestations(getFirestationsFromFile());
		medicalrecords = convertMedicalrecords(getMedicalrecordsFromFile());
		setPersonsMedicalrecords(persons);
	}
		
	@Override
	public List<PersonDTO> getPersonsFromFile() {
		return objectMapper.convertValue(getFromFile.returnJsonEntityFromFile(EntityNames.persons), new TypeReference<List<PersonDTO>>() {});
	}
	
	@Override
	public Map<String, Person> convertPersonsDTO(List<PersonDTO> personsDTO) {
		return personsDTO.stream().map(personDTOService::convertPersonFromDTO).map(this::setPersonAddress).collect(Collectors.toMap(person -> person.getId(), person -> person));

	}	
	
	@Override
	public Person setPersonAddress(Person person) {
		String addressAddress = person.getAddress().getAddress();
		Optional<Address> addressOpt = Optional.ofNullable(allAddressS.get(addressAddress)); //put pointer yet in Map or null in opt
		Address address = addressOpt.orElseGet(() -> {//get the pointer yet in Map or put a new one in Map
			Address newAddress = new Address(addressAddress); 
			allAddressS.put(addressAddress, newAddress);
			return newAddress;
			});
			address.setCity(person.getAddress().getCity());
			address.setZip(person.getAddress().getZip());
			person.setAddress(address); ////this.address.attachPerson(this);
		return person;
	}
	
	@Override
	public List<FirestationDTO> getFirestationsFromFile() {
		return objectMapper.convertValue(getFromFile.returnJsonEntityFromFile(EntityNames.firestations), new TypeReference<List<FirestationDTO>>() {});
	}
	
	@Override
	public Map<Integer, Firestation> convertFirestations(List<FirestationDTO> firestationsDTO) {
		Map<Integer, Firestation> firestationsTemp = new HashMap<>();
		return firestationsDTO.stream().map(firestationDTOService::convertFirestationFromDTO).map(firestation -> updateFirestations(firestation, firestationsTemp)).distinct().collect(Collectors.toMap(firestation ->firestation.getStationNumber(), firestation -> firestation));
	}

	@Override
	public Firestation updateFirestations(Firestation firestation, Map<Integer, Firestation> localFirestations) {
		int stationNumber = firestation.getStationNumber();
		Optional<Firestation> existingFirestationOpt = Optional.ofNullable(localFirestations.get(stationNumber));//put pointer yet in Map or null in opt
		Firestation existingFirestation = existingFirestationOpt.orElseGet(() -> { //get the pointer yet in Map or put a new one in Map
			localFirestations.put(stationNumber, firestation);
			return firestation;
			});
		Address fsAddress = firestation.getAddressS().values().stream().collect(Collectors.toList()).get(0);
		String fsAddressAddress = fsAddress.getAddress();
		Optional<Address> addressOpt = Optional.ofNullable(allAddressS.get(fsAddressAddress));//put pointer yet in Map or null in opt
		Address address = addressOpt.orElseGet(() -> { //get the pointer yet in Map or put a new one in Map
			allAddressS.put(fsAddressAddress, fsAddress);
			return fsAddress;
			});
		address.putFirestation(existingFirestation); //firestation.attachAddress(this); //Update objects pointed yet in Map don't need to put theme again
		return existingFirestation;
	}
	
	@Override
	public List<MedicalrecordDTO> getMedicalrecordsFromFile() {
		return objectMapper.convertValue(getFromFile.returnJsonEntityFromFile(EntityNames.medicalrecords), new TypeReference<List<MedicalrecordDTO>>() {});
	}
	
	@Override
	public Map<String, Medicalrecord> convertMedicalrecords(List<MedicalrecordDTO> medicalrecorsDTO) {
		return medicalrecorsDTO.stream().map(medicalrecordDTOService::convertMedicalrecordFromDTO).collect(Collectors.toMap(medicalrecord -> medicalrecord.getId(), medicalrecord -> medicalrecord));
	}
	
	@Override
	public void setPersonsMedicalrecords(Map<String, Person> persons) {
		persons.forEach((id,person) -> setPersonMedicalrecord(person, id));
	}
	
	@Override
	public void setPersonMedicalrecord(Person person, String id) {
		Medicalrecord medicalrecord = medicalrecords.get(id);
		if (medicalrecord != null) {
			person.setMedicalrecord(medicalrecord);
			setAge(person);				
		} else {
			person.setMedicalrecord(new Medicalrecord());
			person.setAge(0);
			// TODO no medical record for the person id
		}
	}

	@Override
	public boolean setAge(Person person) {
		person.setAge(Period.between(person.getMedicalrecord().getBirthdate(),LocalDate.now()).getYears());
		return true; // I consider birth date is always before now
	}
	
	@Override
	public void setMedicalrecordToPerson(String id) {
		Person person = persons.get(id);
		if (person != null) {
			setPersonMedicalrecord(person, id);
		} else {
			// TODO No one for the new medical records
		}
	}

}
