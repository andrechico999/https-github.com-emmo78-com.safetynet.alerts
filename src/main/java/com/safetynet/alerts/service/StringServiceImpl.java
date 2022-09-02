package com.safetynet.alerts.service;

import org.springframework.stereotype.Service;

@Service
public class StringServiceImpl implements StringService {

	@Override
	public String upperCasingFirstLetter(String word) {
		return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
	}
}
