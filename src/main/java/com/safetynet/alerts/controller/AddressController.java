package com.safetynet.alerts.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.service.AddressService;

@RestController
public class AddressController {
	@Autowired
	AddressService addressService;
	
	@GetMapping("/childAlert")
	public String childAlertAddress(@RequestParam(name = "address") Optional<String> address) {
		/*Cette url doit retourner une liste d'enfants (âge <= 18 ans) habitant à cette adresse. S'il n'y a pas d'enfant,
		 * cette url peut renvoyer une chaîne vide
		 */
		if (address.isEmpty()) {
			return "Query parameter is : /childAlert?address=(address)";
		}
		return addressService.findChildrenByAddress(address.get());
	}
	
	@GetMapping("/fire")
	public String fireAddress(@RequestParam(name = "address") Optional<String> address) {
		/*Cette url doit retourner la liste des habitants vivant à l’adresse donnée ainsi que le numéro de la caserne
		 * de pompiers la desservant
		 */
		if (address.isEmpty()) {
			return "Query parameter is : /fire?address=(address)";
		}
		return addressService.findPersonsByAddress(address.get());
	}
}
