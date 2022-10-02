package com.safetynet.alerts.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.safetynet.alerts.dto.MedicalrecordDTO;
import com.safetynet.alerts.dto.service.MedicalrecordDTOService;
import com.safetynet.alerts.exception.ResourceConflictException;
import com.safetynet.alerts.model.Medicalrecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.JsonRepository;
import com.safetynet.alerts.repository.JsonRepositoryImpl;

@ExtendWith(MockitoExtension.class)
public class MedicalrecordServiceTest {

	@InjectMocks
	MedicalrecordServiceImpl medicalrecordService;

	@Mock
	private JsonRepositoryImpl jsonRepository;

	@Mock
	private MedicalrecordDTOService medicalrecordDTOService;

	@Mock
	private RequestService requestService;

	private Map<String, Medicalrecord> medicalrecordsTest;
	private MockHttpServletRequest requestMock;
	private WebRequest request;

	private static ModelMapper modelMapper;

	@BeforeAll
	public static void setUpForAllTests() {
		modelMapper = new ModelMapper();
		Converter<String, LocalDate> stringToLocalDate = new AbstractConverter<String, LocalDate>() {
			@Override
			protected LocalDate convert(String stringDate) {
				return LocalDate.parse(stringDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
			}
		};
		modelMapper.typeMap(MedicalrecordDTO.class, Medicalrecord.class).addMappings(mapper -> mapper
				.using(stringToLocalDate).map(MedicalrecordDTO::getBirthdate, Medicalrecord::setBirthdate));

		Converter<LocalDate, String> localDateToString = new AbstractConverter<LocalDate, String>() {
			@Override
			protected String convert(LocalDate date) {
				String dateStr;
				if (date == null) {
					dateStr = null;
				} else {
					dateStr = date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
				}
				return dateStr;
			}
		};
		modelMapper.typeMap(Medicalrecord.class, MedicalrecordDTO.class).addMappings(mapper -> mapper
				.using(localDateToString).map(Medicalrecord::getBirthdate, MedicalrecordDTO::setBirthdate));
	}
	
	@AfterAll
	public static void unSetForAllTests() {
		modelMapper = null;
	}
	
	@BeforeEach
	public void setUpForEachTests() {
		Medicalrecord medicalrecord1 = new Medicalrecord("FirstName1 LastName1", LocalDate.parse("01/01/2001", DateTimeFormatter.ofPattern("MM/dd/yyyy")), Arrays.asList("a1nol:110mg", "b1nol:120mg"), Arrays.asList());
		Medicalrecord medicalrecord2 = new Medicalrecord("FirstName2 LastName2", LocalDate.parse("02/02/2002", DateTimeFormatter.ofPattern("MM/dd/yyyy")), Arrays.asList("a2nol:210mg", "b2nol:220mg"), Arrays.asList("fish"));
		
		 medicalrecordsTest = new HashMap<>();
		 medicalrecordsTest.put(medicalrecord1.getId(), medicalrecord1);
		 medicalrecordsTest.put(medicalrecord2.getId(), medicalrecord2);
		 medicalrecordService.setMedicalrecords(medicalrecordsTest);
		 
		requestMock = new MockHttpServletRequest();
		requestMock.setServerName("http://localhost:8080");
	}

	@AfterEach
	public void unSetForEachTests() {
		medicalrecordsTest = null;
		medicalrecordService.setMedicalrecords(medicalrecordsTest);
		requestMock = null;
		request = null;
	}

	@Nested
	@Tag("POST Medicalrecord Tests")
	@DisplayName("createMedicalrecord tests")
	public class CreateMedicalrecordTestClass {
		
		@Test
		@Tag("Nominal case")
		@DisplayName("createMedicalrecordTest should put the medicalrecord in medicalrecords IoC and return it")
		public void createMedicalrecordTestShouldPutMedicalrecordInIoCAndReturnIt() {
			//GIVEN
			requestMock.setRequestURI("/medicalRecord");
			requestMock.setMethod("POST");
			request = new ServletWebRequest(requestMock);
			MedicalrecordDTO medicalrecordDTOExpected = new MedicalrecordDTO("FirstName3", "LastName3", "03/03/2003", Arrays.asList("a3nol:310mg", "b3nol:320mg"), Arrays.asList("peanut"));
			Medicalrecord medicalrecordExpected = modelMapper.map(medicalrecordDTOExpected, Medicalrecord.class);
			medicalrecordExpected.setId(medicalrecordDTOExpected.getFirstName()+" "+medicalrecordDTOExpected.getLastName());
			when(medicalrecordDTOService.convertMedicalrecordFromDTO(any(MedicalrecordDTO.class))).then(invocation -> {
				MedicalrecordDTO medicalrecordDTO = invocation.getArgument(0, MedicalrecordDTO.class);
				Medicalrecord medicalrecord = modelMapper.map(medicalrecordDTO, Medicalrecord.class);
				medicalrecord.setId(medicalrecordDTO.getFirstName()+" "+medicalrecordDTO.getLastName());
				return medicalrecord;
			});
			when(medicalrecordDTOService.convertMedicalrecordToDTO(any(Medicalrecord.class))).then(invocation -> {
				Medicalrecord medicalrecord = invocation.getArgument(0, Medicalrecord.class);
				MedicalrecordDTO medicalrecordDTO = modelMapper.map(medicalrecord, MedicalrecordDTO.class);
				String[] names = medicalrecord.getId().split(" ");
				medicalrecordDTO.setFirstName(names[0]);
				medicalrecordDTO.setLastName(names[1]);
				return medicalrecordDTO;
				});
			//WHEN
			MedicalrecordDTO medicalrecordDTOResult = null;
			try {
				medicalrecordDTOResult = medicalrecordService.createMedicalrecord(medicalrecordDTOExpected, request);
			} catch (ResourceConflictException e) {
				e.printStackTrace();
			}
			//THEN
			assertThat(medicalrecordDTOResult).isEqualTo(medicalrecordDTOExpected);
			assertThat(medicalrecordService.getMedicalrecords()).containsEntry("FirstName3 LastName3", medicalrecordExpected);
		}
		
		@Test
		@Tag("Corner case")
		@DisplayName("createMedicalrecordTest should throw a ResourceConflictException")
		public void createMedicalrecordTestShouldThrowResourceConflictException() {
			//GIVEN
			requestMock.setRequestURI("/medicalRecord");
			requestMock.setMethod("POST");
			request = new ServletWebRequest(requestMock);
			MedicalrecordDTO medicalrecordDTOExpected = new MedicalrecordDTO("FirstName1", "LastName1", "01/01/2001", Arrays.asList("a1nol:110mg", "b1nol:120mg"), Arrays.asList());
			Medicalrecord medicalrecordExpected = modelMapper.map(medicalrecordDTOExpected, Medicalrecord.class);
			medicalrecordExpected.setId(medicalrecordDTOExpected.getFirstName()+" "+medicalrecordDTOExpected.getLastName());
			when(medicalrecordDTOService.convertMedicalrecordFromDTO(any(MedicalrecordDTO.class))).then(invocation -> {
				MedicalrecordDTO medicalrecordDTO = invocation.getArgument(0, MedicalrecordDTO.class);
				Medicalrecord medicalrecord = modelMapper.map(medicalrecordDTO, Medicalrecord.class);
				medicalrecord.setId(medicalrecordDTO.getFirstName()+" "+medicalrecordDTO.getLastName());
				return medicalrecord;
			});
			//WHEN
			//THEN
			assertThrows(ResourceConflictException.class, () -> medicalrecordService.createMedicalrecord(medicalrecordDTOExpected, request));
		}
	}

	@Nested
	@Tag("PUT Medicalrecord Tests")
	@DisplayName("updateMedicalrecord tests")
	public class UpdateMedicalrecordTestClass {
		
		@Test
		@Tag("Nominal case")
		@DisplayName("updateMedicalrecordTest should update the medicalrecord in medicalrecords IoC and return it")
		public void updateMedicalrecordTestShouldUpdateMedicalrecordInIoCAndReturnIt() {
			//GIVEN
			requestMock.setRequestURI("/medicalRecord");
			requestMock.setMethod("PUT");
			request = new ServletWebRequest(requestMock);
			MedicalrecordDTO medicalrecordDTOExpected = new MedicalrecordDTO("FirstName1", "LastName1", "01/01/1999", Arrays.asList("a1bol:110mg", "b1nol:120mg"), Arrays.asList("fish"));
			Medicalrecord medicalrecordExpected = modelMapper.map(medicalrecordDTOExpected, Medicalrecord.class);
			medicalrecordExpected.setId(medicalrecordDTOExpected.getFirstName()+" "+medicalrecordDTOExpected.getLastName());
			when(medicalrecordDTOService.convertMedicalrecordFromDTO(any(MedicalrecordDTO.class))).then(invocation -> {
				MedicalrecordDTO medicalrecordDTO = invocation.getArgument(0, MedicalrecordDTO.class);
				Medicalrecord medicalrecord = modelMapper.map(medicalrecordDTO, Medicalrecord.class);
				medicalrecord.setId(medicalrecordDTO.getFirstName()+" "+medicalrecordDTO.getLastName());
				return medicalrecord;
			});
			when(medicalrecordDTOService.convertMedicalrecordToDTO(any(Medicalrecord.class))).then(invocation -> {
				Medicalrecord medicalrecord = invocation.getArgument(0, Medicalrecord.class);
				MedicalrecordDTO medicalrecordDTO = modelMapper.map(medicalrecord, MedicalrecordDTO.class);
				String[] names = medicalrecord.getId().split(" ");
				medicalrecordDTO.setFirstName(names[0]);
				medicalrecordDTO.setLastName(names[1]);
				return medicalrecordDTO;
				});
			//WHEN
			MedicalrecordDTO medicalrecordDTOResult = null;
			try {
				medicalrecordDTOResult = medicalrecordService.updateMedicalrecord(medicalrecordDTOExpected, request);
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
			}
			//THEN
			assertThat(medicalrecordDTOResult).isEqualTo(medicalrecordDTOExpected);
			assertThat(medicalrecordService.getMedicalrecords()).containsEntry("FirstName1 LastName1", medicalrecordExpected);
		}
		
		@Test
		@Tag("Corner case")
		@DisplayName("updateMedicalrecordTest should throw a ResourceNotFoundException")
		public void updateMedicalrecordTestShouldThrowResourceNotFoundException() {
			//GIVEN
			requestMock.setRequestURI("/medicalRecord");
			requestMock.setMethod("PUT");
			request = new ServletWebRequest(requestMock);
			MedicalrecordDTO medicalrecordDTOExpected = new MedicalrecordDTO("FirstName3", "LastName3", "03/03/2003", Arrays.asList("a3nol:310mg", "b3nol:320mg"), Arrays.asList("peanut"));
			Medicalrecord medicalrecordExpected = modelMapper.map(medicalrecordDTOExpected, Medicalrecord.class);
			medicalrecordExpected.setId(medicalrecordDTOExpected.getFirstName()+" "+medicalrecordDTOExpected.getLastName());
			when(medicalrecordDTOService.convertMedicalrecordFromDTO(any(MedicalrecordDTO.class))).then(invocation -> {
				MedicalrecordDTO medicalrecordDTO = invocation.getArgument(0, MedicalrecordDTO.class);
				Medicalrecord medicalrecord = modelMapper.map(medicalrecordDTO, Medicalrecord.class);
				medicalrecord.setId(medicalrecordDTO.getFirstName()+" "+medicalrecordDTO.getLastName());
				return medicalrecord;
			});
			//WHEN
			//THEN
			assertThrows(ResourceNotFoundException.class, () -> medicalrecordService.updateMedicalrecord(medicalrecordDTOExpected, request));
		}
	}
	
	@Nested
	@Tag("DELETE Medicalrecord Tests")
	@DisplayName("deleteMedicalrecord tests")
	public class DeleteMedicalrecordTestClass {
		
		@Test
		@Tag("Nominal case")
		@DisplayName("deleteMedicalrecordTest should delete the medicalrecord in medicalrecords IoC and return a new one")
		public void deleteMedicalrecordTestShouldDeleteMedicalrecordInIoCAndReturnANewOne() {
			//GIVEN
			requestMock.setRequestURI("/medicalRecord");
			requestMock.setMethod("DELETE");
			request = new ServletWebRequest(requestMock);
			MedicalrecordDTO medicalrecordDTOExpected = new MedicalrecordDTO("FirstName1", "LastName1", "01/01/2001", Arrays.asList("a1nol:110mg", "b1nol:120mg"), Arrays.asList());
			Medicalrecord medicalrecordExpected = modelMapper.map(medicalrecordDTOExpected, Medicalrecord.class);
			medicalrecordExpected.setId(medicalrecordDTOExpected.getFirstName()+" "+medicalrecordDTOExpected.getLastName());
			when(medicalrecordDTOService.convertMedicalrecordFromDTO(any(MedicalrecordDTO.class))).then(invocation -> {
				MedicalrecordDTO medicalrecordDTO = invocation.getArgument(0, MedicalrecordDTO.class);
				Medicalrecord medicalrecord = modelMapper.map(medicalrecordDTO, Medicalrecord.class);
				medicalrecord.setId(medicalrecordDTO.getFirstName()+" "+medicalrecordDTO.getLastName());
				return medicalrecord;
			});
			when(medicalrecordDTOService.convertMedicalrecordToDTO(any(Medicalrecord.class))).then(invocation -> {
				Medicalrecord medicalrecord = invocation.getArgument(0, Medicalrecord.class);
				MedicalrecordDTO medicalrecordDTO = modelMapper.map(medicalrecord, MedicalrecordDTO.class);
				medicalrecordDTO.setFirstName(null);
				medicalrecordDTO.setLastName(null);
				return medicalrecordDTO;
				});
			when(jsonRepository.getPersons()).thenReturn(new HashMap<String,Person>());
			//WHEN
			MedicalrecordDTO medicalrecordDTOResult = null;
			try {
				medicalrecordDTOResult = medicalrecordService.deleteMedicalrecord(medicalrecordDTOExpected, request);
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
			}
			//THEN
			assertThat(medicalrecordDTOResult).isEqualTo(new MedicalrecordDTO());
			assertThat(medicalrecordService.getMedicalrecords()).doesNotContainKey("FirstName1 LastName1");
		}
		
		@Test
		@Tag("Corner case")
		@DisplayName("deleteMedicalrecordTest should throw a ResourceNotFoundException")
		public void deleteMedicalrecordTestShouldThrowResourceNotFoundException() {
			//GIVEN
			requestMock.setRequestURI("/medicalRecord");
			requestMock.setMethod("DELETE");
			request = new ServletWebRequest(requestMock);
			MedicalrecordDTO medicalrecordDTOExpected = new MedicalrecordDTO("FirstName3", "LastName3", "03/03/2003", Arrays.asList("a3nol:310mg", "b3nol:320mg"), Arrays.asList("peanut"));
			Medicalrecord medicalrecordExpected = modelMapper.map(medicalrecordDTOExpected, Medicalrecord.class);
			medicalrecordExpected.setId(medicalrecordDTOExpected.getFirstName()+" "+medicalrecordDTOExpected.getLastName());
			when(medicalrecordDTOService.convertMedicalrecordFromDTO(any(MedicalrecordDTO.class))).then(invocation -> {
				MedicalrecordDTO medicalrecordDTO = invocation.getArgument(0, MedicalrecordDTO.class);
				Medicalrecord medicalrecord = modelMapper.map(medicalrecordDTO, Medicalrecord.class);
				medicalrecord.setId(medicalrecordDTO.getFirstName()+" "+medicalrecordDTO.getLastName());
				return medicalrecord;
			});
			//WHEN
			//THEN
			assertThrows(ResourceNotFoundException.class, () -> medicalrecordService.deleteMedicalrecord(medicalrecordDTOExpected, request));
		}
	}
}
