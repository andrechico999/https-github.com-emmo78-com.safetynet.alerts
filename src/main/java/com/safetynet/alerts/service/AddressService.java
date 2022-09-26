package com.safetynet.alerts.service;

import java.util.List;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import com.safetynet.alerts.dto.AddressAdultChildDTO;
import com.safetynet.alerts.dto.AddressPersonDTO;
import com.safetynet.alerts.dto.AddressPersonEmailDTO;
import com.safetynet.alerts.model.Firestation;

/**
 * GET (Read) by Address given
 * @author olivi
 *
 */
public interface AddressService {
	/**
	 * return list of children (age under 18) living at this address with adults living with
	 * @param address : address given
	 * @return list of children with adults living with
	 * @throws ResourceNotFoundException : "Address not found"
	 */
	List<AddressAdultChildDTO> findChildrenByAddress(String address) throws ResourceNotFoundException;
	
	/**
	 * return list of inhabitants living at the given address as well as the number(s) of the fire station serving it
	 * @param address : address given
	 * @return list of inhabitants and the station number(s)
	 * @throws ResourceNotFoundException : "Address not found"
	 */
	List<AddressPersonDTO> findPersonsByAddress(String address) throws ResourceNotFoundException;
	
	/**
	 * return list of email addresses of all the inhabitants of the city - no duplicate  
	 * @param city : city given
	 * @return list of email
	 * @throws ResourceNotFoundException : "City not found"
	 */
	List<AddressPersonEmailDTO> findemailPersonsByCity(String city) throws ResourceNotFoundException;
	
	/**
	 * return list of firestations serving the given address  
	 * @param address : address given
	 * @return list of firestations
	 * @throws ResourceNotFoundException : "Address not found"
	 */
	List<Firestation> findFirestationssByAddress(String address) throws ResourceNotFoundException;
}
