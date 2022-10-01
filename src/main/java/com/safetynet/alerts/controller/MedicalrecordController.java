package com.safetynet.alerts.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.safetynet.alerts.dto.MedicalrecordDTO;
import com.safetynet.alerts.exception.BadRequestException;
import com.safetynet.alerts.exception.ResourceConflictException;
import com.safetynet.alerts.service.MedicalrecordService;

@RestController
public class MedicalrecordController {

	@Autowired
	private MedicalrecordService medicalrecordService;
	
    @PostMapping("/medicalRecord")
    public ResponseEntity<MedicalrecordDTO> createMedicalrecord(@RequestBody Optional<MedicalrecordDTO> medicalrecord, WebRequest request) throws ResourceConflictException, BadRequestException {
    	if (!medicalrecord.isPresent()) {
    		throw new BadRequestException("Correct request should be a json medical record body");
    	}
	return new ResponseEntity<>(medicalrecordService.createMedicalrecord(medicalrecord.get(), request), HttpStatus.OK);
    }
    
    @PutMapping("/medicalRecord")
    public ResponseEntity<MedicalrecordDTO> updateMedicalrecord(@RequestBody Optional<MedicalrecordDTO> medicalrecord, WebRequest request) throws ResourceNotFoundException, BadRequestException {
    	if (!medicalrecord.isPresent()) {
    		throw new BadRequestException("Correct request should be a json medical record body");
    	}
	return new ResponseEntity<>(medicalrecordService.updateMedicalrecord(medicalrecord.get(), request), HttpStatus.OK);
    }
    
    @DeleteMapping("/medicalRecord")
    public ResponseEntity<MedicalrecordDTO> deleteMedicalrecord(@RequestBody Optional<MedicalrecordDTO> medicalrecord, WebRequest request) throws ResourceNotFoundException, BadRequestException {
    	if (!medicalrecord.isPresent()) {
    		throw new BadRequestException("Correct request should be a json medical record body");
    	}
   	return new ResponseEntity<>(medicalrecordService.deleteMedicalrecord(medicalrecord.get(), request), HttpStatus.OK);
    }
}
