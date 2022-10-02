package com.safetynet.alerts.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor //Needed for Unit Tests
@EqualsAndHashCode //Needed for Unit Tests
public class Medicalrecord {
    private String id;
 	private LocalDate birthdate;
	private List<String> medications;
	private List<String> allergies;
	
	public Medicalrecord() {
		medications = new ArrayList<>();
		allergies = new ArrayList<>();
	}
	
	public Medicalrecord(LocalDate birthdate) {
		this.birthdate=birthdate;
		medications = new ArrayList<>();
		allergies = new ArrayList<>();
	}
}
