package com.safetynet.alerts.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode //for Assertions in UnitTests
@NoArgsConstructor // Needed for Unit Test
@AllArgsConstructor // Needed for Unit Test
public class MedicalrecordDTO {
	private String firstName;
	private String lastName;
    private String birthdate;
	@EqualsAndHashCode.Exclude
    private List<String> medications;
	@EqualsAndHashCode.Exclude
    private List<String> allergies;
}
