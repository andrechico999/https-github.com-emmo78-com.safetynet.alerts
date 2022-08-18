package com.safetynet.alerts.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.Person;

@Service
public class AppendToStringBufferImpl implements AppendToStringBuffer {

	@Override
	public StringBuffer appendFields(StringBuffer stringFieldsPerson, Person person, List<Fields> fields) {
		fields.forEach(field -> {
			switch (field) {
			case Id:
				stringFieldsPerson.append(person.getId());
				break;
			case LastName:
				stringFieldsPerson.append(person.getLastName());
				break;
			case Phone:
				stringFieldsPerson.append(person.getPhone());
				break;
			case Email:
				stringFieldsPerson.append(person.getEmail());
				break;
			case Age:
				int age = person.getAge();
				stringFieldsPerson.append(age);
				if (age>1) {
					stringFieldsPerson.append(" ans");
				} else {
					stringFieldsPerson.append(" an");
				}
				break;
			case Address:
				stringFieldsPerson.append(person.getAddress().getAddress());
				break;
			case Medicalrecords:
				stringFieldsPerson.append("medications : ");
				person.getMedicalrecord().getMedications().forEach(medication -> stringFieldsPerson.append(medication+", "));
				if (person.getMedicalrecord().getMedications().size() == 0 ) {
					stringFieldsPerson.append(", ");
				}
				stringFieldsPerson.append("allergies : ");
				person.getMedicalrecord().getAllergies().forEach(allergie -> stringFieldsPerson.append(allergie+", "));
				int length = stringFieldsPerson.length(); //Local variable length defined in an enclosing scope must be final or effectively final
				int index;
				if (person.getMedicalrecord().getAllergies().size() == 0) {
					index = length;
				} else {
					index = length-2;
				}
				stringFieldsPerson.delete(index, length);
				break;
			case stationNumber:
				stringFieldsPerson.append("station number : ");
				//person.getAddress().getFirestations()
				break;
			default:
				break;
			}
			stringFieldsPerson.append(", ");
		});
		int length = stringFieldsPerson.length();
		stringFieldsPerson.delete(length-2, length);
		return stringFieldsPerson;
	}
}
