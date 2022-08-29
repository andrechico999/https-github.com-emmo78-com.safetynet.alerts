package com.safetynet.alerts.dto;

import com.safetynet.alerts.model.Person;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FirestationPersonDTOPerson implements FirestationPersonDTO {
	@Getter @Setter private static int numAdult;
	@Getter @Setter private static int numChild;
	
	private String firstName;
	private String lastName;
	private String address;
	private String phone;
	
	public void sumAdultAndChild(Person person) {
		if (person.getAge() > 18) {
			numAdult++;
		} else {
			numChild++;
		}
	} 
}
