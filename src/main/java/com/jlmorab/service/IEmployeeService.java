package com.jlmorab.service;

import java.util.List;

import com.jlmorab.data.dto.WebResponseDTO;
import com.jlmorab.data.entity.Employee;

import jakarta.servlet.http.HttpServletResponse;

public interface IEmployeeService {
	
	WebResponseDTO getAll(HttpServletResponse response);
	WebResponseDTO getById(HttpServletResponse response, Integer id);
	WebResponseDTO add(HttpServletResponse response, List<Employee> employees);
	WebResponseDTO update(HttpServletResponse response, Integer id, Employee employee);
	WebResponseDTO delete(HttpServletResponse response, Integer id);
	
}
