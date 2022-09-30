package com.safetynet.alerts.dto.service;

import com.safetynet.alerts.dto.MedicalrecordDTO;
import com.safetynet.alerts.model.Medicalrecord;

/**
 * Map from and to DTO medical record
 * @author Olivier MOREL
 *
 */
public interface MedicalrecordDTOService {
	Medicalrecord convertMedicalrecordFromDTO(MedicalrecordDTO medicalrecordDTO);
	MedicalrecordDTO convertMedicalrecordToDTO(Medicalrecord medicalrecord);
}
