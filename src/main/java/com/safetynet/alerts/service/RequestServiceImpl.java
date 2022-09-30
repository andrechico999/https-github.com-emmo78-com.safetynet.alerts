package com.safetynet.alerts.service;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

@Service
public class RequestServiceImpl implements RequestService {

	@Override
	public String upperCasingFirstLetter(String word) {
		return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
	}

	public String requestToString(WebRequest request) {
		StringBuffer bodyParameters = new StringBuffer(request.getDescription(false)+"?"); 
		request.getParameterMap().forEach((p,v) -> {
			bodyParameters.append(p+"=");
			int i=0;
			while (i<(v.length-1)) {
				bodyParameters.append(v[i]+",");
				i++;}
			bodyParameters.append(v[i]+"&");
		});
		int length = bodyParameters.length();
		bodyParameters.delete(length-1, length);
		return bodyParameters.toString();
	}

}
