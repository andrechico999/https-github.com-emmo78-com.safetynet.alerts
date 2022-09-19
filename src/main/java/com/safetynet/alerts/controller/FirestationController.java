package com.safetynet.alerts.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.dto.FirestationDTO;
import com.safetynet.alerts.dto.FirestationPersonDTO;
import com.safetynet.alerts.dto.FirestationPersonPhoneDTO;
import com.safetynet.alerts.dto.FirestationsPersonDTO;
import com.safetynet.alerts.service.FirestationService;

@RestController
public class FirestationController {
	
	@Autowired
	private FirestationService firestationService;
	
	@GetMapping("/firestation")
	public List<FirestationPersonDTO> firestationStationNumber(@RequestParam(name = "stationNumber") Optional<String> stationNumber) {
		/*
		 * Cette url doit retourner une liste des personnes (firstName, lastName, address, phone)
		 * couvertes par la caserne de pompiers correspondante
		 * avec un décompte du nombre d'adultes et du nombre d'enfants (âge <= 18 ans)
		 */ 
		
		if (stationNumber.isPresent()){
			return firestationService.findPersonsByFirestation(stationNumber.get());
		}
		return null;
	}
	
	@GetMapping("/phoneAlert")
	public List<FirestationPersonPhoneDTO> phoneAlertFirestationNumber(@RequestParam(name = "firestation") Optional<String> stationNumber) {
		/*Cette url doit retourner une liste des numéros de téléphone des résidents desservis par la caserne de pompiers
		 */
		if (stationNumber.isPresent()) {
			return firestationService.findPersonPhonesByFirestation(stationNumber.get());
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
			return firestationService.findAddressPersonsByFiresations(stationNumbers.get());
		}
		return null;
	}
	
    @PostMapping("/firestation")
    public FirestationDTO createFirestation(@RequestBody Optional<FirestationDTO> firestation) {
    	if (firestation.isPresent()) {
    		return firestationService.addMappingAddressToFirestation(firestation.get());
    	}
	return null;
    }
    
    @PutMapping("/firestation")
    public FirestationDTO updateFirestation(@RequestBody Optional<FirestationDTO> firestation) {
    	if (firestation.isPresent()) {
    		return firestationService.updateMappingAddressToFirestation(firestation.get());
    	}
	return null;
    }
    
    @DeleteMapping("/firestation")
    public FirestationDTO deleteFirestation(@RequestBody Optional<FirestationDTO> firestation) {
    	if (firestation.isPresent()) {
    		return firestationService.deleteMappingAddressToFirestation(firestation.get());
    	}
	return null;
    }

}
