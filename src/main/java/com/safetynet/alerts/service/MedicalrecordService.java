package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.MedicalrecordDTO;

public interface MedicalrecordService {
	MedicalrecordDTO createMedicalrecord(MedicalrecordDTO medicalrecordDTO);
	MedicalrecordDTO updateMedicalrecord(MedicalrecordDTO medicalrecordDTO);
	MedicalrecordDTO deleteMedicalrecord(MedicalrecordDTO medicalrecordDTO);
}
