package com.jlmorab.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jlmorab.data.dto.MetaDTO;
import com.jlmorab.data.dto.WebResponseDTO;
import com.jlmorab.data.entity.Employee;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestData {

	public static int getRandom(int min, int max) {
		Random random = new Random();
		return random.nextInt( max ) + min;
	}//end getRandom()
	
	public static String getRandomObject() {
		Map<String, Object> object = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		
		int random = TestData.getRandom(1, 1000);
		String text = String.format("data%d", random);
		
		object.put("id", random);
		object.put("data", text);
		
		try { 
			return mapper.writeValueAsString(object);
		} catch( Exception e ) {
			return null;
		}//end try
	}//end getRandomObject()
	
	public static Employee employee() {
		int random = TestData.getRandom(1, 1000);
		String text = String.format("data%d", random);
		return new Employee(random, text, text, text, LocalDate.now(), "M");
	}//end employee()
	
	public static List<Employee> employees() {
		List<Employee> result = new ArrayList<>();
		for( int i = 1 ; i <= TestData.getRandom(1, 10) ; i++ ) {
			result.add( TestData.employee() );
		}//end for
		return result;
	}//end employees()
	
	public static WebResponseDTO response( HttpStatus httpStatus ) {
		MetaDTO meta = new MetaDTO( httpStatus );
		return WebResponseDTO.builder()
				.meta(meta)
				.build();
	}//end response()
	
}
