package com.safetynet.alerts.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.dto.FirestationPersonDTO;
import com.safetynet.alerts.dto.FirestationPersonDTOPerson;
import com.safetynet.alerts.dto.FirestationPersonDTOStats;
import com.safetynet.alerts.dto.FirestationPersonPhoneDTO;
import com.safetynet.alerts.dto.FirestationsPersonDTO;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.WriteToFile;
import com.safetynet.alerts.service.FirestationService;

@RestController
public class FirestationController {
	
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private ObjectMapper objectMapper;
    
	@Autowired
	private WriteToFile fileWriter;
	
	@Autowired
	FirestationService firestationService;
	
	@GetMapping("/firestation")
	public List<FirestationPersonDTO> firestationstationNumber(@RequestParam(name = "stationNumber") Optional<String> stationNumber) {
		/*
		 * Cette url doit retourner une liste des personnes (firstName, lastName, address, phone)
		 * couvertes par la caserne de pompiers correspondante
		 * avec un décompte du nombre d'adultes et du nombre d'enfants (âge <= 18 ans)
		 */ 
		
		if (stationNumber.isPresent()){
			FirestationPersonDTOPerson.setNumAdult(0);
			FirestationPersonDTOPerson.setNumChild(0);
			modelMapper.typeMap(Person.class, FirestationPersonDTOPerson.class).<String>addMapping(src -> src.getAddress().getAddress(), (dest, v) -> dest.setAddress(v));
			List<FirestationPersonDTO> firestationPersons = firestationService.findPersonsByFirestation(Integer.parseInt(stationNumber.get())).stream().map(this::convertFirestationPersonToDTO).collect(Collectors.toList());  
			firestationPersons.add(new FirestationPersonDTOStats(FirestationPersonDTOPerson.getNumAdult(),FirestationPersonDTOPerson.getNumChild()));
			fileWriter.writeToFile(objectMapper.valueToTree(firestationPersons));
			return firestationPersons;
		}
		return null;
	}
	
	@GetMapping("/phoneAlert")
	public List<FirestationPersonPhoneDTO> phoneAlertFirestationNumber(@RequestParam(name = "firestation") Optional<String> stationNumber) {
		/*Cette url doit retourner une liste des numéros de téléphone des résidents desservis par la caserne de pompiers
		 */
		if (stationNumber.isPresent()) {
			List<FirestationPersonPhoneDTO> firestationPersonPhones = firestationService.findPersonsByFirestation(Integer.parseInt(stationNumber.get())).stream().map(person -> modelMapper.map(person, FirestationPersonPhoneDTO.class)).distinct().collect(Collectors.toList());
			fileWriter.writeToFile(objectMapper.valueToTree(firestationPersonPhones));
			return firestationPersonPhones;
		}
		return null;
	}
	
	@GetMapping("/flood/stations")
	public List<FirestationsPersonDTO> floodStationNumbers(@RequestParam(name = "stations") Optional<List<String>> stationNumbers) {
		/*
		 * Cette url doit retourner une liste de tous les foyers desservis 
		 * (address, lastName, phone, age, medications : , allergies : )
		 * par les casernes. Cette liste doit regrouper les personnes par adresse.
		 */		
		if (stationNumbers.isPresent()) {
			modelMapper.typeMap(Person.class, FirestationsPersonDTO.class).addMappings(mapper -> {
				mapper.<String>map(src -> src.getAddress().getAddress(), (dest, v) ->dest.setAddress(v));
				//mapper.<String>map(Person::getAge, FirestationsPersonDTO::setAge); //ModelMapper Handling Mismatches
				mapper.<List<String>>map(src -> src.getMedicalrecord().getMedications(), (dest, v) -> dest.setMedications(v));
				mapper.<List<String>>map(src -> src.getMedicalrecord().getAllergies(), (dest, v) -> dest.setAllergies(v));
			});	
			List<FirestationsPersonDTO> firestationsAddressPersons = firestationService.findAddressPersonsByFiresations(stationNumbers.get().stream().map(stationNumber -> Integer.parseInt(stationNumber)).collect(Collectors.toList())).stream().map(person -> modelMapper.map(person, FirestationsPersonDTO.class)).collect(Collectors.toList());
			fileWriter.writeToFile(objectMapper.valueToTree(firestationsAddressPersons));
			return firestationsAddressPersons;
		}
		return null;
	}
	
	private FirestationPersonDTO convertFirestationPersonToDTO(Person person) {
		FirestationPersonDTOPerson firestationPersonDTO = modelMapper.map(person, FirestationPersonDTOPerson.class);
		firestationPersonDTO.sumAdultAndChild(person);
		return firestationPersonDTO;
	}
}
