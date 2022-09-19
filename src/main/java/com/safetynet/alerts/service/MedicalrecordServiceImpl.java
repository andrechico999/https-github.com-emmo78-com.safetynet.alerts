package com.safetynet.alerts.service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.dto.MedicalrecordDTO;
import com.safetynet.alerts.dto.service.MedicalrecordDTOService;
import com.safetynet.alerts.model.Medicalrecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.JsonRepository;
import com.safetynet.alerts.repository.JsonRepositoryImpl;

@Service
public class MedicalrecordServiceImpl implements MedicalrecordService {
	@Autowired
	private JsonRepository jsonRepository;
	
    @Autowired
	private MedicalrecordDTOService medicalrecordDTOService;
	
    private Map<String, Medicalrecord> medicalrecords;

	@PostConstruct
	public void personServiceImpl() {
		medicalrecords = ((JsonRepositoryImpl) jsonRepository).getMedicalrecords();
	}

	@Override
	public MedicalrecordDTO createMedicalrecord(MedicalrecordDTO medicalrecordDTO) {
		Medicalrecord medicalrecord = medicalrecordDTOService.convertMedicalrecordFromDTO(medicalrecordDTO);
		String id = medicalrecord.getId();
		if (!medicalrecords.containsKey(id)) {
			medicalrecords.put(id, medicalrecord);
			jsonRepository.setMedicalrecordToPerson(id);
		} else {
			// TODO throw new Exception("Medicalrecord already exist");  
		}
		return medicalrecordDTOService.convertMedicalrecordToDTO(medicalrecords.get(id));
	}

	@Override
	public MedicalrecordDTO updateMedicalrecord(MedicalrecordDTO medicalrecordDTO) {
		Medicalrecord medicalrecord = medicalrecordDTOService.convertMedicalrecordFromDTO(medicalrecordDTO);
		String id = medicalrecord.getId();
		Optional<Medicalrecord> medicalrecordToUpdateOpt = Optional.ofNullable(medicalrecords.get(id));
		try {
			Medicalrecord medicalrecordToUpdate = medicalrecordToUpdateOpt.orElseThrow(() -> new Exception("No medicalrecord To Update"));
			Optional.ofNullable(medicalrecord.getBirthdate()).ifPresent(birthdate -> medicalrecordToUpdate.setBirthdate(birthdate));   
			Optional.ofNullable(medicalrecord.getMedications()).ifPresent(medications -> medicalrecordToUpdate.setMedications(medications));				
			Optional.ofNullable(medicalrecord.getAllergies()).ifPresent(allergies -> medicalrecordToUpdate.setAllergies(allergies));				
			jsonRepository.setMedicalrecordToPerson(id);
		} catch (Exception e) {
			// TODO No medicalrecord to Update
		}
		return medicalrecordDTOService.convertMedicalrecordToDTO(medicalrecords.get(id));
	}

	@Override
	public MedicalrecordDTO deleteMedicalrecord(MedicalrecordDTO medicalrecordDTO) {
		Medicalrecord medicalrecord = medicalrecordDTOService.convertMedicalrecordFromDTO(medicalrecordDTO);
		String id = medicalrecord.getId();
		if (medicalrecords.containsKey(id)) {
			medicalrecords.remove(id);
			jsonRepository.setMedicalrecordToPerson(id);
		} else {
			// TODO throw new Exception("Medicalrecord does not exist for delete");  
		}
		medicalrecord = Optional.ofNullable(((JsonRepositoryImpl) jsonRepository).getPersons().get(id)).orElseGet(() -> {return new Person();}).getMedicalrecord();
		medicalrecord.setId(", ,");
		medicalrecord.setBirthdate(LocalDate.now());
		return medicalrecordDTOService.convertMedicalrecordToDTO(medicalrecord);
	}

}
