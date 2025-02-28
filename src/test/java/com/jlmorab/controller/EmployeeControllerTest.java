package com.jlmorab.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jlmorab.config.WebSecurityConfig;
import com.jlmorab.data.TestData;
import com.jlmorab.data.entity.Employee;
import com.jlmorab.service.IEmployeeService;

import jakarta.servlet.http.HttpServletResponse;

@SuppressWarnings("unchecked")
@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc
@Import(WebSecurityConfig.class)
class EmployeeControllerTest {
	
	private static final Integer ANY_INT 	= TestData.getRandom(1, 1000);
	private final ObjectMapper mapper = new ObjectMapper()
			.registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	
	@Autowired
    MockMvc mockMvc;
	
	@MockitoBean
	IEmployeeService service;
	
	@Mock
	HttpServletResponse httpResponse;
	
	
	@Test
    void endpointGetAllEmployees_returnsOk() throws Exception {
		when( service.getAll(any()) ).thenReturn( TestData.response( HttpStatus.OK ) );
		
        mockMvc.perform(get("/employee/all"))
                .andExpect(status().isOk());
    }//end endpointGetAllEmployees_returnsOk()
	
	@Test
	void endpointGetAllEmployees_returnsMethodNotAllowed() throws Exception {
		mockMvc.perform(post("/employee/all"))
                .andExpect(status().isMethodNotAllowed());
	}//end endpointGetAllEmployees_returnsMethodNotAllowed()
	
	@Test
    void endpointGetEmployeeById_returnsOk() throws Exception {
		when( service.getById(any(), anyInt()) ).thenReturn( TestData.response( HttpStatus.OK ) );
		
        mockMvc.perform(get("/employee/{id}", ANY_INT))
                .andExpect(status().isOk());
    }//end endpointGetEmployeeById_returnsOk()
	
	@Test
	void endpointGetEmployeeById_returnsMethodNotAllowed() throws Exception {
		mockMvc.perform(post("/employee/{id}", ANY_INT))
                .andExpect(status().isMethodNotAllowed());
	}//end endpointGetEmployeeById_returnsMethodNotAllowed()
	
	@Test
    void endpointAddEmployee_returnsBadRequest() throws Exception {
		mockMvc.perform(post("/employee/add"))
                .andExpect(status().isBadRequest());
    }//end endpointAddEmployee_returnsBadRequest()
	
	@Test
    void endpointAddEmployee_returnsMethodNotAllowed() throws Exception {
		mockMvc.perform(put("/employee/add"))
                .andExpect(status().isMethodNotAllowed());
    }//end endpointAddEmployee_returnsMethodNotAllowed()
	
	@Test
	void endpointAddEmployee_returnsOk() throws Exception {
		List<Employee> request = TestData.employees();
		
		when( service.add(any(), any(List.class)) ).thenReturn( TestData.response( HttpStatus.OK ) );
		
		mockMvc.perform(post("/employee/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content( mapper.writeValueAsString(request) ))
                .andExpect(status().isOk());
	}//end endpointAddEmployee_returnsOk()
	
	@Test
    void endpointUpdateEmployee_returnsBadRequest() throws Exception {
		mockMvc.perform(put("/employee/update/{id}", ANY_INT))
                .andExpect(status().isBadRequest());
    }//end endpointUpdateEmployee_returnsBadRequest()
	
	@Test
    void endpointUpdateEmployee_returnsMethodNotAllowed() throws Exception {
		mockMvc.perform(delete("/employee/update/{id}", ANY_INT))
                .andExpect(status().isMethodNotAllowed());
    }//end endpointUpdateEmployee_returnsMethodNotAllowed()
	
	@Test
	void endpointUpdateEmployee_returnsOk() throws Exception {
		Employee request = TestData.employee();
		
		when( service.update(any(), anyInt(), any(Employee.class)) ).thenReturn( TestData.response( HttpStatus.OK ) );
		
		mockMvc.perform(put("/employee/update/{id}", ANY_INT)
				.contentType(MediaType.APPLICATION_JSON)
				.content( mapper.writeValueAsString(request) ))
                .andExpect(status().isOk());
	}//end endpointUpdateEmployee_returnsOk()
	
	@Test
    void endpointDeleteEmployee_returnsOk() throws Exception {
		when( service.getAll(any()) ).thenReturn( TestData.response( HttpStatus.OK ) );
		
        mockMvc.perform(delete("/employee/delete/{id}", ANY_INT))
                .andExpect(status().isOk());
    }//end endpointDeleteEmployee_returnsOk()
	
	@Test
	void endpointDeleteEmployee_returnsMethodNotAllowed() throws Exception {
		mockMvc.perform(get("/employee/delete/{id}", ANY_INT))
                .andExpect(status().isMethodNotAllowed());
	}//end endpointDeleteEmployee_returnsMethodNotAllowed()
}
