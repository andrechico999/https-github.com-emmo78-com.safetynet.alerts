package com.safetynet.alerts.service;

import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import com.safetynet.alerts.dto.MedicalrecordDTO;
import com.safetynet.alerts.dto.service.MedicalrecordDTOService;
import com.safetynet.alerts.exception.ResourceConflictException;
import com.safetynet.alerts.model.Medicalrecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.JsonRepository;
import com.safetynet.alerts.repository.JsonRepositoryImpl;

@Service
public class MedicalrecordServiceImpl implements MedicalrecordService {
	
	private Logger logger = LoggerFactory.getLogger(MedicalrecordServiceImpl.class);
	
	@Autowired
	private JsonRepository jsonRepository;
	
    @Autowired
	private MedicalrecordDTOService medicalrecordDTOService;
    
	@Autowired
	private RequestService requestService;
	
    private Map<String, Medicalrecord> medicalrecords;

	@PostConstruct
	public void personServiceImpl() {
		medicalrecords = ((JsonRepositoryImpl) jsonRepository).getMedicalrecords();
	}

	@Override
	public MedicalrecordDTO createMedicalrecord(MedicalrecordDTO medicalrecordDTO, WebRequest request) throws ResourceConflictException {
		Medicalrecord medicalrecord = medicalrecordDTOService.convertMedicalrecordFromDTO(medicalrecordDTO);
		String id = medicalrecord.getId();
		if (!medicalrecords.containsKey(id)) {
			medicalrecords.put(id, medicalrecord);
			jsonRepository.setMedicalrecordToPerson(id);
		} else {
			throw new ResourceConflictException("Medicalrecord "+id+"  already exist");  
		}
		logger.info("{} : create medical record {} with success", requestService.requestToString(request), id);
		return medicalrecordDTOService.convertMedicalrecordToDTO(medicalrecords.get(id));
	}

	@Override
	public MedicalrecordDTO updateMedicalrecord(MedicalrecordDTO medicalrecordDTO, WebRequest request) throws ResourceNotFoundException {
		Medicalrecord medicalrecord = medicalrecordDTOService.convertMedicalrecordFromDTO(medicalrecordDTO);
		String id = medicalrecord.getId();
		Optional<Medicalrecord> medicalrecordToUpdateOpt = Optional.ofNullable(medicalrecords.get(id));
		Medicalrecord medicalrecordToUpdate = medicalrecordToUpdateOpt.orElseThrow(() -> new ResourceNotFoundException("No medicalrecord with id "+id+" to Update"));
		Optional.ofNullable(medicalrecord.getBirthdate()).ifPresent(birthdate -> medicalrecordToUpdate.setBirthdate(birthdate));   
		Optional.ofNullable(medicalrecord.getMedications()).ifPresent(medications -> medicalrecordToUpdate.setMedications(medications));				
		Optional.ofNullable(medicalrecord.getAllergies()).ifPresent(allergies -> medicalrecordToUpdate.setAllergies(allergies));
		logger.info("{} : update medical record {} with success", requestService.requestToString(request), id);
		return medicalrecordDTOService.convertMedicalrecordToDTO(medicalrecords.get(id));
	}

	@Override
	public MedicalrecordDTO deleteMedicalrecord(MedicalrecordDTO medicalrecordDTO, WebRequest request) throws ResourceNotFoundException {
		Medicalrecord medicalrecord = medicalrecordDTOService.convertMedicalrecordFromDTO(medicalrecordDTO);
		String id = medicalrecord.getId();
		if (medicalrecords.containsKey(id)) {
			medicalrecords.remove(id);
			jsonRepository.setMedicalrecordToPerson(id); //if person exist set a new Medicalrecord()
		} else {
			throw new ResourceNotFoundException("Medicalrecord with id "+id+" does not exist for delete");  
		}
		logger.info("{} : delete medical record {} with success", requestService.requestToString(request), id);		
		return medicalrecordDTOService.convertMedicalrecordToDTO(Optional.ofNullable(((JsonRepositoryImpl) jsonRepository).getPersons().get(id)).orElseGet(() -> {return new Person();}).getMedicalrecord());
	}
}
