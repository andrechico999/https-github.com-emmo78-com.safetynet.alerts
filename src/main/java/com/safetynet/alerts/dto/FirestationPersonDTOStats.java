package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FirestationPersonDTOStats implements FirestationPersonDTO{
	int adults;
	int children;
}
