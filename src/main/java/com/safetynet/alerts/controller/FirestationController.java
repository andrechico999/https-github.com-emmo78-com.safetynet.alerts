package com.safetynet.alerts.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.safetynet.alerts.dto.FirestationPersonDTO;
import com.safetynet.alerts.dto.FirestationPersonDTOPerson;
import com.safetynet.alerts.dto.FirestationPersonDTOStats;
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
			FirestationPersonDTOPerson.setNumChildren(0);
			modelMapper.typeMap(Person.class, FirestationPersonDTOPerson.class).<String>addMapping(src -> src.getAddress().getAddress(), (dest, v) -> dest.setAddress(v));
			List<FirestationPersonDTO> firestationPersons = firestationService.findPersonsByFireStation(Integer.parseInt(stationNumber.get())).stream().map(this::convertFirePersonToDTO).collect(Collectors.toList());  
			firestationPersons.add(new FirestationPersonDTOStats(FirestationPersonDTOPerson.getNumAdult(),FirestationPersonDTOPerson.getNumChildren()));
			fileWriter.writeToFile(objectMapper.valueToTree(firestationPersons));
			return firestationPersons;
		}
		return null;
	}
	
	@GetMapping("/phoneAlert")
	public JsonNode phoneAlertFirestationNumber(@RequestParam(name = "firestation") Optional<String> stationNumber) {
		/*Cette url doit retourner une liste des numéros de téléphone des résidents desservis par la caserne de pompiers
		 */
		if (stationNumber.isEmpty()) {
			return TextNode.valueOf("Query parameter is : /phoneAlert?firestation=<firestation_number>");
		}
		return firestationService.findPhoneNumbersByFireStation(Integer.parseInt(stationNumber.get()));
	}
	
	@GetMapping("/flood/stations")
	public JsonNode floodStationNumbers(@RequestParam(name = "stations") Optional<List<String>> stationNumbers) {
		/*
		 * Cette url doit retourner une liste de tous les foyers desservis 
		 * (address, lastName, phone, age, medications : , allergies : )
		 * par les casernes. Cette liste doit regrouper les personnes par adresse.
		 */		
		if (stationNumbers.isEmpty()) {
			return TextNode.valueOf("Query parameter is /flood/stations?stations=(a list of station_numbers)");
		}
		return firestationService.findAddressPersonsByFiresations(stationNumbers.get().stream().map(stationNumber -> Integer.parseInt(stationNumber)).collect(Collectors.toList()));
	}
	
	private FirestationPersonDTO convertFirePersonToDTO(Person person) {
		FirestationPersonDTOPerson firestationPersonDTO = modelMapper.map(person, FirestationPersonDTOPerson.class);
		firestationPersonDTO.sumAdultAndChild(person);
		return firestationPersonDTO;
	}
}
