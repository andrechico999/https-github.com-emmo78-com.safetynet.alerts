package com.safetynet.alerts.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.service.FireStationService;

@RestController
public class FirestationController {
	@Autowired
	FireStationService FsService;
	
	@GetMapping("/firestation")
	public String firestationstationNumber(@RequestParam(name = "stationNumber") Optional<String> stationNumber) {
		/*
		 * Cette url doit retourner une liste des personnes "firstName lastName, address, phone"
		 * couvertes par la caserne de pompiers correspondante
		 * avec un décompte du nombre d'adultes et du nombre d'enfants (âge <= 18 ans)
		 */ 
		
		if (stationNumber.isEmpty()) {
			return "Query parameter is : /firestation?stationNumber=(station_number)";
		}
		return FsService.findPersonsByFireStation(Integer.parseInt(stationNumber.get()));
	}
	
	@GetMapping("/phoneAlert")
	public String phoneAlertFirestationNumber(@RequestParam(name = "firestation") Optional<String> stationNumber) {
		/*Cette url doit retourner une liste des numéros de téléphone des résidents desservis par la caserne de pompiers
		 */
		if (stationNumber.isEmpty()) {
			return "Query parameter is : /phoneAlert?firestation=(firestation_number)";
		}
		return FsService.findPhoneNumbersByFireStation(Integer.parseInt(stationNumber.get()));
	}
	
	@GetMapping("/flood/stations")
	public String floodStationNumbers(@RequestParam(name = "stations") Optional<List<String>> stationNumbers) {
		/*Cette url doit retourner une liste de tous les foyers desservis 
		 * "address, lastName, phone, age ans, lastName, medications : , allergies : "
		 * par les casernes. Cette liste doit regrouper les personnes par adresse.
		 */		
		if (stationNumbers.isEmpty()) {
			return "Query parameter is /flood/stations?stations=(a list of station_numbers)";
		}
		return FsService.findAddressPersonsByFiresations(stationNumbers.get().stream().map(stationNumber -> Integer.parseInt(stationNumber)).collect(Collectors.toList()));
	}
}
