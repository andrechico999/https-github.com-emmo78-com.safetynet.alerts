package com.safetynet.alerts.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressChildDTO extends AddressAdultChildDTO {
	private List<AddressAdultChildDTO> adults;
}
