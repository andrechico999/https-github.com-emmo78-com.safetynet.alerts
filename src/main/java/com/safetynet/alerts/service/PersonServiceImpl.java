package com.safetynet.alerts.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.dto.PersonAddressNameDTO;
import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.dto.service.PersonDTOService;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.JsonRepository;
import com.safetynet.alerts.repository.JsonRepositoryImpl;

@Service
public class PersonServiceImpl implements PersonService {

	@Autowired
	private JsonRepository jsonRepository;
	
	@Autowired
	private RequestService requestService;
	
    @Autowired
	private PersonDTOService personDTOService;
	
	private Map<String, Person> persons;

	@PostConstruct
	public void personServiceImpl() {
		persons = ((JsonRepositoryImpl) jsonRepository).getPersons(); 
	}

	
	@Override
	public List<PersonAddressNameDTO> findPersonsByFirstNameAndLastName(String firstName, String lastName) {

		firstName = requestService.upperCasingFirstLetter(firstName);
		lastName = requestService.upperCasingFirstLetter(lastName);
		final String id =firstName +" "+lastName; //Local variable lastName defined in an enclosing scope must be final or effectively final
		return personDTOService.personsAddressNameToDTO(persons.get(id).getAddress().getPersons().values().stream().filter(person -> person.equals(persons.get(id))).collect(Collectors.toList()));
	}
	
	@Override
	public PersonDTO createPerson(PersonDTO personDTO) {
		Person person = personDTOService.convertPersonFromDTO(personDTO);
		String id = person.getId();
		if (!persons.containsKey(id)) {
			person = jsonRepository.setPersonAddress(person);
			jsonRepository.setPersonMedicalrecord(person, id);
			persons.put(id, person);
		} else {
			//TODO person already exist}
		}
		return personDTOService.convertPersonToDTO (person);
	}
	
	@Override
	public PersonDTO updatePerson(PersonDTO personDTO) {
		Person person = personDTOService.convertPersonFromDTO(personDTO);
		Optional<Person> personToUpdateOpt = Optional.ofNullable(persons.get(person.getId()));
		try {
			Person personToUpdate = personToUpdateOpt.orElseThrow(() -> new Exception("No One To Update"));
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
		} catch (Exception e) {
			// TODO No one to Update
		}
		return personDTOService.convertPersonToDTO(jsonRepository.setPersonAddress(personToUpdateOpt.get()));
	}
	
	@Override
	public PersonDTO deletePerson(PersonDTO personDTO) {
		Person person = personDTOService.convertPersonFromDTO(personDTO);
		Optional<Person> personToRemoveOpt = Optional.ofNullable(persons.remove(person.getId()));
		try {
			Person personToRemove = personToRemoveOpt.orElseThrow(() -> new Exception("No One To remove"));
			personToRemove.getAddress().detachPerson(personToRemove);
		} catch (Exception e) {
			// TODO No One to remove
			e.printStackTrace();
		}
		return personDTOService.convertPersonToDTO(personToRemoveOpt.get());
	}	
}
