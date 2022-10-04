package com.safetynet.alerts.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.safetynet.alerts.dto.FirestationAddressPersonsDTO;
import com.safetynet.alerts.dto.FirestationDTO;
import com.safetynet.alerts.dto.FirestationPersonDTO;
import com.safetynet.alerts.dto.FirestationPersonPhoneDTO;
import com.safetynet.alerts.dto.service.FirestationDTOService;
import com.safetynet.alerts.exception.BadRequestException;
import com.safetynet.alerts.exception.ResourceConflictException;
import com.safetynet.alerts.model.Address;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.JsonRepository;
import com.safetynet.alerts.repository.JsonRepositoryImpl;
import com.safetynet.alerts.repository.WriteToFile;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FirestationServiceImpl implements FirestationService {
	
	@Autowired
	private JsonRepository jsonRepository;
	
	@Autowired
	private FirestationDTOService firestationDTOService;
	
    @Autowired
    private ObjectMapper objectMapper;
	
	@Autowired
	private WriteToFile fileWriter;
	
	@Autowired
	private RequestService requestService;

	@Setter
	private Map<Integer, Firestation> firestations;

	@Setter
	private Map<String, Address> allAddressS;

	// for filter(distinctByKey(Address::getAddress))
	private static Set<Object> seen;
	
	@PostConstruct
	public void firestationServiceImpl() {
		firestations = ((JsonRepositoryImpl) jsonRepository).getFirestations();
		allAddressS = ((JsonRepositoryImpl) jsonRepository).getAllAddressS();
	}
		
	@Override
	public List<FirestationPersonDTO> findPersonsByFirestation(String stationNum, WebRequest request) throws BadRequestException, ResourceNotFoundException {
		int stationNumber = 0;
		try {
			stationNumber = Integer.parseInt(stationNum);
		} catch (NumberFormatException ex) {
			throw new BadRequestException("Correct request is to specify an integer for the station number");
		}
		List<FirestationPersonDTO> firestationPersonsDTO = firestationDTOService.firestationPersonsToDTO(findPersonsByStationNumber(stationNumber));
		log.info("{} : found {} person(s) covered by the fire station", requestService.requestToString(request), firestationPersonsDTO.size()-1);
		fileWriter.writeToFile(objectMapper.valueToTree(firestationPersonsDTO));
		return firestationPersonsDTO;
	}
	
	@Override
	public List<FirestationPersonPhoneDTO> findPersonPhonesByFirestation(String stationNum, WebRequest request) throws BadRequestException, ResourceNotFoundException {
		int stationNumber = 0;
		try {
			stationNumber = Integer.parseInt(stationNum);
		} catch (NumberFormatException ex) {
			throw new BadRequestException("Correct request is to specify an integer for the station number");
		}
		List<FirestationPersonPhoneDTO> firestationPersonPhonesDTO = firestationDTOService.firestationPersonsToPhonesDTO(findPersonsByStationNumber(stationNumber));
		log.info("{} : found {} phone numbers served by the fire station", requestService.requestToString(request), firestationPersonPhonesDTO.size());
		fileWriter.writeToFile(objectMapper.valueToTree(firestationPersonPhonesDTO));
		return firestationPersonPhonesDTO;
	}

	@Override
	public List<Person> findPersonsByStationNumber (int stationNumber) throws ResourceNotFoundException {
		return Optional.ofNullable(firestations.get(stationNumber)).orElseThrow(() -> {
			fileWriter.writeToFile(NullNode.instance);
			return new ResourceNotFoundException("No fire station found");})
				.getAddressS().values().stream().flatMap(address -> address.getPersons().values().stream()).collect(Collectors.toList());
	}
	
	@Override
	public List<FirestationAddressPersonsDTO> findAddressPersonsByFiresations(List<String> stationNums, WebRequest request) throws BadRequestException, ResourceNotFoundException {
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
		seen = new HashSet<>();
		List<FirestationAddressPersonsDTO> firestationsAddressPersonsDTO = firestationDTOService.firestationsAddressToDTO(stationNumbers.stream().flatMap(stationNumber -> firestations.get(stationNumber).getAddressS().values().stream()).filter(distinctByKey(Address::getAddress)).collect(Collectors.toList()));
		log.info("{} : found {} household(s) served by the fire station", requestService.requestToString(request), firestationsAddressPersonsDTO.size());
		fileWriter.writeToFile(objectMapper.valueToTree(firestationsAddressPersonsDTO));
		seen = null;
		return firestationsAddressPersonsDTO;
	}

	private <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) 
	{
	    return t -> seen.add(keyExtractor.apply(t));
	}
	
	@Override
	public FirestationDTO addMappingAddressToFirestation(FirestationDTO firestationDTO, WebRequest request) throws ResourceNotFoundException, ResourceConflictException {
		Firestation firestation = firestationDTOService.convertFirestationFromDTO(firestationDTO);
		int stationNumber = firestation.getStationNumber();
		String addressAddress = firestation.getAddressS().values().stream().collect(Collectors.toList()).get(0).getAddress();
		
		Optional<Address> existingAddressOpt = Optional.ofNullable(allAddressS.get(addressAddress));
		Optional<Firestation> existingFirestationOpt = Optional.ofNullable(firestations.get(stationNumber));
		
		Address existingAddress = existingAddressOpt.orElseThrow(() -> new ResourceNotFoundException("Non-existent address : "+addressAddress));
		Firestation existingFirestation = existingFirestationOpt.orElseThrow(() -> new ResourceNotFoundException("No fire station with this number : "+stationNumber));
		if (!existingAddress.getFirestations().isEmpty()) {
			throw new ResourceConflictException("Address : "+addressAddress+" has already a firestation");
		}
		existingAddress.putFirestation(existingFirestation); //firestation.attachAddress(this);
		log.info("{} : add mapping address {} to fire station {} with succes", requestService.requestToString(request), existingAddress.getAddress(), existingFirestation.getStationNumber());
		return firestationDTOService.convertFirestationToDTO(existingFirestationOpt.get(), addressAddress);
	}

	@Override
	public FirestationDTO updateMappingAddressToFirestation(FirestationDTO firestationDTO, WebRequest request) throws ResourceNotFoundException {
		Firestation firestation = firestationDTOService.convertFirestationFromDTO(firestationDTO);
		int stationNumber = firestation.getStationNumber();
		String addressAddress = firestation.getAddressS().values().stream().collect(Collectors.toList()).get(0).getAddress();
		
		Optional<Address> existingAddressOpt = Optional.ofNullable(allAddressS.get(addressAddress));
		Optional<Firestation> existingFirestationOpt = Optional.ofNullable(firestations.get(stationNumber));
		
		Address existingAddress = existingAddressOpt.orElseThrow(() -> new ResourceNotFoundException("Non-existent address : "+addressAddress));
		Firestation existingFirestation = existingFirestationOpt.orElseThrow(() -> new ResourceNotFoundException("No fire station with this number : "+stationNumber));
		existingAddress.getFirestations().values().stream().forEach(firestationLocal -> firestationLocal.detachAddress(existingAddress));
		existingAddress.getFirestations().clear();
		existingAddress.putFirestation(existingFirestation); //firestation.attachAddress(this);
		log.info("{} : update mapping address {} to fire station {} with succes", requestService.requestToString(request), existingAddress.getAddress(), existingFirestation.getStationNumber());
		return firestationDTOService.convertFirestationToDTO(existingFirestationOpt.get(), addressAddress);
	}

	@Override
	public FirestationDTO deleteMappingAddressToFirestation(FirestationDTO firestationDTO, WebRequest request) throws ResourceNotFoundException {
		Firestation firestation = firestationDTOService.convertFirestationFromDTO(firestationDTO);
		int stationNumber = firestation.getStationNumber();
		String stationAddressAddress = firestation.getAddressS().values().stream().collect(Collectors.toList()).get(0).getAddress();
		Optional<Firestation> existingFirestationOpt;
		Optional<Address> existingAddressOpt;
		
		if (stationAddressAddress == null) {
			existingFirestationOpt = Optional.ofNullable(firestations.get(stationNumber));
			Firestation existingFirestation = existingFirestationOpt.orElseThrow(() -> new ResourceNotFoundException("No Station with this number : "+stationNumber));
			if (existingFirestation.getAddressS().size() == 0) {
				throw new ResourceNotFoundException("Non-existing mapping to this fire station : "+stationNumber);
			}
			existingFirestation.getAddressS().values().stream().forEach(address -> address.removeFirestation(existingFirestation));
			existingFirestation.getAddressS().clear();
			log.info("{} : delete mapping to fire station {} with succes", requestService.requestToString(request), stationNumber);
		} else if (stationNumber == 0) {
			existingAddressOpt = Optional.ofNullable(allAddressS.get(stationAddressAddress));
			Address existingAddress = existingAddressOpt.orElseThrow(() -> new ResourceNotFoundException("Non-existent address : "+stationAddressAddress));
			if (existingAddress.getFirestations().size() == 0) {
				throw new ResourceNotFoundException("Non-existing mapping from this address : "+stationAddressAddress);
			}
			existingAddress.getFirestations().values().stream().forEach(firestationLocal -> firestationLocal.detachAddress(existingAddress));
			existingAddress.getFirestations().clear();
			existingFirestationOpt = Optional.of(firestation);
			log.info("{} : delete mapping address {} to fire station with succes", requestService.requestToString(request), stationAddressAddress);
		} else {
			existingAddressOpt = Optional.ofNullable(allAddressS.get(stationAddressAddress));
			Address existingAddress = existingAddressOpt.orElseThrow(() -> new ResourceNotFoundException("Non-existent address : "+stationAddressAddress));
			existingFirestationOpt = Optional.ofNullable(firestations.get(stationNumber));
			Firestation existingFirestation = existingFirestationOpt.orElseThrow(() -> new ResourceNotFoundException("No Station with this number : "+stationNumber));
			if (existingAddress.removeFirestation(existingFirestation) == null) {
				throw new ResourceNotFoundException("Non-existing mapping address "+stationAddressAddress+" to fire station "+stationNumber);
			}
			existingFirestation.detachAddress(existingAddress);
			existingFirestationOpt = Optional.of(new Firestation());
			log.info("{} : delete mapping address {} to fire station {} with succes", requestService.requestToString(request), stationAddressAddress, stationNumber);
		}
		return firestationDTOService.convertFirestationToDTO(existingFirestationOpt.get(), stationAddressAddress);
	}
}
