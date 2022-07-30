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

public class FloodStationsStationsSteps {
	
	@Autowired
	MockMvc mockMvc;
	
	@Given("les personnes avec leur antécédants médicaux contexte 5:")
	public void lesPersonnes(List<Map<String, String>> lesPersonnes) {}
	
	@Given("les stations contexte 5:")
	public void lesStations(List<Map<String, String>> lesStations) {}
	
	@When("utilisateur requête les stations numéro {string}")
	public void requeteA(String numStations) {}
	
	@Then("la liste de tous les foyers desservis par les casernes est:")
	public void resultatA(List<Map<String, String>> resultat) {}
	
}
