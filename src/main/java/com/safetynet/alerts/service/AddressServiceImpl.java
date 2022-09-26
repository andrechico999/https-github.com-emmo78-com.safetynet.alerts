package com.safetynet.alerts.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.NullNode;
import com.safetynet.alerts.dto.AddressAdultChildDTO;
import com.safetynet.alerts.dto.AddressPersonDTO;
import com.safetynet.alerts.dto.AddressPersonEmailDTO;
import com.safetynet.alerts.dto.service.AddressDTOService;
import com.safetynet.alerts.dto.service.AddressDTOServiceImpl;
import com.safetynet.alerts.exception.AddressNotFoundException;
import com.safetynet.alerts.model.Address;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.repository.JsonRepository;
import com.safetynet.alerts.repository.JsonRepositoryImpl;
import com.safetynet.alerts.repository.WriteToFile;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	private JsonRepository jsonNodeService;
	
    @Autowired
	private AddressDTOService addressDTOService;
    
	@Autowired
	private WriteToFile fileWriter;
	
	@Autowired
	private StringService dataProcService;
	
	private Map<String, Address> allAddressS;

	@PostConstruct
	public void addressServiceImpl () {
		allAddressS = ((JsonRepositoryImpl) jsonNodeService).getAllAddressS();
	}
	
	@Override
	public List<AddressAdultChildDTO> findChildrenByAddress(String address) throws AddressNotFoundException {
		return addressDTOService.addressChildrenToDTO(Optional.ofNullable(allAddressS.get(address)).orElseThrow(() -> {
			fileWriter.writeToFile(NullNode.instance);
			return new AddressNotFoundException("Address not found");})
				.getPersons().values().stream().filter(person -> person.getAge() <= 18).sorted((p1, p2) -> p1.getLastName().compareTo(p2.getLastName())).collect(Collectors.toList()));
	}

	@Override
	public List<AddressPersonDTO> findPersonsByAddress(String address) throws AddressNotFoundException {
		((AddressDTOServiceImpl) addressDTOService).setStationNumbers(findFirestationssByAddress(address).stream().map(firestation -> String.valueOf(firestation.getStationNumber())).collect(Collectors.toList()));
		return addressDTOService.addressPersonsToDTO(allAddressS.get(address).getPersons().values().stream().collect(Collectors.toList()));
	}

	@Override
	public List<AddressPersonEmailDTO> findemailPersonsByCity(String city) throws AddressNotFoundException {
		List<Address> addressCity = allAddressS.values().stream().filter(address -> address.getCity().equals(dataProcService.upperCasingFirstLetter(city))).collect(Collectors.toList());
 		if ( addressCity.size() == 0) {
 			fileWriter.writeToFile(NullNode.instance);
			throw new AddressNotFoundException("City not found");
		}
		return addressDTOService.addressPersonEmailToDTO(addressCity.stream().flatMap(address -> address.getPersons().values().stream()).collect(Collectors.toList()));
	}

	@Override
	public List<Firestation> findFirestationssByAddress(String address) throws AddressNotFoundException {
		return Optional.ofNullable(allAddressS.get(address)).orElseThrow(() -> {
			fileWriter.writeToFile(NullNode.instance);
			return new AddressNotFoundException("Address not found");})
				.getFirestations().values().stream().collect(Collectors.toList());
	}
}
