package com.safetynet.alerts.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.safetynet.alerts.service.FindByFireStation;

@RestController
public class ByFireStationController {
	
	@Autowired
	FindByFireStation findByFS;
	
	@GetMapping("/firestation")
	public List<Map<String,String>> firestationstationNumber(@RequestParam(name = "stationNumber") Optional<String> stationNumber) {
		/*
		 * Cette url doit retourner une liste des personnes couvertes par la caserne de pompiers correspondante avec un
		 * décompte du nombre d'adultes et du nombre d'enfants (âge <= 18 ans)
		 */ 
		
		if (stationNumber.isEmpty()) {
			List<Map<String,String>> listPersons = new ArrayList<>();
			Map<String,String> mapPerson = new HashMap<>();
			mapPerson.put("Query parameter is "," /firestation?stationNumber=<station_number>");
			listPersons.add(mapPerson);
			return listPersons;
		}
		return findByFS.findPersonsByFireStation(Integer.parseInt(stationNumber.get()));
	}
	
	@GetMapping("/childAlert")
	public JsonNode childAlertAddress(@RequestParam(name = "address") Optional<String> address) {
		/*Cette url doit retourner une liste d'enfants (âge <= 18 ans) habitant à cette adresse. S'il n'y a pas d'enfant,
		 * cette url peut renvoyer une chaîne vide
		 */
		if (address.isEmpty()) {
			return TextNode.valueOf("Query parameter is : /childAlert?address=<address>");
		}
		return TextNode.valueOf("address = "+address.get());
	}
	
	@GetMapping("/phoneAlert")
	public List<String> phoneAlertFirestationNumber(@RequestParam(name = "firestation") Optional<String> stationNumber) {
		/*Cette url doit retourner une liste des numéros de téléphone des résidents desservis par la caserne de pompiers
		 */
		if (stationNumber.isEmpty()) {
			List<String> list = new ArrayList<>();
			list.add("Query parameter is : /phoneAlert?firestation=<firestation_number>");
			return list;
		}
		return findByFS.findPhoneNumbersByFireStation(Integer.parseInt(stationNumber.get()));
	}
	
	@GetMapping("/fire")
	public JsonNode fireAddress(@RequestParam(name = "address") Optional<String> address) {
		/*Cette url doit retourner la liste des habitants vivant à l’adresse donnée ainsi que le numéro de la caserne
		 * de pompiers la desservant
		 */
		if (address.isEmpty()) {
			return TextNode.valueOf("Query parameter is : /fire?address=<address>");
		}
		return TextNode.valueOf("address = "+address.get());
	}
	
	@GetMapping("/flood/stations")
	public List<Map<String,String>> floodStationNumbers(@RequestParam(name = "stations") Optional<List<String>> stationNumbers) {
		/*Cette url doit retourner une liste de tous les foyers desservis par les casernes.
		 * Cette liste doit regrouper les personnes par adresse.
		 */		
		if (stationNumbers.isEmpty()) {
			List<Map<String,String>> listAddressPersons = new ArrayList<>();
			Map<String,String> mapAddressPerson = new HashMap<>();
			mapAddressPerson.put("Query parameter is","/flood/stations?stations=<a list of station_numbers>");
			listAddressPersons.add(mapAddressPerson);
			return listAddressPersons;
		}
		return null;
	}
	
	@GetMapping("/personInfo")
	public JsonNode personInfoFirstNameLastName(@RequestParam(name = "firstName") Optional<String> firstName, @RequestParam(name = "lastName") Optional<String> lastName) {
		/*Cette url doit retourner la liste des personnes vivant avec la personne donnée
		 * ainsi que la liste des personnes portant le même nom.
		 */		
		if (firstName.isEmpty() || lastName.isEmpty()) {
			return TextNode.valueOf("Query parameter is : /personInfo?firstName=<firstName>&lastName=<lastName>");
		}
		return TextNode.valueOf("firstName = " + firstName.get() + " - lastName = " + lastName.get());
	}
	
	@GetMapping("/communityEmail")
	public JsonNode communityEmailCity(@RequestParam(name = "city") Optional<String> city) {
		/*Cette url doit retourner les adresses mail de tous les habitants de la ville
		 */
		if (city.isEmpty()) {
			return TextNode.valueOf("Query parameter is : /communityEmail?city=<city>");
		}
		return TextNode.valueOf("city = "+city.get());
	}
}
