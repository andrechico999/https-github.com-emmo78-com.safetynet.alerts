package com.safetynet.alerts.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.dto.PersonAddressNameDTO;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.WriteToFile;
import com.safetynet.alerts.service.PersonService;

@RestController
public class PersonController {
	
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private ObjectMapper objectMapper;

	@Autowired
	private WriteToFile fileWriter;
	
	@Autowired
	PersonService personService;
		
	@GetMapping("/personInfo")
	public List<PersonAddressNameDTO> personInfoFirstNameLastName(@RequestParam(name = "firstName") Optional<String> firstName, @RequestParam(name = "lastName") Optional<String> lastName) {
		/*Cette url doit retourner la personne et la liste des personnes habitant à la même adresse
		 * ainsi que la liste des personnes portant le même nom (lastName, address, age, email, medicalrecord)
		 */		
		if (firstName.isPresent() && lastName.isPresent()) {
			modelMapper.typeMap(Person.class, PersonAddressNameDTO.class).addMappings(mapper -> {
				mapper.map(src -> src.getAddress().getAddress(), PersonAddressNameDTO::setAddress);
				//mapper.<String>map(Person::getAge, FirestationsPersonDTO::setAge); //ModelMapper Handling Mismatches
				mapper.map(src -> src.getMedicalrecord().getMedications(), PersonAddressNameDTO::setMedications);
				mapper.map(src -> src.getMedicalrecord().getAllergies(), PersonAddressNameDTO::setAllergies);
			});	
			List<PersonAddressNameDTO> personsAddressName = personService.findPersonsByFirstNameAndLastName(firstName.get(), lastName.get()).stream().map(person -> modelMapper.map(person, PersonAddressNameDTO.class)).collect(Collectors.toList());
			fileWriter.writeToFile(objectMapper.valueToTree(personsAddressName));
			return personsAddressName;
		}
		return null;
	}
	
}