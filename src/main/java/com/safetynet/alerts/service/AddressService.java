package com.safetynet.alerts.service;

import java.util.List;

import com.safetynet.alerts.dto.AddressAdultChildDTO;
import com.safetynet.alerts.dto.AddressPersonDTO;
import com.safetynet.alerts.dto.AddressPersonEmailDTO;
import com.safetynet.alerts.model.Firestation;

public interface AddressService {
	List<AddressAdultChildDTO> findChildrenByAddress(String address);
	List<AddressPersonDTO> findPersonsByAddress(String address);
	List<AddressPersonEmailDTO> findemailPersonsByCity(String city);
	List<Firestation> findFirestationssByAddress(String address);
}
