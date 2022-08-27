package com.safetynet.alerts.controller;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.safetynet.alerts.service.PersonService;

@RestController
public class PersonController {
	
    @Autowired
    private ModelMapper modelMapper;
	
	@Autowired
	PersonService personService;
		
	@GetMapping("/personInfo")
	public JsonNode personInfoFirstNameLastName(@RequestParam(name = "firstName") Optional<String> firstName, @RequestParam(name = "lastName") Optional<String> lastName) {
		/*Cette url doit retourner la personne et la liste des personnes habitant à la même adresse
		 * ainsi que la liste des personnes portant le même nom (lastName, address, age, email, medicalrecord)
		 */		
		if (firstName.isEmpty() || lastName.isEmpty()) {
			return TextNode.valueOf("Query parameter is : /personInfo?firstName=<firstName>&lastName=<lastName>");
		}
		return personService.findPersonsByFirstNameAndLastName(firstName.get(), lastName.get());
	}
	
}
