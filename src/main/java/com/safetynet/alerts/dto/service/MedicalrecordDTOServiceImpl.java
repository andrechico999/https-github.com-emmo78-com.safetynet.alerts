package com.safetynet.alerts.dto.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.dto.MedicalrecordDTO;
import com.safetynet.alerts.model.Medicalrecord;



@Service
public class MedicalrecordDTOServiceImpl implements MedicalrecordDTOService {

    @Autowired
	private ModelMapper modelMapper;
    
	@Override
	public Medicalrecord convertMedicalrecordFromDTO(MedicalrecordDTO medicalrecordDTO) {
		Converter<String, LocalDate> stringToLocalDate = new AbstractConverter<String, LocalDate>() {
			@Override
			protected LocalDate convert(String stringDate) {
				return LocalDate.parse(stringDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
			}	
		};
		modelMapper.typeMap(MedicalrecordDTO.class, Medicalrecord.class).addMappings(mapper -> 
			mapper.using(stringToLocalDate).map(MedicalrecordDTO::getBirthdate, Medicalrecord::setBirthdate));
		Medicalrecord medicalrecord = modelMapper.map(medicalrecordDTO, Medicalrecord.class);
		medicalrecord.setId(medicalrecordDTO.getFirstName()+" "+medicalrecordDTO.getLastName()); 
		return medicalrecord;

	}

	@Override
	public MedicalrecordDTO convertMedicalrecordToDTO(Medicalrecord medicalrecord) {
		// TODO Auto-generated method stub
		return null;
	}

}
