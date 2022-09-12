package com.safetynet.alerts.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.dto.PersonAddressNameDTO;
import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.JsonRepository;
import com.safetynet.alerts.repository.JsonRepositoryImpl;
import com.safetynet.alerts.repository.WriteToFile;

@Service
public class PersonServiceImpl implements PersonService {

	@Autowired
	private JsonRepository jsonRepository;
	
	@Autowired
	private StringService stringService;
	
    @Autowired
	private ModelMapper modelMapper;
    
    @Autowired
    private ObjectMapper objectMapper;
    
	@Autowired
	private WriteToFile fileWriter;
	
	private Map<String, Person> persons;

	@PostConstruct
	public void personServiceImpl() {
		persons = ((JsonRepositoryImpl) jsonRepository).getPersons(); 
	}

	
	@Override
	public List<PersonAddressNameDTO> findPersonsByFirstNameAndLastName(String firstName, String lastName) {

		firstName = stringService.upperCasingFirstLetter(firstName);
		lastName = stringService.upperCasingFirstLetter(lastName);
		final String id =firstName +" "+lastName; //Local variable lastName defined in an enclosing scope must be final or effectively final
		return personsAddressNameToDTO(persons.get(id).getAddress().getPersons().values().stream().filter(person -> person.equals(persons.get(id))).collect(Collectors.toList()));
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


	@Override
	public PersonDTO createPerson(PersonDTO personDTO) {
		Person person = jsonRepository.convertPersonFromDTO(personDTO);
		if (!persons.containsKey(person.getId())) {
			person = jsonRepository.setPersonAddress(person);
			persons.put(person.getId(), person);
		}
		return convertPersonToDTO (person);
	}
	
	@Override
	public PersonDTO updatePerson(PersonDTO personDTO) {
		Person person = jsonRepository.convertPersonFromDTO(personDTO);
		Optional<Person> personToUpdateOpt = Optional.ofNullable(persons.get(person.getId()));
		personToUpdateOpt.ifPresentOrElse(
				personToUpdate -> {
			Optional.ofNullable(person.getAddress().getAddress()).ifPresent(address -> {
				personToUpdate.getAddress().detachPerson(personToUpdate);
				personToUpdate.getAddress().setAddress(address);				
			});   
			Optional.ofNullable(person.getAddress().getCity()).ifPresent(city -> {
				personToUpdate.getAddress().detachPerson(personToUpdate);
				personToUpdate.getAddress().setCity(city);				
			});   
			Optional.ofNullable(person.getAddress().getZip()).ifPresent(zip -> {
				personToUpdate.getAddress().detachPerson(personToUpdate);
				personToUpdate.getAddress().setZip(zip);				
			});   
			Optional.ofNullable(person.getPhone()).ifPresent(phone -> 
				personToUpdate.setPhone(phone));   
			Optional.ofNullable(person.getEmail()).ifPresent(email -> 
				personToUpdate.setEmail(email));   
				},
				() -> {
					//ToDo if no one to Update
				});
		return convertPersonToDTO(jsonRepository.setPersonAddress(personToUpdateOpt.get()));
	}
	
	@Override
	public PersonDTO deletePerson(PersonDTO personDTO) {
		Person person = jsonRepository.convertPersonFromDTO(personDTO);
		Optional<Person> personToRemoveOpt = Optional.ofNullable(persons.remove(person.getId()));
		personToRemoveOpt.ifPresentOrElse(
				personToRemove -> {
					personToRemove.getAddress().detachPerson(personToRemove);
				},
				() -> {
					// ToDo if non one to remove
				});
		return convertPersonToDTO(jsonRepository.setPersonAddress(personToRemoveOpt.get()));
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

}
