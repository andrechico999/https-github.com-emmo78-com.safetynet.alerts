package com.safetynet.alerts.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.Address;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.JsonRepository;
import com.safetynet.alerts.repository.JsonRepositoryImpl;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	private JsonRepository jsonNodeService;
	
	@Autowired
	private StringService dataProcService;
	
	private Map<String, Address> allAddressS;

	@PostConstruct
	public void addressServiceImpl () {
		allAddressS = ((JsonRepositoryImpl) jsonNodeService).getAllAddressS();
	}
	
	@Override
	public List<Person> findChildrenByAddress(String address) {
		return allAddressS.get(address).getPersons().values().stream().filter(person -> person.getAge() <= 18).sorted((p1, p2) -> p1.getLastName().compareTo(p2.getLastName())).collect(Collectors.toList());
	}

	@Override
	public List<Person> findPersonsByAddress(String address) {
		return allAddressS.get(address).getPersons().values().stream().collect(Collectors.toList());
	}

	@Override
	public List<Person> findemailPersonsByCity(String city) {
		return allAddressS.values().stream().filter(address -> address.getCity().equals(dataProcService.upperCasingFirstLetter(city))).flatMap(address -> address.getPersons().values().stream()).collect(Collectors.toList());
	}

	@Override
	public List<Firestation> findFirestationssByAddress(String address) {
		return allAddressS.get(address).getFirestations().values().stream().collect(Collectors.toList());
	}
}
