package com.safetynet.alerts.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.Address;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.WriteToStringAndFile;

@Service
public class FindByAddressImpl implements FindByAddress {

	@Autowired
	private ConvertJsonToClass convJsToClass;
	
	@Autowired
	private SetMedicalrecordsForPersons setMedrecForP;
	
	@Autowired
	private WriteToStringAndFile jsonWritter;
	
	@Autowired
	private AppendToStringBuffer appendToStrBuf;
	
	@Autowired
	private SelectRemovePersonByField selectPByF;
	
	private Map<String, Address> allAddressS;
	private Map<Integer, Firestation> firestations;
	private Map<String, Person> persons;

	@Override
	public List<String> findChildrenByAddress(String address) {
		List<String> listAddressChildren = new ArrayList<>();
		Map<String, Person> personsAddress;
		Map<String, Person> childrenAddress;
		List<Person> parentsOfChildren = new ArrayList<>();
		Optional<String> childNameOpt;
		String childName;
		StringBuffer stringFieldsPerson;
		allAddressS = new HashMap<>();
		firestations = convJsToClass.convertFireStations(allAddressS);
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
					parentsOfChildren.addAll(selectPByF.selectRemovePersonsByName(childName, personsAddress).values());
					stringFieldsPerson = new StringBuffer();
					appendToStrBuf.appendFields(stringFieldsPerson, entry.getValue(), new ArrayList<Fields>(Arrays.asList(Fields.Id, Fields.Age)));
					listAddressChildren.add(stringFieldsPerson.toString());
					childNameOpt = Optional.of(childName);
					childrenAdrdressIterator.remove();
				} else if(childName.equals(childNameOpt.get())) {
					stringFieldsPerson = new StringBuffer();
					appendToStrBuf.appendFields(stringFieldsPerson, entry.getValue(), new ArrayList<Fields>(Arrays.asList(Fields.Id, Fields.Age)));
					listAddressChildren.add(stringFieldsPerson.toString());
					childrenAdrdressIterator.remove();
				}
			}
		}
		parentsOfChildren.forEach(person -> {
			StringBuffer stringFieldsPersonLambda = new StringBuffer();
			appendToStrBuf.appendFields(stringFieldsPersonLambda, person, new ArrayList<Fields>(Arrays.asList(Fields.Id, Fields.Age)));
			listAddressChildren.add(stringFieldsPersonLambda.toString());

			
		});
		jsonWritter.writeToFile(listAddressChildren);
		allAddressS = null;
		firestations = null;
		persons = null;
		return listAddressChildren;
	}

	@Override
	public List<String> findPersonsByAddress(String address) {
		List<String> listAddressPersons = new ArrayList<>();
		allAddressS = new HashMap<>();
		firestations = convJsToClass.convertFireStations(allAddressS);
		persons = convJsToClass.convertPersons(allAddressS);
		setMedrecForP.setPersonsMedicalrecords(persons);
		
		jsonWritter.writeToFile(listAddressPersons);
		allAddressS = null;
		firestations = null;
		persons = null;
		return null;
	}

	@Override
	public List<String> findemailPersonsByCity(String city) {
		// TODO Auto-generated method stub
		return null;
	}

}
