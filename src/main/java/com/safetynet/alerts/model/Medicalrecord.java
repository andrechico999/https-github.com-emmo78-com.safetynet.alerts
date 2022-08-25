package com.safetynet.alerts.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Medicalrecord {
	private LocalDate birthdate;
	private List<String> medications;
	private List<String> allergies;
	
	public Medicalrecord(LocalDate birthdate) {
		this.birthdate=birthdate;
		medications = new ArrayList<>();
		allergies = new ArrayList<>();
	}
}
