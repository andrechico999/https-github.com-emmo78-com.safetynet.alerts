package com.safetynet.alerts.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import com.safetynet.alerts.service.PersonService;

@ExtendWith(MockitoExtension.class)
public class PersonControllerTest {
	//class to be tested
	@InjectMocks
	private PersonController personController;
	
	@Mock
	private PersonService personPersonService;
	
	
	@Test
	public void doSomething() {
		//GIVEN
		
		//WHEN
		
		//THEN
	}

}
