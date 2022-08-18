package com.safetynet.alerts.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.Address;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.WriteToStringAndFile;

@Service
public class FindByFireStationImpl implements FindByFireStation {

	@Autowired
	private ConvertJsonToClass convJsToClass;
	
	@Autowired
	private SetMedicalrecordsForPersons setMedrecForP;
	
	@Autowired
	private WriteToStringAndFile jsonWritter;
	
	@Autowired
	private AppendToStringBuffer appendToStrBuf;
	
	private Map<String, Address> allAddressS;
	private Map<Integer, Firestation> firestations;
	private Map<String, Person> persons;
	
	@Override
	public List<String> findPersonsByFireStation(int stationNum) {
		List<String> listPersons = new ArrayList<>();
		int numAdult = 0; //Local variable length defined in an enclosing scope must be final or effectively final
		int numChildren = 0;
		allAddressS = new HashMap<>();
		firestations = convJsToClass.convertFireStations(allAddressS);
		persons = convJsToClass.convertPersons(allAddressS);
		setMedrecForP.setPersonsMedicalrecords(persons);
				
		Iterator<Address> itAddress = firestations.get(stationNum).getAddressS().values().iterator();
		while (itAddress.hasNext()) {
			Iterator<Person> itPerson =	itAddress.next().getPersons().values().iterator();
			while (itPerson.hasNext()) {
				Person person = itPerson.next();
				StringBuffer stringFieldsPerson = new StringBuffer();
				appendToStrBuf.appendFields(stringFieldsPerson, person, new ArrayList<Fields>(Arrays.asList(Fields.Id, Fields.Address, Fields.Phone)));
				listPersons.add(stringFieldsPerson.toString());
				if (person.getAge() > 18) {
					 numAdult++;
				} else {
					numChildren++;
				}
			}			
		}
		
		StringBuffer stats = new StringBuffer();
		stats.append("Adult = "+String.valueOf(numAdult));
		stats.append(", ");
		stats.append("Children = "+String.valueOf(numChildren));
		listPersons.add(stats.toString());

		jsonWritter.writeToFile(listPersons);
		allAddressS = null;
		firestations = null;
		persons = null;
		return listPersons;
	}

	@Override
	public List<String> findPhoneNumbersByFireStation(int stationNum) {
		allAddressS = new HashMap<>();
		firestations = convJsToClass.convertFireStations(allAddressS);
		persons = convJsToClass.convertPersons(allAddressS);
		
		Set<String> phonesSet = new HashSet<>();
		firestations.get(stationNum).getAddressS().values().forEach(address -> {
			address.getPersons().values().forEach(person -> {
				phonesSet.add(person.getPhone()); //"phone"
			});
		});
		List<String> listPhones = phonesSet.stream().collect(Collectors.toList());
		
		jsonWritter.writeToFile(listPhones);
		allAddressS = null;
		firestations = null;
		persons = null;		
		return listPhones;
	}

	@Override
	public List<String> findAddressPersonsByFiresations(List<Integer> stationNumbers) {
		List<String> listAddressPersonsFSs = new ArrayList<>();
		allAddressS = new HashMap<>();
		firestations = convJsToClass.convertFireStations(allAddressS);
		persons = convJsToClass.convertPersons(allAddressS);
		setMedrecForP.setPersonsMedicalrecords(persons);
				
		Map<String, Address> addressS = new HashMap<>();
		stationNumbers.forEach(stationNumber ->
			firestations.get(stationNumber).getAddressS().values().forEach(address ->
				addressS.put(address.getAddress(),address)));
		
		addressS.values().forEach(address -> {
			address.getPersons().values().forEach(person -> {
				StringBuffer stringFieldsPerson = new StringBuffer();
				appendToStrBuf.appendFields(stringFieldsPerson, person, new ArrayList<Fields>(Arrays.asList(Fields.Address, Fields.LastName, Fields.Phone, Fields.Age, Fields.LastName, Fields.Medicalrecords)));
				listAddressPersonsFSs.add(stringFieldsPerson.toString());
			});
		});
		
		jsonWritter.writeToFile(listAddressPersonsFSs);
		allAddressS = null;
		firestations = null;
		persons = null;		
		return listAddressPersonsFSs;
	}

}
