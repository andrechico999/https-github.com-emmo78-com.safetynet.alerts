package com.safetynet.alerts.service;

import java.util.List;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.context.request.WebRequest;

import com.safetynet.alerts.dto.AddressAdultChildDTO;
import com.safetynet.alerts.dto.AddressPersonDTO;
import com.safetynet.alerts.dto.AddressPersonEmailDTO;
import com.safetynet.alerts.model.Firestation;

/**
 * GET (Read) by Address given
 * @author Olivier MOREL
 *
 */
public interface AddressService {
	/**
	 * return list of children (age under 18) living at this address with adults living with
	 * @param address : address given
	 * @param request : WebRequest parameters
	 * @return list of children with adults living with
	 * @throws ResourceNotFoundException : "Address not found"	 *
	 */
	List<AddressAdultChildDTO> findChildrenByAddress(String address, WebRequest request) throws ResourceNotFoundException;
	
	/**
	 * return list of inhabitants living at the given address as well as the number(s) of the fire station serving it
	 * @param address : address given
	 * @param request : WebRequest parameters
	 * @return list of inhabitants and the station number(s)
	 * @throws ResourceNotFoundException : "Address not found"
	 */
	List<AddressPersonDTO> findPersonsByAddress(String address, WebRequest request) throws ResourceNotFoundException;
	
	/**
	 * return list of email addresses of all the inhabitants of the city - no duplicate  
	 * @param city : city given
	 * @param request : WebRequest parameters
	 * @return list of email
	 * @throws ResourceNotFoundException : "City not found"
	 */
	List<AddressPersonEmailDTO> findemailPersonsByCity(String city, WebRequest request) throws ResourceNotFoundException;
	
	/**
	 * return list of firestations serving the given address  
	 * @param address : address given
	 * @return list of firestations
	 * @throws ResourceNotFoundException : "Address not found"
	 */
	List<Firestation> findFirestationssByAddress(String address) throws ResourceNotFoundException;
}
