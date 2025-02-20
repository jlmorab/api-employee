package com.jlmorab.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jlmorab.data.dto.WebResponseDTO;
import com.jlmorab.data.entity.Employee;
import com.jlmorab.service.IEmployeeService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

	private final IEmployeeService service;
	
	@GetMapping("all")
	public WebResponseDTO getAll( HttpServletResponse response ) {
		return service.getAll( response );
	}//end getAll()

	@GetMapping("{id}")
	public WebResponseDTO getById( 
			@PathVariable Integer id, 
			HttpServletResponse response ) {
		return service.getById(response, id);
	}//end getById()

	@PostMapping("add")
	public WebResponseDTO add( 
			@RequestBody List<Employee> employees, 
			HttpServletResponse response ) {
		return service.add(response, employees);
	}//end add()

	@PutMapping("update/{id}")
	public WebResponseDTO update( 
			@PathVariable Integer id, 
			@RequestBody Employee employee, 
			HttpServletResponse response ) {
		return service.update(response, id, employee);
	}//end update()

	@DeleteMapping("delete/{id}")
	public WebResponseDTO delete( 
			@PathVariable Integer id, 
			HttpServletResponse response ) {
		return service.delete(response, id);
	}//end delete()

}
