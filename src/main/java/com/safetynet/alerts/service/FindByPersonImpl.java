package com.safetynet.alerts.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.Address;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.WriteToFile;

@Service
public class FindByPersonImpl implements FindByPerson {

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
	public List<String> findPersonsByFirstNameAndLastName(String firstName, String lastName) {
		List<String> addressOrNamePersonsIdStr = new ArrayList<>();
		List<Person> addressOrNamePersonsId = new ArrayList<>();
		firstName = stringProc.upperCasingFirstLetter(firstName);
		lastName = stringProc.upperCasingFirstLetter(lastName);
		String id =firstName +" "+lastName;
		allAddressS = new HashMap<>();
		convJsToClass.convertFireStations(allAddressS);
		persons = convJsToClass.convertPersons(allAddressS);
		setMedrecForP.setPersonsMedicalrecords(persons);
		Person personId = persons.get(id);
		Map<String, Person> addressPersonsId = personId.getAddress().getPersons();

		addressOrNamePersonsId.add(personId);
		addressPersonsId.remove(id);
		persons.remove(id);
		selectPByF.selectRemovePersonsByName(lastName, addressPersonsId).forEach((idLambda, person)-> {
			addressOrNamePersonsId.add(person);
			persons.remove(idLambda);
		});
		addressOrNamePersonsId.addAll(addressPersonsId.values());
		addressOrNamePersonsId.addAll(selectPByF.selectRemovePersonsByName(lastName, persons).values());
		
		addressOrNamePersonsId.forEach(person -> {
			StringBuffer stringFieldsPerson = new StringBuffer();
			stringProc.appendFields(stringFieldsPerson, person, new ArrayList<Fields>(Arrays.asList(Fields.LastName, Fields.Address, Fields.Age, Fields.Email, Fields.Medicalrecords)));
			addressOrNamePersonsIdStr.add(stringFieldsPerson.toString());
		});
				
		fileWritter.writeToFile(addressOrNamePersonsIdStr);
		allAddressS = null;
		persons = null;
		return addressOrNamePersonsIdStr;
	}

}
