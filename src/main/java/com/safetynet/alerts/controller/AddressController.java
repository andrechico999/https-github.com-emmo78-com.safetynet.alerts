package com.safetynet.alerts.controller;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.safetynet.alerts.service.AddressService;

@RestController
public class AddressController {
	
    @Autowired
    private ModelMapper modelMapper;
	
	@Autowired
	AddressService addressService;
	
	@GetMapping("/childAlert")
	public JsonNode childAlertAddress(@RequestParam(name = "address") Optional<String> address) {
		/*Cette url doit retourner une liste d'enfants (âge <= 18 ans) habitant à cette adresse,
		 * ainsi que les autres membres du foyer (firstName, lastName, age)
		 * S'il n'y a pas d'enfant, cette url peut renvoyer une chaîne vide
		 */
		if (address.isEmpty()) {
			return TextNode.valueOf("Query parameter is : /childAlert?address=<address>");
		}
		return addressService.findChildrenByAddress(address.get());
	}
	
	@GetMapping("/fire")
	public JsonNode fireAddress(@RequestParam(name = "address") Optional<String> address) {
		/*Cette url doit retourner la liste des habitants vivant à l’adresse donnée
		 * (lastName, phone, age, medicalrecord)
		 * ainsi que le numéro de la caserne de pompiers la desservant
		 */
		if (address.isEmpty()) {
			return TextNode.valueOf("Query parameter is : /fire?address=<address>");
		}
		return addressService.findPersonsByAddress(address.get());
	}
	
	@GetMapping("/communityEmail")
	public JsonNode communityEmailCity(@RequestParam(name = "city") Optional<String> city) {
		/*Cette url doit retourner les adresses mail de tous les habitants de la ville
		 */
		if (city.isEmpty()) {
			return TextNode.valueOf("Query parameter is : /communityEmail?city=<city>");
		}
		return addressService.findemailPersonsByCity(city.get());
	}
}
