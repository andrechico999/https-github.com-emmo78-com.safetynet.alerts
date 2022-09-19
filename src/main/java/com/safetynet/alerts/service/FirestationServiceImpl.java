package com.safetynet.alerts.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.dto.FirestationDTO;
import com.safetynet.alerts.dto.FirestationPersonDTO;
import com.safetynet.alerts.dto.FirestationPersonPhoneDTO;
import com.safetynet.alerts.dto.FirestationsPersonDTO;
import com.safetynet.alerts.dto.service.FirestationDTOService;
import com.safetynet.alerts.model.Address;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.JsonRepository;
import com.safetynet.alerts.repository.JsonRepositoryImpl;

@Service
public class FirestationServiceImpl implements FirestationService {

	@Autowired
	private JsonRepository jsonNodeService;
	
	@Autowired
	private FirestationDTOService firestationDTOService;

	private Map<Integer, Firestation> firestations;
	private Map<String, Address> allAddressS;
	
	@PostConstruct
	public void firestationServiceImpl() {
		firestations = ((JsonRepositoryImpl) jsonNodeService).getFirestations();
		allAddressS = ((JsonRepositoryImpl) jsonNodeService).getAllAddressS();
	}
		
	@Override
	public List<FirestationPersonDTO> findPersonsByFirestation(String stationNum) {
		return firestationDTOService.firestationPersonsToDTO(findPersonsByStationNumber(Integer.parseInt(stationNum)));
	}
	
	@Override
	public List<FirestationPersonPhoneDTO> findPersonPhonesByFirestation (String stationNum) {
		return firestationDTOService.firestationPersonsToPhonesDTO(findPersonsByStationNumber(Integer.parseInt(stationNum)));
	}

	@Override
	public List<Person> findPersonsByStationNumber (int stationNumber) {
		return firestations.get(stationNumber).getAddressS().values().stream().flatMap(address -> address.getPersons().values().stream()).collect(Collectors.toList());
	}
	
	@Override
	public List<FirestationsPersonDTO> findAddressPersonsByFiresations(List<String> stationNumbers) {
		return firestationDTOService.firestationsAddressPersonsToDTO(stationNumbers.stream().map(stationNumber -> Integer.parseInt(stationNumber)).flatMap(stationNumber -> firestations.get(stationNumber).getAddressS().values().stream()).distinct().flatMap(address -> address.getPersons().values().stream()).collect(Collectors.toList()));
	}

	@Override
	public FirestationDTO addMappingAddressToFirestation(FirestationDTO firestationDTO) {
		Firestation firestation = firestationDTOService.convertFirestationFromDTO(firestationDTO);
		String addressAddress = firestation.getAddressS().values().stream().collect(Collectors.toList()).get(0).getAddress();
		
		Optional<Address> existingAddressOpt = Optional.ofNullable(allAddressS.get(addressAddress));
		Optional<Firestation> existingFirestationOpt = Optional.ofNullable(firestations.get(firestation.getStationNumber()));
		try {
			Address existingAddress = existingAddressOpt.orElseThrow(() -> new Exception("Address not existing"));
			Firestation existingFirestation = existingFirestationOpt.orElseThrow(() -> new Exception("No Station with this number"));
			if (!existingAddress.getFirestations().isEmpty()) {
				throw new Exception("Address has already a firestation");
			}
			existingAddress.putFirestation(existingFirestation); //firestation.attachAddress(this);
		} catch (Exception e) {
			// TODO Address not existing
			// TODO No Station with this Number
			// TODO Address has already a firestation
		}
		return firestationDTOService.convertFirestationToDTO(existingFirestationOpt.get(), addressAddress);
	}

	@Override
	public FirestationDTO updateMappingAddressToFirestation(FirestationDTO firestationDTO) {
		Firestation firestation = firestationDTOService.convertFirestationFromDTO(firestationDTO);
		String addressAddress = firestation.getAddressS().values().stream().collect(Collectors.toList()).get(0).getAddress();
		
		Optional<Address> existingAddressOpt = Optional.ofNullable(allAddressS.get(addressAddress));
		Optional<Firestation> existingFirestationOpt = Optional.ofNullable(firestations.get(firestation.getStationNumber()));
		try {
			Address existingAddress = existingAddressOpt.orElseThrow(() -> new Exception("Address not existing"));
			Firestation existingFirestation = existingFirestationOpt.orElseThrow(() -> new Exception("No Station with this number"));
			existingAddress.getFirestations().values().stream().forEach(firestationLocal -> firestationLocal.detachAddress(existingAddress));
			existingAddress.getFirestations().clear();
			existingAddress.putFirestation(existingFirestation); //firestation.attachAddress(this);
		} catch (Exception e) {
			// TODO Address not existing
			// TODO No Station with this Number

		}
		return firestationDTOService.convertFirestationToDTO(existingFirestationOpt.get(), addressAddress);
	}

	@Override
	public FirestationDTO deleteMappingAddressToFirestation(FirestationDTO firestationDTO) {
		Firestation firestation = firestationDTOService.convertFirestationFromDTO(firestationDTO);
		int stationNumber = firestation.getStationNumber();
		String stationAddressAddress = firestation.getAddressS().values().stream().collect(Collectors.toList()).get(0).getAddress();
		Optional<Firestation> existingFirestationOpt;
		Optional<Address> existingAddressOpt;
		try {
			if (stationAddressAddress == null) {
				existingFirestationOpt = Optional.ofNullable(firestations.get(stationNumber));
				Firestation existingFirestation = existingFirestationOpt.orElseThrow(() -> new Exception("No Station with this number"));
				existingFirestation.getAddressS().values().stream().forEach(address -> address.removeFirestation(existingFirestation));
				existingFirestation.getAddressS().clear();
			} else if (stationNumber == 0) {
				existingAddressOpt = Optional.ofNullable(allAddressS.get(stationAddressAddress));
				Address existingAddress = existingAddressOpt.orElseThrow(() -> new Exception("Address not existing"));
				existingAddress.getFirestations().values().stream().forEach(firestationLocal -> firestationLocal.detachAddress(existingAddress));
				existingAddress.getFirestations().clear();
			} else {
				existingAddressOpt = Optional.ofNullable(allAddressS.get(stationAddressAddress));
				Address existingAddress = existingAddressOpt.orElseThrow(() -> new Exception("Address not existing"));
				existingFirestationOpt = Optional.ofNullable(firestations.get(stationNumber));
				Firestation existingFirestation = existingFirestationOpt.orElseThrow(() -> new Exception("No Station with this number"));
				existingAddress.removeFirestation(existingFirestation);
				existingFirestation.detachAddress(existingAddress);
			}
		} catch (Exception e) {
			// TODO No Station with this Number
			// TODO Address not existing
		}
 		return firestationDTO;
	}
}
