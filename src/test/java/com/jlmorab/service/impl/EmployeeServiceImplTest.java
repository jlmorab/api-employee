package com.jlmorab.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;

import com.jlmorab.data.TestData;
import com.jlmorab.data.dto.MetaDTO;
import com.jlmorab.data.dto.WebResponseDTO;
import com.jlmorab.data.entity.Employee;
import com.jlmorab.repository.IEmployeeRepository;

import jakarta.servlet.http.HttpServletResponse;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

	private static final Exception 	TEST_EXCEPTION 	= mock(DataAccessResourceFailureException.class);
	private static final Integer 	ANY_INT 		= TestData.getRandom(1, 1000);
	
	@InjectMocks
	EmployeeServiceImpl service;
	
	@Mock
	IEmployeeRepository repository;
	
	@Mock
	HttpServletResponse httpResponse;
	
	
	@BeforeEach
	void setUp() {
		reset( repository, httpResponse );
	}//end sertUp()
	
	
	@Test
	void getEmployees_whenRecordExists() {
		List<Employee> expected = TestData.employees();
		
		when( repository.findAll() ).thenReturn( expected );
		
		WebResponseDTO actual = service.getAll( httpResponse );
		
		List<Employee> data = (List<Employee>) obtainData( actual, HttpStatus.OK );
		assertThat( data ).isEqualTo( expected );
	}//end getEmployees_whenRecordExists()
	
	@Test
	void getEmployeeById_whenExist() {
		Employee expected = TestData.employee();
		
		when( repository.findById(anyInt()) ).thenReturn( Optional.of(expected) );
		
		WebResponseDTO actual = service.getById(httpResponse, ANY_INT);
		
		Object data = obtainData( actual, HttpStatus.OK );
		assertEquals( expected, data );
	}//end getEmployeeById_whenExist()
	
	@Test
	void getEmployeeById_whenNotExist() {
		when( repository.findById(anyInt()) ).thenReturn( Optional.empty() );
		
		WebResponseDTO actual = service.getById(httpResponse, ANY_INT);
		
		validateResponse( actual, HttpStatus.NOT_FOUND );
	}//end getEmployeeById_whenNotExist()
	
	@Test
	void getEmployeeById_whenIdIsntProvided() {
		when( repository.findById(null) ).thenThrow( new IllegalArgumentException() );
		
		WebResponseDTO actual = service.getById(httpResponse, null);
		
		validateResponse( actual, HttpStatus.BAD_REQUEST );
	}//end getEmployeeById_whenIdIsntProvided()
	
	@Test
	void getEmployeeById_whenThrowException() {
		when( repository.findById(anyInt()) ).thenThrow( TEST_EXCEPTION );
		
		WebResponseDTO actual = service.getById(httpResponse, ANY_INT);
		
		validateResponse( actual, HttpStatus.INTERNAL_SERVER_ERROR );
	}//end getEmployeeById_whenThrowException()
	
	@Test
	void addEmployees_whenSavedCorrectly() {
		List<Employee> expected = TestData.employees();
		
		when( repository.saveAll(any(List.class)) ).thenReturn( expected );
		
		WebResponseDTO actual = service.add( httpResponse, expected );
		
		List<Employee> data = (List<Employee>) obtainData( actual, HttpStatus.OK );
		assertThat( data ).isEqualTo( expected );
	}//end addEmployees_whenSavedCorrectly()
	
	@Test
	void updateEmployee_whenExist() {
		Employee request = TestData.employee();
		Employee found = TestData.employee();
		
		when( repository.findById(anyInt()) ).thenReturn( Optional.of(found) );
		when( repository.save(any()) ).thenReturn(found);
		
		WebResponseDTO actual = service.update( httpResponse, ANY_INT, request );
		
		boolean data = (boolean) obtainData( actual, HttpStatus.OK );
		assertTrue( data );
		assertEquals( ANY_INT, request.getEmpId() );
	}//end updateEmployee_whenExist()
	
	@Test
	void updateEmployee_whenNotExist() {
		Employee request = TestData.employee();
		
		when( repository.findById(anyInt()) ).thenReturn( Optional.empty() );
		
		WebResponseDTO actual = service.update( httpResponse, ANY_INT, request );
		
		validateResponse( actual, HttpStatus.NOT_FOUND );
	}//end updateEmployee_whenNotExist()
	
	@Test
	void deleteEmployee_whenDeletedCorrectly() {
		Boolean expected = true;
		
		when( repository.deleteEmployeeById(anyInt()) ).thenReturn( expected );
		
		WebResponseDTO actual = service.delete( httpResponse, ANY_INT );
		
		boolean data = (boolean) obtainData( actual, HttpStatus.OK );
		assertTrue( data );
	}//end deleteEmployee_whenDeletedCorrectly()
	
	@Test 
	void deleteEmployee_whenCantDelete() {
		Boolean expected = false;
		
		when( repository.deleteEmployeeById(anyInt()) ).thenReturn( expected );
		
		WebResponseDTO actual = service.delete( httpResponse, ANY_INT );
		
		boolean data = (boolean) obtainData( actual, HttpStatus.OK );
		assertFalse( data );
	}//end deleteEmployee_whenCantDelete()
	
	private boolean validateResponse( WebResponseDTO response, HttpStatus httpStatus ) {
		boolean result = false;
		assertNotNull( response );
		MetaDTO meta = response.getMeta();
		assertEquals( httpStatus.value(), meta.getStatusCode() );
		assertEquals( httpStatus.name(), meta.getStatus() );
		verify( httpResponse, times(1) ).setStatus( httpStatus.value() );
		result = true;
		return result;
	}//end validateResponse()
	
	private Object obtainData( WebResponseDTO response, HttpStatus httpStatus ) {
		if( validateResponse( response, httpStatus ) ) {
			return response.getData();
		} else {
			return null;
		}//end if
	}//end obtainData()

}
