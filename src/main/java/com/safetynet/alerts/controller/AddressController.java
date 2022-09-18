package com.safetynet.alerts.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.dto.AddressAdultChildDTO;
import com.safetynet.alerts.dto.AddressPersonDTO;
import com.safetynet.alerts.dto.AddressPersonEmailDTO;
import com.safetynet.alerts.service.AddressService;

@RestController
public class AddressController {
	
    @Autowired
	private AddressService addressService;
	
	@GetMapping("/childAlert")
	public List<AddressAdultChildDTO> childAlertAddress(@RequestParam(name = "address") Optional<String> address) {
		/*Cette url doit retourner une liste d'enfants (âge <= 18 ans) habitant à cette adresse,
		 * ainsi que les autres membres du foyer (firstName, lastName, age)
		 * S'il n'y a pas d'enfant, cette url peut renvoyer une chaîne vide
		 */
		if (address.isPresent()) {
			return addressService.findChildrenByAddress(address.get());
		}
		return null;
	}
	
	@GetMapping("/fire")
	public List<AddressPersonDTO> fireAddress(@RequestParam(name = "address") Optional<String> address) {
		/*Cette url doit retourner la liste des habitants vivant à l’adresse donnée
		 * (lastName, phone, age, medicalrecord)
		 * ainsi que le numéro de la caserne de pompiers la desservant
		 */
		if (address.isPresent()) {
			return addressService.findPersonsByAddress(address.get());
		}
		return null;
	}
	
	@GetMapping("/communityEmail")
	public List<AddressPersonEmailDTO> communityEmailCity(@RequestParam(name = "city") Optional<String> city) {
		/*Cette url doit retourner les adresses mail de tous les habitants de la ville
		 */
		if (city.isPresent()) {
			return addressService.findemailPersonsByCity(city.get());
		}
		return null;
	}
	
	
	@GetMapping("/toto") 
	public ResponseEntity<String> getToto() {
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body("toto");
	}
}
