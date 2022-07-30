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
public class ChildAlertAddressSteps {

	@Autowired
	MockMvc mockMvc;
	
	@Given("les personnes contexte 2:")
	public void lesPersonnes(List<Map<String, String>> lesPersonnes) {}
	
	@When("utilisateur 2A requête l’addresse {string}")
	public void requeteA(String address) {}
	
	@Then("la liste A des enfants est:")
	public void resultatA(List<Map<String, String>> resultat) {}
	
	@When("utilisateur 2B requête l’addresse {string}")
	public void requeteB(String address) {}
	
	@Then("la liste B des enfants est:")
	public void resultatB(List<Map<String, String>> resultat) {}
	
	@When("utilisateur 2C requête l’addresse {string}")
	public void requeteC(String address) {}
	
	@Then("la liste C des enfants est:")
	public void resultatC() {}
}
