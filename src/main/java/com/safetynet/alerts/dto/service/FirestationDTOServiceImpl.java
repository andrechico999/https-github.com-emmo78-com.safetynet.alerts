package com.safetynet.alerts.dto.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.dto.FirestationDTO;
import com.safetynet.alerts.dto.FirestationPersonDTO;
import com.safetynet.alerts.dto.FirestationPersonDTOPerson;
import com.safetynet.alerts.dto.FirestationPersonDTOStats;
import com.safetynet.alerts.dto.FirestationPersonPhoneDTO;
import com.safetynet.alerts.dto.FirestationsPersonDTO;
import com.safetynet.alerts.model.Address;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.WriteToFile;

@Service
public class FirestationDTOServiceImpl implements FirestationDTOService {
    @Autowired
	private ModelMapper modelMapper;
    
    @Autowired
    private ObjectMapper objectMapper;
    
	@Autowired
	private WriteToFile fileWriter;
	
	@Override
	public Firestation convertFirestationFromDTO(FirestationDTO firestationDTO) {
		modelMapper.typeMap(FirestationDTO.class, Firestation.class).addMapping(FirestationDTO::getStation, Firestation::setStationNumber); //ModelMapper Handling Mismatches
		Firestation firestation = modelMapper.map(firestationDTO, Firestation.class);
		firestation.attachAddress(new Address(firestationDTO.getAddress())); 
		return firestation;
	}

	@Override
	public FirestationDTO convertFirestationToDTO(Firestation firestation, String addressAddress) {
		modelMapper.typeMap(Firestation.class, FirestationDTO.class).addMappings(mapper -> {
			mapper.map(Firestation::getStationNumber, FirestationDTO::setStation);
			});
		FirestationDTO firestationDTO = modelMapper.map(firestation, FirestationDTO.class);
		firestationDTO.setAddress(firestation.getAddressS().get(addressAddress).getAddress());
		return firestationDTO;
	}

	@Override
	public List<FirestationPersonDTO> firestationPersonsToDTO(List<Person> firestationPersons) {
		FirestationPersonDTOPerson.setNumAdult(0);
		FirestationPersonDTOPerson.setNumChild(0);
		modelMapper.typeMap(Person.class, FirestationPersonDTOPerson.class).addMapping(src -> src.getAddress().getAddress(), FirestationPersonDTOPerson::setAddress);
		List<FirestationPersonDTO> firestationPersonsDTO = firestationPersons.stream().map(this::convertFirestationPersonToDTO).collect(Collectors.toList());  
		firestationPersonsDTO.add(new FirestationPersonDTOStats(FirestationPersonDTOPerson.getNumAdult(),FirestationPersonDTOPerson.getNumChild()));
		fileWriter.writeToFile(objectMapper.valueToTree(firestationPersonsDTO));
		return firestationPersonsDTO;
	}
	
	private FirestationPersonDTO convertFirestationPersonToDTO(Person person) {
		FirestationPersonDTOPerson firestationPersonDTO = modelMapper.map(person, FirestationPersonDTOPerson.class);
		firestationPersonDTO.sumAdultAndChild(person);
		return firestationPersonDTO;
	}
	
	@Override
	public List<FirestationPersonPhoneDTO> firestationPersonsToPhonesDTO(List<Person> firestationPersons) {
		List<FirestationPersonPhoneDTO> firestationPersonPhonesDTO = firestationPersons.stream().map(person -> modelMapper.map(person, FirestationPersonPhoneDTO.class)).distinct().collect(Collectors.toList());
		fileWriter.writeToFile(objectMapper.valueToTree(firestationPersonPhonesDTO));
		return firestationPersonPhonesDTO;
	}

	@Override
	public List<FirestationsPersonDTO> firestationsAddressPersonsToDTO(List<Person> firestationsPersons) {
		modelMapper.typeMap(Person.class, FirestationsPersonDTO.class).addMappings(mapper -> {
			mapper.map(src -> src.getAddress().getAddress(), FirestationsPersonDTO::setAddress);
			//mapper.<String>map(Person::getAge, FirestationsPersonDTO::setAge); //ModelMapper Handling Mismatches
			mapper.map(src -> src.getMedicalrecord().getMedications(), FirestationsPersonDTO::setMedications);
			mapper.map(src -> src.getMedicalrecord().getAllergies(), FirestationsPersonDTO::setAllergies);
		});	
		List<FirestationsPersonDTO> firestationsAddressPersonsDTO = firestationsPersons.stream().map(person -> modelMapper.map(person, FirestationsPersonDTO.class)).collect(Collectors.toList());
		fileWriter.writeToFile(objectMapper.valueToTree(firestationsAddressPersonsDTO));
		return firestationsAddressPersonsDTO;
	}
}
