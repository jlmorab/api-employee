package com.jlmorab.service;

import java.util.List;

import com.jlmorab.data.dto.WebResponseDTO;
import com.jlmorab.data.entity.EmployeeEntity;

import jakarta.servlet.http.HttpServletResponse;

public interface IEmployeeService {
	
	WebResponseDTO getAll(HttpServletResponse response);
	WebResponseDTO getById(HttpServletResponse response, Integer id);
	WebResponseDTO add(HttpServletResponse response, List<EmployeeEntity> employees);
	WebResponseDTO update(HttpServletResponse response, Integer id, EmployeeEntity employee);
	WebResponseDTO delete(HttpServletResponse response, Integer id);
	
}
