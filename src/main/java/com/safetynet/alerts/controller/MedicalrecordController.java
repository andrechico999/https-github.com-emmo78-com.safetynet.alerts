package com.safetynet.alerts.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.safetynet.alerts.dto.MedicalrecordDTO;
import com.safetynet.alerts.service.MedicalrecordService;

@RestController
public class MedicalrecordController {

	@Autowired
	MedicalrecordService medicalrecordService;
	
    @PostMapping("/medicalRecord")
    public MedicalrecordDTO createMedicalrecord(@RequestBody Optional<MedicalrecordDTO> medicalrecord, WebRequest request) {
    	if (medicalrecord.isPresent()) {
    		return medicalrecordService.createMedicalrecord(medicalrecord.get());
    	}
	return null;
    }
    
    @PutMapping("/medicalRecord")
    public MedicalrecordDTO updateMedicalrecord(@RequestBody Optional<MedicalrecordDTO> medicalrecord, WebRequest request) {
    	if (medicalrecord.isPresent()) {
    		return medicalrecordService.updateMedicalrecord(medicalrecord.get());
    	}
	return null;
    }
    
    @DeleteMapping("/medicalRecord")
    public MedicalrecordDTO deleteMedicalrecord(@RequestBody Optional<MedicalrecordDTO> medicalrecord, WebRequest request) {
    	if (medicalrecord.isPresent()) {
    		return medicalrecordService.deleteMedicalrecord(medicalrecord.get());
    	}
	return null;
    }
}
