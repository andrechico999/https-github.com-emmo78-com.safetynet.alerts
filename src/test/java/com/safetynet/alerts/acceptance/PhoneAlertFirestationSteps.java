package com.safetynet.alerts.acceptance;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@SpringBootTest
@AutoConfigureMockMvc
public class PhoneAlertFirestationSteps {
	
	@Autowired
	MockMvc mockMvc;
	
	@Given("les personnes contexte 3:")
	public void lesPersonnes(List<Map<String, String>> lesPersonnes) {}
	
	@Given("les stations contexte 3:")
	public void lesStations(List<Map<String, String>> lesStations) {}
	
	@When("utilisateur 3A requête la station numéro {int}")
	public void requeteA(int stationNumber) {}
	
	@Then("la liste A des numéros de téléphone est:")
	public void resultatA(List<String> resultat) {}
	
	@When("utilisateur 3B requête la station numéro {int}")
	public void requeteB(int stationNumber) {}
	
	@Then("la liste B des numéros de téléphone est:")
	public void resultatB() {}
}
