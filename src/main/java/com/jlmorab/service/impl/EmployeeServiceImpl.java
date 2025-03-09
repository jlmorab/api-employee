package com.jlmorab.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.jlmorab.data.dto.WebResponseDTO;
import com.jlmorab.data.entity.Employee;
import com.jlmorab.exception.LogicException;
import com.jlmorab.service.IEmployeeService;
import com.jlmorab.service.ServiceAbstract;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl extends ServiceAbstract implements IEmployeeService {
	
    private final EmployeeServiceCacheableImpl service;

	public WebResponseDTO getAll(HttpServletResponse response) {
		return executeFlow( response, log, () -> service.getAllEmployees() );
	}//end getAll()

	@Override
	public WebResponseDTO getById(HttpServletResponse response, Integer id) {
		return executeFlow( response, log, () -> {
			Employee found = service.getEmployeeById( id );
			
			if( found == null ) {
				throw new LogicException("Employee not found", HttpStatus.NOT_FOUND);
			} else {
				return found;
			}//end if
		});
	}//end getById()
	
	@Override
	public WebResponseDTO add(HttpServletResponse response, List<Employee> employees) {
		return executeFlow( response, log, () ->
			employees.stream()
				.map(service::addEmployee)
				.toList()
		);
	}//end add()

	@Override
	public WebResponseDTO update(HttpServletResponse response, Integer id, Employee employee) {
		return executeFlow( response, log, () -> {
			Employee found = service.getEmployeeById( id );
			
			if( found == null ) {
				throw new LogicException("Employee not found", HttpStatus.NOT_FOUND);
			}//end if
			
			employee.setEmpId(id);
			
			service.addEmployee( employee );
			return true;
		});
	}//end update()

	@Override
	public WebResponseDTO delete(HttpServletResponse response, Integer id) {
		return executeFlow( response, log, () -> service.deleteEmployeeById(id) );
	}//end delete()
	
}
