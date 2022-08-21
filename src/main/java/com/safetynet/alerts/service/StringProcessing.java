package com.safetynet.alerts.service;

import java.util.List;

import com.safetynet.alerts.model.Person;

public interface StringProcessing {
	StringBuffer appendFields(StringBuffer stringFieldsPerson, Person person, List<Fields> fields);
	String upperCasingFirstLetter(String word);
}

