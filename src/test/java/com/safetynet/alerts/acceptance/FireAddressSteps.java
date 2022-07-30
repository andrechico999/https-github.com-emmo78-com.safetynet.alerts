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
public class FireAddressSteps {
	
	@Autowired
	MockMvc mockMvc;
	
	@Given("les personnes avec leur antécédants médicaux contexte 4:")
	public void lesPersonnes(List<Map<String, String>> lesPersonnes) {}
	
	@Given("les stations contexte 4:")
	public void lesStations(List<Map<String, String>> lesStations) {}
	
	@When("utilisateur 4A requête l’addresse {string}")
	public void requeteA(String address) {}
	
	@Then("la liste A des habitants est:")
	public void resultatA(List<Map<String, String>> resultat) {}
	
	@Then("le numéro A de la caserne de pompiers la desservant est:")
	public void nombreA(List<String> resultat) {}
	
	@When("utilisateur 4B requête l’addresse {string}")
	public void requeteB(String address) {}
	
	@Then("la liste B des habitants est:")
	public void resultatB() {}
	
	@Then("le numéro B de la caserne de pompiers la desservant est:")
	public void nombreB() {}
}
