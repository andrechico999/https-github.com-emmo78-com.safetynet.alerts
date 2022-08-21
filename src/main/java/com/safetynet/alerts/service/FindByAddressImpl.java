package com.safetynet.alerts.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.Address;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.WriteToFile;

@Service
public class FindByAddressImpl implements FindByAddress {

	@Autowired
	private ConvertJsonToClass convJsToClass;
	
	@Autowired
	private SetMedicalrecordsForPersons setMedrecForP;
	
	@Autowired
	private WriteToFile fileWritter;
	
	@Autowired
	private StringProcessing stringProc;
	
	@Autowired
	private SelectRemovePersonByField selectPByF;
	
	private Map<String, Address> allAddressS;
	private Map<String, Person> persons;

	@Override
	public List<String> findChildrenByAddress(String address) {
		List<String> listAddressChildren = new ArrayList<>();
		StringBuffer stringFieldsPerson;
		Map<String, Person> personsAddress;
		Map<String, Person> childrenAddress;
		List<Person> parentsOfChildren = new ArrayList<>();
		Optional<String> childNameOpt;
		String childName;
		allAddressS = new HashMap<>();
		convJsToClass.convertFireStations(allAddressS);
		persons = convJsToClass.convertPersons(allAddressS);
		setMedrecForP.setPersonsMedicalrecords(persons);
		
		personsAddress = allAddressS.get(address).getPersons();
		childrenAddress = selectPByF.selectRemovePersonsUnderEqualAge(18, personsAddress);
		while (childrenAddress.size() > 0) {
			childName = null;
			childNameOpt = Optional.ofNullable(childName);
			Iterator<Map.Entry<String, Person>> childrenAdrdressIterator = childrenAddress.entrySet().iterator();
			while (childrenAdrdressIterator.hasNext()) {
				Map.Entry<String, Person> entry = childrenAdrdressIterator.next();
				childName = entry.getValue().getLastName();
				if (childNameOpt.isEmpty()) {
					childNameOpt = Optional.of(childName);
					parentsOfChildren.addAll(selectPByF.selectRemovePersonsByName(childName, personsAddress).values());
					stringFieldsPerson = new StringBuffer();
					stringProc.appendFields(stringFieldsPerson, entry.getValue(), new ArrayList<Fields>(Arrays.asList(Fields.Id, Fields.Age)));
					listAddressChildren.add(stringFieldsPerson.toString());
					childrenAdrdressIterator.remove();
				} else if(childName.equals(childNameOpt.get())) {
					stringFieldsPerson = new StringBuffer();
					stringProc.appendFields(stringFieldsPerson, entry.getValue(), new ArrayList<Fields>(Arrays.asList(Fields.Id, Fields.Age)));
					listAddressChildren.add(stringFieldsPerson.toString());
					childrenAdrdressIterator.remove();
				}
			}
		}

		parentsOfChildren.forEach(person -> {
			StringBuffer stringFieldsPersonLambda = new StringBuffer();
			stringProc.appendFields(stringFieldsPersonLambda, person, new ArrayList<Fields>(Arrays.asList(Fields.Id, Fields.Age)));
			listAddressChildren.add(stringFieldsPersonLambda.toString());
		});

		fileWritter.writeToFile(listAddressChildren);
		allAddressS = null;
		persons = null;
		return listAddressChildren;
	}

	@Override
	public List<String> findPersonsByAddress(String address) {
		List<String> listAddressPersons = new ArrayList<>();
		allAddressS = new HashMap<>();
		convJsToClass.convertFireStations(allAddressS);
		persons = convJsToClass.convertPersons(allAddressS);
		setMedrecForP.setPersonsMedicalrecords(persons);
		
		allAddressS.get(address).getPersons().values().forEach(person -> {
			StringBuffer stringFieldsPerson = new StringBuffer();
			stringProc.appendFields(stringFieldsPerson, person, new ArrayList<Fields>(Arrays.asList(Fields.LastName, Fields.Phone, Fields.Age, Fields.Medicalrecords)));
			listAddressPersons.add(stringFieldsPerson.toString());
		});
		
		StringBuffer stationNumber = new StringBuffer();
		stationNumber.append("Station number(s) : ");
		allAddressS.get(address).getFirestations().values().forEach(firestation -> stationNumber.append(firestation.getStationNumber()+", "));
		int length = stationNumber.length();
		int index;
		if (allAddressS.get(address).getFirestations().size() == 0) {
			index = length;
		} else {
			index = length-2;
		}
		stationNumber.delete(index, length);
		listAddressPersons.add(stationNumber.toString());
		
		fileWritter.writeToFile(listAddressPersons);
		allAddressS = null;
		persons = null;
		return listAddressPersons;
	}

	@Override
	public List<String> findemailPersonsByCity(String city) {
		Set<String> emailsSet = new HashSet<>();
		allAddressS = new HashMap<>();
		convJsToClass.convertFireStations(allAddressS);
		persons = convJsToClass.convertPersons(allAddressS);
		
		// cityAddress.size = 0 if City not found
		List<Address> cityAddressS = allAddressS.values().stream().filter(address -> address.getCity().equals(stringProc.upperCasingFirstLetter(city))).collect(Collectors.toList());
		
		cityAddressS.forEach(address -> 
			address.getPersons().values().forEach(person ->
				emailsSet.add(person.getEmail())));
		List<String> listEmails = emailsSet.stream().collect(Collectors.toList());
		
		fileWritter.writeToFile(listEmails);
		allAddressS = null;
		persons = null;		
		return listEmails;
	}

}
