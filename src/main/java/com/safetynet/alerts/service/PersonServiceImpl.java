package com.safetynet.alerts.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.JsonRepository;
import com.safetynet.alerts.repository.JsonRepositoryImpl;
import com.safetynet.alerts.repository.WriteToFile;

@Service
public class PersonServiceImpl implements PersonService {

	private Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);
	
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
				.getAddress().getPersons().values().stream().filter(person -> person.equals(persons.get(id))).collect(Collectors.toList()));
		logger.info("{} : found {} persons", requestService.requestToString(request), personsAddressNameDTO.size());
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
		logger.info("{} : create person {} with success", requestService.requestToString(request), id);
		return personDTOService.convertPersonToDTO (person);
	}
	
	@Override
	public PersonDTO updatePerson(PersonDTO personDTO, WebRequest request) throws ResourceNotFoundException {
		Person person = personDTOService.convertPersonFromDTO(personDTO);
		String id = person.getId();
		Optional<Person> personToUpdateOpt = Optional.ofNullable(persons.get(id));
		Person personToUpdate = personToUpdateOpt.orElseThrow(() -> new ResourceNotFoundException("No person with id "+id+" to Update"));
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
		logger.info("{} : update person {} with success", requestService.requestToString(request), id);
		return personDTOService.convertPersonToDTO(jsonRepository.setPersonAddress(personToUpdate));
	}
	
	@Override
	public PersonDTO deletePerson(PersonDTO personDTO, WebRequest request) throws ResourceNotFoundException {
		Person person = personDTOService.convertPersonFromDTO(personDTO);
		String id = person.getId();
		Optional<Person> personToRemoveOpt = Optional.ofNullable(persons.remove(id));
		Person personToRemove = personToRemoveOpt.orElseThrow(() -> new ResourceNotFoundException("Person with id "+id+" does not exist for delete"));
		personToRemove.getAddress().detachPerson(personToRemove);
		logger.info("{} : delete person {} with success", requestService.requestToString(request), id);
		return personDTOService.convertPersonToDTO(Optional.ofNullable(persons.get(id)).orElseGet(() -> new Person()));
	}	
}
