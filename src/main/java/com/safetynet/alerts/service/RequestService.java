package com.safetynet.alerts.service;

import org.springframework.web.context.request.WebRequest;

public interface RequestService {
	String upperCasingFirstLetter(String word);
	String requestToString(WebRequest request);
}

