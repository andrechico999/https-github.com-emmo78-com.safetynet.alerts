package com.safetynet.alerts.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.NullNode;
import com.safetynet.alerts.dto.FirestationDTO;
import com.safetynet.alerts.dto.FirestationPersonDTO;
import com.safetynet.alerts.dto.FirestationPersonPhoneDTO;
import com.safetynet.alerts.dto.FirestationsPersonDTO;
import com.safetynet.alerts.dto.service.FirestationDTOService;
import com.safetynet.alerts.exception.BadRequestException;
import com.safetynet.alerts.model.Address;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.JsonRepository;
import com.safetynet.alerts.repository.JsonRepositoryImpl;
import com.safetynet.alerts.repository.WriteToFile;

@Service
public class FirestationServiceImpl implements FirestationService {

	@Autowired
	private JsonRepository jsonNodeService;
	
	@Autowired
	private FirestationDTOService firestationDTOService;
	
	@Autowired
	private WriteToFile fileWriter;

	private Map<Integer, Firestation> firestations;
	private Map<String, Address> allAddressS;
	
	@PostConstruct
	public void firestationServiceImpl() {
		firestations = ((JsonRepositoryImpl) jsonNodeService).getFirestations();
		allAddressS = ((JsonRepositoryImpl) jsonNodeService).getAllAddressS();
	}
		
	@Override
	public List<FirestationPersonDTO> findPersonsByFirestation(String stationNum) throws BadRequestException, ResourceNotFoundException {
		int stationNumber = 0;
		try {
			stationNumber = Integer.parseInt(stationNum);
		} catch (NumberFormatException ex) {
			throw new BadRequestException("Correct request is to specify an integer for the station number");
		}
		return firestationDTOService.firestationPersonsToDTO(findPersonsByStationNumber(stationNumber));
	}
	
	@Override
	public List<FirestationPersonPhoneDTO> findPersonPhonesByFirestation (String stationNum) throws BadRequestException, ResourceNotFoundException {
		int stationNumber = 0;
		try {
			stationNumber = Integer.parseInt(stationNum);
		} catch (NumberFormatException ex) {
			throw new BadRequestException("Correct request is to specify an integer for the station number");
		}
		return firestationDTOService.firestationPersonsToPhonesDTO(findPersonsByStationNumber(stationNumber));
	}

	@Override
	public List<Person> findPersonsByStationNumber (int stationNumber) throws ResourceNotFoundException {
		return Optional.ofNullable(firestations.get(stationNumber)).orElseThrow(() -> {
			fileWriter.writeToFile(NullNode.instance);
			return new ResourceNotFoundException("No fire station found");})
				.getAddressS().values().stream().flatMap(address -> address.getPersons().values().stream()).collect(Collectors.toList());
	}
	
	@Override
	public List<FirestationsPersonDTO> findAddressPersonsByFiresations(List<String> stationNums) throws BadRequestException, ResourceNotFoundException {
		List<Integer> stationNumbers = null;
		try {
			stationNumbers = stationNums.stream().map(stationNum -> Integer.parseInt(stationNum)).filter(stationNumber -> Optional.ofNullable(firestations.get(stationNumber)).isPresent()).collect(Collectors.toList());
		} catch (NumberFormatException ex) {
			throw new BadRequestException("Correct request is to specify a list of integer for the station numbers");
		}
		if (stationNumbers.size() == 0) {
			fileWriter.writeToFile(NullNode.instance);
			throw new ResourceNotFoundException("No fire station found");
		}
		return firestationDTOService.firestationsAddressPersonsToDTO(stationNumbers.stream().flatMap(stationNumber -> firestations.get(stationNumber).getAddressS().values().stream()).distinct().flatMap(address -> address.getPersons().values().stream()).collect(Collectors.toList()));
	}

	@Override
	public FirestationDTO addMappingAddressToFirestation(FirestationDTO firestationDTO) throws ResourceNotFoundException {
		Firestation firestation = firestationDTOService.convertFirestationFromDTO(firestationDTO);
		String addressAddress = firestation.getAddressS().values().stream().collect(Collectors.toList()).get(0).getAddress();
		
		Optional<Address> existingAddressOpt = Optional.ofNullable(allAddressS.get(addressAddress));
		Optional<Firestation> existingFirestationOpt = Optional.ofNullable(firestations.get(firestation.getStationNumber()));
		
		Address existingAddress = existingAddressOpt.orElseThrow(() -> new ResourceNotFoundException("Non-existent address"));
		Firestation existingFirestation = existingFirestationOpt.orElseThrow(() -> new ResourceNotFoundException("No fire station with this number"));
		if (!existingAddress.getFirestations().isEmpty()) {
			throw new ResourceNotFoundException("Address has already a firestation");
		}
		existingAddress.putFirestation(existingFirestation); //firestation.attachAddress(this);
		
		return firestationDTOService.convertFirestationToDTO(existingFirestationOpt.get(), addressAddress);
	}

	@Override
	public FirestationDTO updateMappingAddressToFirestation(FirestationDTO firestationDTO) throws ResourceNotFoundException {
		Firestation firestation = firestationDTOService.convertFirestationFromDTO(firestationDTO);
		String addressAddress = firestation.getAddressS().values().stream().collect(Collectors.toList()).get(0).getAddress();
		
		Optional<Address> existingAddressOpt = Optional.ofNullable(allAddressS.get(addressAddress));
		Optional<Firestation> existingFirestationOpt = Optional.ofNullable(firestations.get(firestation.getStationNumber()));
		
		Address existingAddress = existingAddressOpt.orElseThrow(() -> new ResourceNotFoundException("Non-existent address"));
		Firestation existingFirestation = existingFirestationOpt.orElseThrow(() -> new ResourceNotFoundException("No Station with this number"));
		existingAddress.getFirestations().values().stream().forEach(firestationLocal -> firestationLocal.detachAddress(existingAddress));
		existingAddress.getFirestations().clear();
		existingAddress.putFirestation(existingFirestation); //firestation.attachAddress(this);
	
		return firestationDTOService.convertFirestationToDTO(existingFirestationOpt.get(), addressAddress);
	}

	@Override
	public FirestationDTO deleteMappingAddressToFirestation(FirestationDTO firestationDTO) throws ResourceNotFoundException {
		Firestation firestation = firestationDTOService.convertFirestationFromDTO(firestationDTO);
		int stationNumber = firestation.getStationNumber();
		String stationAddressAddress = firestation.getAddressS().values().stream().collect(Collectors.toList()).get(0).getAddress();
		Optional<Firestation> existingFirestationOpt;
		Optional<Address> existingAddressOpt;
		
		if (stationAddressAddress == null) {
			existingFirestationOpt = Optional.ofNullable(firestations.get(stationNumber));
			Firestation existingFirestation = existingFirestationOpt.orElseThrow(() -> new ResourceNotFoundException("No Station with this number"));
			existingFirestation.getAddressS().values().stream().forEach(address -> address.removeFirestation(existingFirestation));
			existingFirestation.getAddressS().clear();
		} else if (stationNumber == 0) {
			existingAddressOpt = Optional.ofNullable(allAddressS.get(stationAddressAddress));
			Address existingAddress = existingAddressOpt.orElseThrow(() -> new ResourceNotFoundException("Non-existent address"));
			existingAddress.getFirestations().values().stream().forEach(firestationLocal -> firestationLocal.detachAddress(existingAddress));
			existingAddress.getFirestations().clear();
		} else {
			existingAddressOpt = Optional.ofNullable(allAddressS.get(stationAddressAddress));
			Address existingAddress = existingAddressOpt.orElseThrow(() -> new ResourceNotFoundException("Non-existent address"));
			existingFirestationOpt = Optional.ofNullable(firestations.get(stationNumber));
			Firestation existingFirestation = existingFirestationOpt.orElseThrow(() -> new ResourceNotFoundException("No Station with this number"));
			existingAddress.removeFirestation(existingFirestation);
			existingFirestation.detachAddress(existingAddress);
		}
		return firestationDTO;
	}
}
