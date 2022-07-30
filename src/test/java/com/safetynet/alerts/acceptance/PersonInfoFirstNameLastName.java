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
public class PersonInfoFirstNameLastName {

	@Autowired
	MockMvc mockMvc;
	
	@Given("les personnes avec leur antécédants médicaux contexte 6:")
	public void lesPersonnes(List<Map<String, String>> lesPersonnes) {}
	
	@When("utilisateur A requête prénom {word} et nom {word}")
	public void requeteA(String firstNaame, String lastName) {}
	
	@Then("la liste des personnes 6A est:")
	public void resultatA(List<Map<String, String>> resultat) {}
	
	@When("utilisateur B requête prénom {word} et nom {word}")
	public void requeteB(String firstNaame, String lastName) {}
	
	@Then("la liste des personnes 6B est:")
	public void resultatB() {}
}
