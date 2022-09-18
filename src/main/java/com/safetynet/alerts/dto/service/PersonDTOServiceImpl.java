package com.safetynet.alerts.dto.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.dto.PersonAddressNameDTO;
import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.WriteToFile;

@Service
public class PersonDTOServiceImpl implements PersonDTOService {
    @Autowired
	private ModelMapper modelMapper;
    
    @Autowired
    private ObjectMapper objectMapper;
    
	@Autowired
	private WriteToFile fileWriter;

	@Override
	public Person convertPersonFromDTO(PersonDTO personDTO) {
		modelMapper.typeMap(PersonDTO.class, Person.class).addMappings(mapper -> {
			mapper.<String>map(PersonDTO::getAddress, (dest, v) -> dest.getAddress().setAddress(v));
			mapper.<String>map(PersonDTO::getCity, (dest, v) -> dest.getAddress().setCity(v));
			mapper.<String>map(PersonDTO::getZip, (dest, v) -> dest.getAddress().setZip(v));
			});
		Person person = modelMapper.map(personDTO, Person.class);
		person.buildId();
		return person;
	}

	@Override
	public PersonDTO convertPersonToDTO (Person person) {
		modelMapper.typeMap(Person.class, PersonDTO.class).addMappings(mapper -> {
			mapper.<String>map(src -> src.getAddress().getAddress(), PersonDTO::setAddress);
			mapper.<String>map(src -> src.getAddress().getCity(), PersonDTO::setCity);
			mapper.<String>map(src -> src.getAddress().getZip(), PersonDTO::setZip);
		});
		return modelMapper.map(person, PersonDTO.class);
	}
	
	@Override
	public List<PersonAddressNameDTO> personsAddressNameToDTO(List<Person> personsAddressName) {
		modelMapper.typeMap(Person.class, PersonAddressNameDTO.class).addMappings(mapper -> {
			mapper.map(src -> src.getAddress().getAddress(), PersonAddressNameDTO::setAddress);
			//mapper.<String>map(Person::getAge, FirestationsPersonDTO::setAge); //ModelMapper Handling Mismatches
			mapper.map(src -> src.getMedicalrecord().getMedications(), PersonAddressNameDTO::setMedications);
			mapper.map(src -> src.getMedicalrecord().getAllergies(), PersonAddressNameDTO::setAllergies);
		});	
		List<PersonAddressNameDTO> personsAddressNameDTO = personsAddressName.stream().map(person -> modelMapper.map(person, PersonAddressNameDTO.class)).collect(Collectors.toList());
		fileWriter.writeToFile(objectMapper.valueToTree(personsAddressNameDTO));
		return personsAddressNameDTO;
	}

}
