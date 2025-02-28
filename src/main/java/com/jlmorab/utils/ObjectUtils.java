package com.jlmorab.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ObjectUtils {
	
	/**
	 * Get object representation in JSON string
	 * @param object Is any object {@code Object}
	 * @return Object JSON in string
	 */
	public static String getJsonString(Object object) {
		
		String result = "";
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			result = mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			result = "{}";
		}//end try
		
		return result;
		
	}//end getJsonString()
	
}
