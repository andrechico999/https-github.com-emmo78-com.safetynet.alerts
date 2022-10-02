package com.safetynet.alerts.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.safetynet.alerts.dto.PersonAddressNameDTO;
import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.dto.service.PersonDTOService;
import com.safetynet.alerts.exception.ResourceConflictException;
import com.safetynet.alerts.model.Address;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.JsonRepository;
import com.safetynet.alerts.repository.JsonRepositoryImpl;
import com.safetynet.alerts.repository.WriteToFile;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PersonServiceImpl implements PersonService {

	@Autowired
	private JsonRepository jsonRepository;
	
	@Autowired
	private RequestService requestService;
	
    @Autowired
	private PersonDTOService personDTOService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
	@Autowired
	private WriteToFile fileWriter;
	
	@Getter // Needed for Unit Test
	@Setter // Needed for Unit Test
	private Map<String, Person> persons;

	@PostConstruct
	public void personServiceImpl() {
		persons = ((JsonRepositoryImpl) jsonRepository).getPersons(); 
	}

	
	@Override
	public List<PersonAddressNameDTO> findPersonsByFirstNameAndLastName(String firstName, String lastName, WebRequest request) throws ResourceNotFoundException{
		firstName = requestService.upperCasingFirstLetter(firstName);
		lastName = requestService.upperCasingFirstLetter(lastName);
		final String id =firstName +" "+lastName; //Local variable lastName defined in an enclosing scope must be final or effectively final
		List<PersonAddressNameDTO> personsAddressNameDTO = personDTOService.personsAddressNameToDTO(Optional.ofNullable(persons.get(id)).orElseThrow(() -> {
			fileWriter.writeToFile(NullNode.instance);
			return new ResourceNotFoundException("No person found");})
				.getAddress().getPersons().values().stream().filter(person -> person.getLastName().equals(persons.get(id).getLastName())).collect(Collectors.toList()));
		log.info("{} : found {} persons", requestService.requestToString(request), personsAddressNameDTO.size());
		fileWriter.writeToFile(objectMapper.valueToTree(personsAddressNameDTO));
		return personsAddressNameDTO;
	}
	
	@Override
	public PersonDTO createPerson(PersonDTO personDTO, WebRequest request) throws ResourceConflictException {
		Person person = personDTOService.convertPersonFromDTO(personDTO);
		String id = person.getId();
		if (!persons.containsKey(id)) {
			person = jsonRepository.setPersonAddress(person);
			jsonRepository.setPersonMedicalrecord(person, id);
			persons.put(id, person);
		} else {
			throw new ResourceConflictException("Person "+id+" already exist");
		}
		log.info("{} : create person {} with success", requestService.requestToString(request), id);
		return personDTOService.convertPersonToDTO(person);
	}
	
	@Override
	public PersonDTO updatePerson(PersonDTO personDTO, WebRequest request) throws ResourceNotFoundException {
		Person person = personDTOService.convertPersonFromDTO(personDTO);
		String id = person.getId();
		Optional<Person> personToUpdateOpt = Optional.ofNullable(persons.get(id));
		Person personToUpdate = personToUpdateOpt.orElseThrow(() -> new ResourceNotFoundException("No person with id "+id+" to Update"));
		Address addressToUpdate = new Address(personToUpdate.getAddress());
		Optional.ofNullable(person.getAddress().getAddress()).ifPresent(address -> {
			personToUpdate.getAddress().detachPerson(personToUpdate);
			addressToUpdate.setAddress(address);				
		});   
		Optional.ofNullable(person.getAddress().getCity()).ifPresent(city -> {
			personToUpdate.getAddress().detachPerson(personToUpdate);
			addressToUpdate.setCity(city);				
		});   
		Optional.ofNullable(person.getAddress().getZip()).ifPresent(zip -> {
			personToUpdate.getAddress().detachPerson(personToUpdate);
			addressToUpdate.setZip(zip);				
		});
		Optional.ofNullable(person.getPhone()).ifPresent(phone -> 
			personToUpdate.setPhone(phone));   
		Optional.ofNullable(person.getEmail()).ifPresent(email -> 
			personToUpdate.setEmail(email));
		personToUpdate.setAddress(addressToUpdate);
		log.info("{} : update person {} with success", requestService.requestToString(request), id);
		return personDTOService.convertPersonToDTO(jsonRepository.setPersonAddress(personToUpdate));
	}
	
	@Override
	public PersonDTO deletePerson(PersonDTO personDTO, WebRequest request) throws ResourceNotFoundException {
		Person person = personDTOService.convertPersonFromDTO(personDTO);
		String id = person.getId();
		Optional<Person> personToRemoveOpt = Optional.ofNullable(persons.remove(id));
		Person personToRemove = personToRemoveOpt.orElseThrow(() -> new ResourceNotFoundException("Person with id "+id+" does not exist for delete"));
		personToRemove.getAddress().detachPerson(personToRemove);
		log.info("{} : delete person {} with success", requestService.requestToString(request), id);
		return personDTOService.convertPersonToDTO(Optional.ofNullable(persons.get(id)).orElseGet(() -> new Person()));
	}	
}
