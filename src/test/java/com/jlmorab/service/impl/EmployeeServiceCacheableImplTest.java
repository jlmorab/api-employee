package com.jlmorab.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import com.jlmorab.TestCacheConfig;
import com.jlmorab.data.TestData;
import com.jlmorab.data.entity.Employee;
import com.jlmorab.repository.IEmployeeRepository;

@SpringBootTest( classes = { EmployeeServiceCacheableImpl.class, TestCacheConfig.class } )
@ExtendWith(MockitoExtension.class)
class EmployeeServiceCacheableImplTest {
	
	private static final Integer 	ANY_INT = TestData.getRandom(1, 1000);
	
	@Autowired
	EmployeeServiceCacheableImpl service;
	
	@MockitoBean
	IEmployeeRepository repository;
	
	@MockitoSpyBean
	CacheManager cacheManager;
	
	@BeforeEach
	void setUp() {
		getCache().clear();
	}//end setUp()
	
	
	@Test
	void getEmployeeById_usingCache_shouldCallRepositoryOnce() {
		Employee employee = TestData.employee();
		
		when( repository.findById(anyInt()) ).thenReturn( Optional.of( employee ) );
		
		service.getEmployeeById( ANY_INT );
		service.getEmployeeById( ANY_INT );
		
		verify( repository, times(1) ).findById( anyInt() );
	}//end getEmployeeById_usingCache_shouldCallRepositoryOnce()
	
	@Test
	void getEmployeeById_usingCache_shouldntCachingWhenReturnNull() {
		when( repository.findById(anyInt()) ).thenReturn( Optional.empty() );
		
		service.getEmployeeById( ANY_INT );
		
		assertNull( getCache().get( ANY_INT ) );
	}//end getEmployeeById_usingCache_shouldntCachingWhenReturnNull() 
	
	@Test
	void getAllEmployees_usingCache_shouldCallRepositoryOnce() {
		List<Employee> employees = TestData.employees();
		
		when( repository.findAll() ).thenReturn( employees );
		
		service.getAllEmployees();
		service.getAllEmployees();
		
		verify( repository, times(1) ).findAll();
	}//end getAllEmployees_usingCache_shouldCallRepositoryOnce()
	
	@Test
	void getAllEmployees_usingCache_shouldntCachingWhenReturnEmpty() {
		when( repository.findAll() ).thenReturn( List.of() );
		
		service.getAllEmployees();
		
		assertNull( getCache().get("all") );
	}//end getAllEmployees_usingCache_shouldntCachingWhenReturnEmpty()
	
	@Test
	void addEmployee_usingCache_shouldBeAddedToCacheOnSave() {
		Employee expected = TestData.employee();
		
		when( repository.save( any() ) ).thenReturn( expected );
		
		service.addEmployee( expected );
		
		verify( repository, times(1) ).save( any() );
		assertNotNull( getCache().get( expected.getEmpId() ) );
	}//end addEmployee_usingCache_shouldBeAddedToCacheOnSave()
	
	@Test
	void deleteEmployeeById_usingCache_shouldBeRemovedFromCache() {
		getCache().put(ANY_INT, TestData.employee());
		
		when( repository.deleteEmployeeById( anyInt() ) ).thenReturn( true );
		
		service.deleteEmployeeById( ANY_INT );
		
		verify( repository, times(1) ).deleteEmployeeById(anyInt());
		assertNull( getCache().get(ANY_INT) );
	}//end deleteEmployeeById_usingCache_shouldBeRemovedFromCache()
	
	@Test
	void deleteEmployeeById_usingCache_shouldntBeRemovedFromCacheWhenFailDB() {
		getCache().put(ANY_INT, TestData.employee());
		
		when( repository.deleteEmployeeById( anyInt() ) ).thenReturn( false );
		
		service.deleteEmployeeById( ANY_INT );
		
		verify( repository, times(1) ).deleteEmployeeById(anyInt());
		assertNotNull( getCache().get(ANY_INT) );
	}//end deleteEmployeeById_usingCache_shouldntBeRemovedFromCacheWhenFailDB()
	
	
	private Cache getCache() {
		return cacheManager.getCache("employees");
	}//end getCache()
	
}
