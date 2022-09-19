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

import com.safetynet.alerts.dto.PersonAddressNameDTO;
import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.service.PersonService;

@RestController
public class PersonController {
	
	@Autowired
	PersonService personService;
		
	@GetMapping("/personInfo")
	public List<PersonAddressNameDTO> personInfoFirstNameLastName(@RequestParam(name = "firstName") Optional<String> firstName, @RequestParam(name = "lastName") Optional<String> lastName) {
		/*Cette url doit retourner la personne et la liste des personnes habitant à la même adresse
		 * ainsi que la liste des personnes portant le même nom (lastName, address, age, email, medicalrecord)
		 */		
		if (firstName.isPresent() && lastName.isPresent()) {
			return personService.findPersonsByFirstNameAndLastName(firstName.get(), lastName.get());
		}
		return null;
	}
	
    @PostMapping("/person")
    public PersonDTO createPerson(@RequestBody Optional<PersonDTO> person) {
    	if (person.isPresent()) {
    		return personService.createPerson(person.get());
    	}
	return null;
    }
    
    @PutMapping("/person")
    public PersonDTO updatePerson(@RequestBody Optional<PersonDTO> person) {
    	if (person.isPresent()) {
    		return personService.updatePerson(person.get());
    	}
	return null;
    }
    
    @DeleteMapping("/person")
    public PersonDTO deletePerson(@RequestBody Optional<PersonDTO> person) {
    	if (person.isPresent()) {
    		return personService.deletePerson(person.get());
    	}
	return null;
    }
    
}
