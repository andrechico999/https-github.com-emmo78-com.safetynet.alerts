package com.safetynet.alerts.service;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.context.request.WebRequest;

import com.safetynet.alerts.dto.MedicalrecordDTO;
import com.safetynet.alerts.exception.ResourceConflictException;

/**
 * POST, PUT, DELETE (Create, Update, Delete) a medical record
 * @author Olivier MOREL
 *
 */
public interface MedicalrecordService {
	/**
	 * Creates a medical record and attaches it to the person with the same id (first name last name) if it exists.
	 * @param medicalrecordDTO : the POJO from json to create 
	 * @param request : the WebRequest
	 * @return medical record created
	 * @throws ResourceConflictException if the medical record already exists
	 */
	MedicalrecordDTO createMedicalrecord(MedicalrecordDTO medicalrecordDTO, WebRequest request) throws ResourceConflictException;
	
	/**
	 * Updates a medical record (if it was set to a person these is its reference (pointer) so no need to set again) 
	 * @param medicalrecordDTO : the POJO from json to update
	 * @param request : the WebRequest
	 * @return medical record updated
	 * @throws ResourceNotFoundException if there is no medical record with id to update
	 */
	MedicalrecordDTO updateMedicalrecord(MedicalrecordDTO medicalrecordDTO, WebRequest request) throws ResourceNotFoundException;
	
	/**
	 * Deletes a medical record and sets a new empty one to the person with the same id (first name last name) if it exists. 
	 * @param medicalrecordDTO : the POJO from json to delete
	 * @param request : the WebRequest
	 * @return the get(id) from the map so a null (converted to an empty medical record by DTO)
	 * @throws ResourceNotFoundException if there is no medical record with id to delete
	 */
	MedicalrecordDTO deleteMedicalrecord(MedicalrecordDTO medicalrecordDTO, WebRequest request) throws ResourceNotFoundException;
}
