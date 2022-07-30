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
public class CommunityEmailCity {

	@Autowired
	MockMvc mockMvc;
	
	@Given("les personnes contexte 7:")
	public void lesPersonnes(List<Map<String, String>> lesPersonnes) {}
	
	@When("utilisateur A requête la ville {word}")
	public void requeteA(String firstNaame) {}
	
	@Then("la liste A des courriels est:")
	public void resultatA(List<String> resultat) {}
	
	@When("utilisateur B requête la ville {word}")
	public void requeteB(String firstNaame) {}
	
	@Then("la liste B des courriels est:")
	public void resultatB() {}
}
