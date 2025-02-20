package com.jlmorab.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.jlmorab.data.dto.WebResponseDTO;
import com.jlmorab.data.entity.Employee;
import com.jlmorab.exception.LogicException;
import com.jlmorab.repository.IEmployeeRepository;
import com.jlmorab.service.IEmployeeService;
import com.jlmorab.service.ServiceAbstract;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl extends ServiceAbstract implements IEmployeeService {

	private final IEmployeeRepository repository;

	@Override
	public WebResponseDTO getAll(HttpServletResponse response) {
		return executeFlow( response, log, () -> repository.findAll() );
	}//end getAll()

	@Override
	public WebResponseDTO getById(HttpServletResponse response, Integer id) {
		return executeFlow( response, log, () -> {
			Optional<Employee> found = repository.findById( id );
			
			if( !found.isPresent() ) {
				throw new LogicException("Employee not found", HttpStatus.NOT_FOUND);
			} else {
				return found.get();
			}//end if
		});
	}//end getById()
	
	@Override
	public WebResponseDTO add(HttpServletResponse response, List<Employee> employees) {
		return executeFlow( response, log, () -> repository.saveAll(employees) );
	}//end add()

	@Override
	public WebResponseDTO update(HttpServletResponse response, Integer id, Employee employee) {
		return executeFlow( response, log, () -> {
			Optional<Employee> found = repository.findById( id );
			
			if( !found.isPresent() ) {
				throw new LogicException("Employee not found", HttpStatus.NOT_FOUND);
			}//end if
			
			employee.setEmpId(id);
			
			repository.save(employee);
			return true;
		});
	}//end update()

	@Override
	public WebResponseDTO delete(HttpServletResponse response, Integer id) {
		return executeFlow( response, log, () -> repository.deleteEmployeeById(id) );
	}//end delete()
	
}
