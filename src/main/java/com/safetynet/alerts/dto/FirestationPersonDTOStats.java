package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class FirestationPersonDTOStats implements FirestationPersonDTO{
	int adults;
	int children;
}
