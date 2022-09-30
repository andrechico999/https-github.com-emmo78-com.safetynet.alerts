package com.safetynet.alerts.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode //for Assertions in UnitTests
public class FirestationDTO {
	private String address;
	private String station;
}
