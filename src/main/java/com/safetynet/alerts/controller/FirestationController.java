package com.safetynet.alerts.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.safetynet.alerts.exception.BadRequestException;
import com.safetynet.alerts.service.FirestationService;

@RestController
public class FirestationController {
	
	@Autowired
	private FirestationService firestationService;
	
	@GetMapping("/firestation")
	public ResponseEntity<List<FirestationPersonDTO>> firestationStationNumber(@RequestParam(name = "stationNumber") Optional<String> stationNumber) throws ResourceNotFoundException, BadRequestException {
		/*
		 * Cette url doit retourner une liste des personnes (firstName, lastName, address, phone)
		 * couvertes par la caserne de pompiers correspondante
		 * avec un décompte du nombre d'adultes et du nombre d'enfants (âge <= 18 ans)
		 */ 
		
		if (!stationNumber.isPresent()){
			throw new BadRequestException("Correct request should be http://localhost:8080/firestation?stationNumber=1");
		}
		return new ResponseEntity<>(firestationService.findPersonsByFirestation(stationNumber.get()), HttpStatus.OK);
	}
	
	@GetMapping("/phoneAlert")
	public ResponseEntity<List<FirestationPersonPhoneDTO>> phoneAlertFirestationNumber(@RequestParam(name = "firestation") Optional<String> stationNumber) throws ResourceNotFoundException, BadRequestException {
		/*Cette url doit retourner une liste des numéros de téléphone des résidents desservis par la caserne de pompiers
		 */
		if (!stationNumber.isPresent()) {
			throw new BadRequestException("Correct request should be http://localhost:8080/phoneAlert?firestation=1");
		}
		return new ResponseEntity<>(firestationService.findPersonPhonesByFirestation(stationNumber.get()), HttpStatus.OK);
	}
	
	@GetMapping("/flood/stations")
	public ResponseEntity<List<FirestationsPersonDTO>> floodStationNumbers(@RequestParam(name = "stations") Optional<List<String>> stationNumbers) throws ResourceNotFoundException, BadRequestException {
		/*
		 * Cette url doit retourner une liste de tous les foyers desservis 
		 * (address, lastName, phone, age, medications : , allergies : )
		 * par les casernes. Cette liste doit regrouper les personnes par adresse.
		 */		
		if (!stationNumbers.isPresent()) {
			throw new BadRequestException("Correct request should be http://localhost:8080/flood/stations?stations=1,2,3,4");
		}
		return new ResponseEntity<>(firestationService.findAddressPersonsByFiresations(stationNumbers.get()), HttpStatus.OK);
	}
	
    @PostMapping("/firestation")
    public ResponseEntity<FirestationDTO> createFirestation(@RequestBody Optional<FirestationDTO> firestation) throws ResourceNotFoundException, BadRequestException {
    	if (!firestation.isPresent()) {
    		throw new BadRequestException("Correct request should be a json firestation body");
    	}
    	return new ResponseEntity<>(firestationService.addMappingAddressToFirestation(firestation.get()), HttpStatus.OK);
    }
    
    @PutMapping("/firestation")
    public ResponseEntity<FirestationDTO> updateFirestation(@RequestBody Optional<FirestationDTO> firestation) throws ResourceNotFoundException, BadRequestException {
    	if (!firestation.isPresent()) {
    		throw new BadRequestException("Correct request should be a json firestation body");
    	}
    	return new ResponseEntity<>(firestationService.updateMappingAddressToFirestation(firestation.get()), HttpStatus.OK);
    }
    
    @DeleteMapping("/firestation")
    public ResponseEntity<FirestationDTO> deleteFirestation(@RequestBody Optional<FirestationDTO> firestation) throws ResourceNotFoundException, BadRequestException {
    	if (!firestation.isPresent()) {
    		throw new BadRequestException("Correct request should be a json firestation body");
    	}
    	return new ResponseEntity<>(firestationService.deleteMappingAddressToFirestation(firestation.get()), HttpStatus.OK);
    }
}
