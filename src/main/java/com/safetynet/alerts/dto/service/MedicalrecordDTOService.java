package com.safetynet.alerts.dto.service;

import com.safetynet.alerts.dto.MedicalrecordDTO;
import com.safetynet.alerts.model.Medicalrecord;

public interface MedicalrecordDTOService {
	Medicalrecord convertMedicalrecordFromDTO(MedicalrecordDTO medicalrecordDTO);
	MedicalrecordDTO convertMedicalrecordToDTO(Medicalrecord medicalrecord);
}
