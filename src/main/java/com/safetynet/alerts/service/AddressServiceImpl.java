package com.safetynet.alerts.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.Address;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	private JsonNodeService jsNodeService;
	
	@Autowired
	private MedicalrecordPersonService medrecPService;
	
	@Autowired
	private DataProcessingService dataProcService;
	
	private Map<String, Address> allAddressS;
	private Map<String, Person> persons;

	@Override
	public List<Person> findChildrenByAddress(String address) {
		
		allAddressS = new HashMap<>();
		jsNodeService.convertFireStations(allAddressS);
		persons = jsNodeService.convertPersons(allAddressS);
		medrecPService.setPersonsMedicalrecords(persons);
		
		List<Person> addressPersonChildren = allAddressS.get(address).getPersons().values().stream().filter(person -> person.getAge() <= 18).sorted((p1, p2) -> p1.getLastName().compareTo(p2.getLastName())).collect(Collectors.toList());
		addressPersonChildren.addAll(allAddressS.get(address).getPersons().values().stream().filter(person -> addressPersonChildren.contains(person)&&(person.getAge() > 18)).sorted((p1, p2) -> p1.getLastName().compareTo(p2.getLastName())).collect(Collectors.toList()));
		return addressPersonChildren ;
	}

	@Override
	public List<Person> findPersonsByAddress(String address) {
		allAddressS = new HashMap<>();
		jsNodeService.convertFireStations(allAddressS);
		persons = jsNodeService.convertPersons(allAddressS);
		medrecPService.setPersonsMedicalrecords(persons);
		
		return allAddressS.get(address).getPersons().values().stream().collect(Collectors.toList());
	}

	@Override
	public List<Person> findemailPersonsByCity(String city) {
		allAddressS = new HashMap<>();
		jsNodeService.convertFireStations(allAddressS);
		persons = jsNodeService.convertPersons(allAddressS);

		return allAddressS.values().stream().filter(address -> address.getCity().equals(dataProcService.upperCasingFirstLetter(city))).flatMap(address -> address.getPersons().values().stream()).collect(Collectors.toList());
	}

	@Override
	public List<Firestation> findFirestationssByAddress(String address) {
		return allAddressS.get(address).getFirestations().values().stream().collect(Collectors.toList());
	}
}
