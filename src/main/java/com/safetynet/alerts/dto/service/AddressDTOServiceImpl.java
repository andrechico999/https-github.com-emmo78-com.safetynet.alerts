package com.safetynet.alerts.dto.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.dto.AddressAdultChildDTO;
import com.safetynet.alerts.dto.AddressChildDTO;
import com.safetynet.alerts.dto.AddressPersonDTO;
import com.safetynet.alerts.dto.AddressPersonDTOPerson;
import com.safetynet.alerts.dto.AddressPersonDTOStationNumbers;
import com.safetynet.alerts.dto.AddressPersonEmailDTO;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.WriteToFile;
import lombok.Setter;


@Service
public class AddressDTOServiceImpl implements AddressDTOService {
    @Autowired
	private ModelMapper modelMapper;
    
    @Autowired
    private ObjectMapper objectMapper;
    
	@Autowired
	private WriteToFile fileWriter;
	
	@Setter
	private List<String> stationNumbers;
	
	@Override
	public List<AddressAdultChildDTO> addressChildrenToDTO(List<Person> addressChildren) {
		List<AddressAdultChildDTO> addressChildrenDTO = addressChildren.stream().map(this::convertAddressChildrenToDTO).collect(Collectors.toList());
		fileWriter.writeToFile(objectMapper.valueToTree(addressChildrenDTO));
		return addressChildrenDTO;
	}
	
	private AddressAdultChildDTO convertAddressChildrenToDTO(Person child) {
		//modelMapper.typeMap(Person.class, AddressChildDTO.class).addMappings(mapper -> mapper.skip(AddressChildDTO::setParents)); //should be necessary to avoid ValidationException ?
		AddressChildDTO addressChildDTO = modelMapper.map(child, AddressChildDTO.class);
		addressChildDTO.setAdults(child.getAddress().getPersons().values().stream().filter(person -> person.equals(child)&&(person.getAge() > 18)).map(person -> modelMapper.map(person, AddressAdultChildDTO.class)).collect(Collectors.toList()));
		return addressChildDTO;
	}

	@Override
	public List<AddressPersonDTO> addressPersonsToDTO(List<Person> addressPersons) {
		List<AddressPersonDTO> addressPersonsDTO = addressPersons.stream().map(this::addressPersonToDTO).collect(Collectors.toList());
		addressPersonsDTO.add(new AddressPersonDTOStationNumbers(stationNumbers));
		fileWriter.writeToFile(objectMapper.valueToTree(addressPersonsDTO));
		return addressPersonsDTO;
	}

	@Override
	public AddressPersonDTO addressPersonToDTO(Person addressPerson) {
		modelMapper.typeMap(Person.class, AddressPersonDTOPerson.class).addMappings(mapper -> {
			//mapper.map(Person::getAge, AddressPersonDTOPerson::setAge); //ModelMapper Handling Mismatches
			mapper.map(src -> src.getMedicalrecord().getMedications(), AddressPersonDTOPerson::setMedications);
			mapper.map(src -> src.getMedicalrecord().getAllergies(), AddressPersonDTOPerson::setAllergies);
		});
		return modelMapper.map(addressPerson, AddressPersonDTOPerson.class);
	}
	
	@Override
	public List<AddressPersonEmailDTO> addressPersonEmailToDTO(List<Person> addressPersonEmail) {
		List<AddressPersonEmailDTO> addressPersonEmailDTO = addressPersonEmail.stream().map(person -> modelMapper.map(person, AddressPersonEmailDTO.class)).distinct().collect(Collectors.toList());
		fileWriter.writeToFile(objectMapper.valueToTree(addressPersonEmailDTO));
		return addressPersonEmailDTO;
	}
}
