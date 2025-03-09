package com.jlmorab.service.impl;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.jlmorab.data.entity.Employee;
import com.jlmorab.repository.IEmployeeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig( cacheNames = { "employees" } )
public class EmployeeServiceCacheableImpl {
	
	private final IEmployeeRepository repository;

	@Cacheable( key = "'all'", unless = "#result.isEmpty()" )
	public List<Employee> getAllEmployees() {
		return repository.findAll();
	}//end getAllEmployees()
	
	@Cacheable( key = "#id", unless = "#result == null" )
	public Employee getEmployeeById( int id ) {
		return repository.findById( id ).orElse( null );
	}//end getEmployeeById()
	
	@CachePut( key = "#result.empId" )
	public Employee addEmployee( Employee employee ) {
		return repository.save( employee );
	}//end addEmployee()
	
	@CacheEvict( key = "#id", condition = "#result" )
	public boolean deleteEmployeeById( int id ) {
		return repository.deleteEmployeeById( id );
	}//end deleteEmployeeById()
	
}
