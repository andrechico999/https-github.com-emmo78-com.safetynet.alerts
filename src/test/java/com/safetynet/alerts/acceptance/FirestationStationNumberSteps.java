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
import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
public class FirestationStationNumberSteps {
	
	@Autowired
	MockMvc mockMvc;
	
	@Given("les personnes contexte 1:")
	public void lesPersonnes(List<Map<String, String>> lesPersonnes) {}
	
	@Given("les stations contexte 1:")
	public void lesStations(List<Map<String, String>> lesStations) {}
	
	@When("utilisateur 1A requête la station numéro {int}")
	public void requeteA(int stationNumber) {}
	
	@Then("la liste 1A des personne est:")
	public void resultatA(List<Map<String, String>> resultat) {}
	
	@Then("le nombre A d’adulte et d’enfant est:")
	public void nombreA(List<Map<String, String>> resultat) {}
	
	@When("utilisateur 1B requête la station numéro {int}")
	public void requeteB(int stationNumber) {}
	
	@Then("la liste 1B des personne est:")
	public void resultatB() {}
	
	@Then("le nombre B d’adulte et d’enfant est:")
	public void nombreB() {}
}
