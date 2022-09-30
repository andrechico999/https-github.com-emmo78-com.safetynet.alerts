package com.safetynet.alerts.service;

import java.util.List;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.context.request.WebRequest;

import com.safetynet.alerts.dto.PersonAddressNameDTO;
import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.exception.ResourceConflictException;

/**
 * GET (Read) persons by person
 * POST, PUT, DELETE (Add, Update, Delete) a person
 * @author Olivier MOREL
 *
 */
public interface PersonService {
	/**
	 * return list of people with the same name living at the address of the specified person
	 * @param firstName : of a person to find
	 * @param lastName : of same person to find
	 * @return : the persons living with
	 * @throws ResourceNotFoundException : if no person found
	 */
	List<PersonAddressNameDTO> findPersonsByFirstNameAndLastName(String firstName, String lastName, WebRequest request) throws ResourceNotFoundException;

	/**
	 * Creates a person and attaches its medical records if exists.
	 * @param personDTO : POJO fron json to create
	 * @param request : WebRequest
	 * @return : the created person
	 * @throws ResourceConflictException if the person already exists
	 */
	PersonDTO createPerson(PersonDTO personDTO, WebRequest request) throws ResourceConflictException;
	
	/**
	 * Updates a person 
	 * @param personDTO : POJO fron json to update
	 * @param request : WebRequest
	 * @return : the updated person
	 * @throws ResourceNotFoundException if person to update not found
	 */
	PersonDTO updatePerson(PersonDTO personDTO, WebRequest request) throws ResourceNotFoundException;
	
	/**
	 * 
	 * @param personDTO : POJO fron json to delete
	 * @param request : WebRequest
	 * @return : if persons.get(id) retrun null, return a new Person() with null fields value
	 * @throws ResourceNotFoundException if person to delete not found
	 */
	PersonDTO deletePerson(PersonDTO personDTO, WebRequest request) throws ResourceNotFoundException;
}
