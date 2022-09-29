package com.safetynet.alerts.dto.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		Converter<LocalDate, String> localDateToString = new AbstractConverter<LocalDate, String>() {
			@Override
			protected String convert(LocalDate date) {
				String dateStr;
				if (date == null) {
					dateStr = null;
				} else {
					dateStr = date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
				}
				return dateStr;
			}	
		};
		modelMapper.typeMap(Medicalrecord.class, MedicalrecordDTO.class).addMappings(mapper -> 
			mapper.using(localDateToString).map(Medicalrecord::getBirthdate, MedicalrecordDTO::setBirthdate));
		MedicalrecordDTO medicalrecordDTO = modelMapper.map(medicalrecord, MedicalrecordDTO.class);
		String id = medicalrecord.getId();
		if (id == null) {
			medicalrecordDTO.setFirstName(null);
			medicalrecordDTO.setLastName(null);
		} else {
			String[] names = id.split(" ");
			medicalrecordDTO.setFirstName(names[0]);
			medicalrecordDTO.setLastName(names[1]);
		}
		return medicalrecordDTO;
	}
}
