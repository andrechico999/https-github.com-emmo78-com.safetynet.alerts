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
import org.springframework.web.context.request.WebRequest;

import com.safetynet.alerts.dto.PersonAddressNameDTO;
import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.exception.BadRequestException;
import com.safetynet.alerts.exception.ResourceConflictException;
import com.safetynet.alerts.service.PersonService;

@RestController
public class PersonController {
	
	@Autowired
	PersonService personService;
		
	@GetMapping("/personInfo")
	public ResponseEntity<List<PersonAddressNameDTO>> personInfoFirstNameLastName(@RequestParam(name = "firstName") Optional<String> firstName, @RequestParam(name = "lastName") Optional<String> lastName, WebRequest request) throws ResourceNotFoundException, BadRequestException {
		/*Cette url doit retourner la personne et la liste des personnes habitant à la même adresse
		 * ainsi que la liste des personnes portant le même nom (lastName, address, age, email, medicalrecord)
		 */		
		if (!(firstName.isPresent() && lastName.isPresent())) {
			throw new BadRequestException("Correct request should be http://localhost:8080/personInfo?firstName=john&lastName=boyd");
		}
	return new ResponseEntity<>(personService.findPersonsByFirstNameAndLastName(firstName.get(), lastName.get(), request), HttpStatus.OK);
	}
	
    @PostMapping("/person")
    public ResponseEntity<PersonDTO> createPerson(@RequestBody Optional<PersonDTO> person, WebRequest request) throws ResourceConflictException, BadRequestException {
    	if (!person.isPresent()) {
    		throw new BadRequestException("Correct request should be a json person body");
    	}
	return new ResponseEntity<>(personService.createPerson(person.get(), request), HttpStatus.OK);
    }
    
    @PutMapping("/person")
    public ResponseEntity<PersonDTO> updatePerson(@RequestBody Optional<PersonDTO> person, WebRequest request) throws ResourceNotFoundException, BadRequestException {
    	if (!person.isPresent()) {
    		throw new BadRequestException("Correct request should be a json person body");
    	}
	return new ResponseEntity<>(personService.updatePerson(person.get(), request), HttpStatus.OK);
    }
    
    @DeleteMapping("/person")
    public ResponseEntity<PersonDTO> deletePerson(@RequestBody Optional<PersonDTO> person, WebRequest request) throws ResourceNotFoundException, BadRequestException {
    	if (!person.isPresent()) {
    		throw new BadRequestException("Correct request should be a json person body");
    	}
	return new ResponseEntity<>(personService.deletePerson(person.get(), request), HttpStatus.OK);
    }
    
}
