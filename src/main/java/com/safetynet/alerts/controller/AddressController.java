package com.safetynet.alerts.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.dto.AddressPersonChildrenDTO;
import com.safetynet.alerts.dto.AddressPersonDTO;
import com.safetynet.alerts.dto.AddressPersonDTOPerson;
import com.safetynet.alerts.dto.AddressPersonDTOStationNumbers;
import com.safetynet.alerts.dto.AddressPersonEmailDTO;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.WriteToFile;
import com.safetynet.alerts.service.AddressService;

@RestController
public class AddressController {
	
    @Autowired
    private ModelMapper modelMapper;
	
    @Autowired
    private ObjectMapper objectMapper;

	@Autowired
	private WriteToFile fileWriter;
	
    @Autowired
	private AddressService addressService;
	
	@GetMapping("/childAlert")
	public List<AddressPersonChildrenDTO> childAlertAddress(@RequestParam(name = "address") Optional<String> address) {
		/*Cette url doit retourner une liste d'enfants (âge <= 18 ans) habitant à cette adresse,
		 * ainsi que les autres membres du foyer (firstName, lastName, age)
		 * S'il n'y a pas d'enfant, cette url peut renvoyer une chaîne vide
		 */
		if (address.isPresent()) {
			List<AddressPersonChildrenDTO> addressPersonChildren = addressService.findChildrenByAddress(address.get()).stream().map(person -> modelMapper.map(person, AddressPersonChildrenDTO.class)).collect(Collectors.toList());
			fileWriter.writeToFile(objectMapper.valueToTree(addressPersonChildren));
			return addressPersonChildren;
		}
		return null;
	}
	
	@GetMapping("/fire")
	public List<AddressPersonDTO> fireAddress(@RequestParam(name = "address") Optional<String> address) {
		/*Cette url doit retourner la liste des habitants vivant à l’adresse donnée
		 * (lastName, phone, age, medicalrecord)
		 * ainsi que le numéro de la caserne de pompiers la desservant
		 */
		if (address.isPresent()) {
			modelMapper.typeMap(Person.class, AddressPersonDTOPerson.class).addMappings(mapper -> {
				//mapper.map(Person::getAge, AddressPersonDTOPerson::setAge); //ModelMapper Handling Mismatches
				mapper.map(src -> src.getMedicalrecord().getMedications(), AddressPersonDTOPerson::setMedications);
				mapper.map(src -> src.getMedicalrecord().getAllergies(), AddressPersonDTOPerson::setAllergies);
			});
			List<AddressPersonDTO> addressPersons = addressService.findPersonsByAddress(address.get()).stream().map(person -> modelMapper.map(person, AddressPersonDTOPerson.class)).collect(Collectors.toList());
			addressPersons.add(new AddressPersonDTOStationNumbers(addressService.findFirestationssByAddress(address.get()).stream().map(firestation -> String.valueOf(firestation.getStationNumber())).collect(Collectors.toList())));
			fileWriter.writeToFile(objectMapper.valueToTree(addressPersons));			
			return addressPersons;
		}
		return null;
	}
	
	@GetMapping("/communityEmail")
	public List<AddressPersonEmailDTO> communityEmailCity(@RequestParam(name = "city") Optional<String> city) {
		/*Cette url doit retourner les adresses mail de tous les habitants de la ville
		 */
		if (city.isPresent()) {
			List<AddressPersonEmailDTO> firestationPersonPhones = addressService.findemailPersonsByCity(city.get()).stream().map(person -> modelMapper.map(person, AddressPersonEmailDTO.class)).distinct().collect(Collectors.toList());
			fileWriter.writeToFile(objectMapper.valueToTree(firestationPersonPhones));
			return firestationPersonPhones;
		}
		return null;
	}
	
	@GetMapping("/toto") 
	public ResponseEntity<String> getToto() {
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body("toto");
	}
}
