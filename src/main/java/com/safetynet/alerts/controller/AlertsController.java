package com.safetynet.alerts.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.service.AddressService;
import com.safetynet.alerts.service.PersonService;

@RestController
public class AlertsController {
	
	@Autowired
	AddressService findByAddress;
	
	@Autowired
	PersonService findByP;
	


	

	
	@GetMapping("/personInfo")
	public List<String> personInfoFirstNameLastName(@RequestParam(name = "firstName") Optional<String> firstName, @RequestParam(name = "lastName") Optional<String> lastName) {
		/*Cette url doit retourner la liste des personnes vivant avec la personne donnée
		 * ainsi que la liste des personnes portant le même nom.
		 */		
		if (firstName.isEmpty() || lastName.isEmpty()) {
			List<String> query = new ArrayList<>();
			query.add("Query parameter is : /personInfo?firstName=<firstName>&lastName=<lastName>");
			return query;
		}
		return findByP.findPersonsByFirstNameAndLastName(firstName.get(), lastName.get());
	}
	
	@GetMapping("/communityEmail")
	public List<String> communityEmailCity(@RequestParam(name = "city") Optional<String> city) {
		/*Cette url doit retourner les adresses mail de tous les habitants de la ville
		 */
		if (city.isEmpty()) {
			List<String> query = new ArrayList<>();
			query.add("Query parameter is : /communityEmail?city=<city>");
			return query;
		}
		return findByAddress.findemailPersonsByCity(city.get());
	}
}
