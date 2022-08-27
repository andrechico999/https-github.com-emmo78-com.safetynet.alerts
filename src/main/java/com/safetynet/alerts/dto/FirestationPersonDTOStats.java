package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FirestationPersonDTOStats implements FirestationPersonDTO{
	int adults;
	int children;
}
