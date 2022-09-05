package com.safetynet.alerts.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddressPersonDTOStationNumbers implements AddressPersonDTO {
	List<String> stationNumbers;
}
