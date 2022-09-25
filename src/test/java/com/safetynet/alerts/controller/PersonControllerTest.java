package com.safetynet.alerts.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safetynet.alerts.service.PersonService;

@ExtendWith(MockitoExtension.class)
public class PersonControllerTest {
	//class to be tested
	@InjectMocks
	private PersonController personController;
	
	@Mock
	private PersonService personService;
	
	
	@Test
	public void personInfoFirstNameLastName() {
		//GIVEN
		//PersonAdressNameDTO personA = new 
		//when(personService.findPersonsByFirstNameAndLastName("A", "B")).thenReturn(new ArrayList(Arrays.asList()));
		//WHEN
		
		//THEN
	}

}
