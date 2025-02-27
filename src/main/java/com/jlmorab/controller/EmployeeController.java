package com.jlmorab.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name="Empleados", description = "Operaciones sobre empleados")
@Validated
@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

	private final IEmployeeService service;
	
	@Operation(summary = "Obtener todos los empleados", description = "Obtiene todos los empleados registrados")
	@ApiResponse(responseCode = "200", description = "Ejecución correcta. Devuelve los registros disponibles")
	@GetMapping("all")
	public WebResponseDTO getAll( HttpServletResponse response ) {
		return service.getAll( response );
	}//end getAll()

	@Operation(summary = "Elimina un empleado", description = "Obtiene todos los empleados registrados en la tabla empleado")
	@ApiResponse(responseCode = "200", description = "Ejecución correcta. Devuelve los registros disponibles")
	@ApiResponse(responseCode = "404", description = "Empleado inválido. El ID proporcionado no corresponde a un empleado registrado")
	@GetMapping("{id}")
	public WebResponseDTO getById( 
			@Parameter(description = "ID del empleado", example = "1")
			@PathVariable Integer id, 
			HttpServletResponse response ) {
		return service.getById(response, id);
	}//end getById()

	@Operation(summary = "Registra varios empleados. Obtiene el registro del empleado con su ID", description = "Registra uno o varios empleados")
	@ApiResponse(responseCode = "200", description = "Ejecución correcta. Devuelve los registros de los empleados registrados incluyendo el ID que le corresponde")
	@ApiResponse(responseCode = "400", description = "Errores de validación. Existe error en uno o varios de los empleados. Donde: Uno o varios de los campos proporcionados no cumplen con las validaciones definidas")
	@PostMapping("add")
	public WebResponseDTO add( 
			@Valid @RequestBody List<Employee> employees, 
			HttpServletResponse response ) {
		return service.add(response, employees);
	}//end add()

	@Operation(summary = "Actualiza los datos de un empleado", description = "Actualiza los registros de un empleado (excepto su ID)")
	@ApiResponse(responseCode = "200", description = "Ejecución correcta. El empleado se actualizo.")
	@ApiResponse(responseCode = "400", description = "Errores de validación. Uno o varios de los campos proporcionados no cumplen con las validaciones definidas")
	@ApiResponse(responseCode = "404", description = "Empleado inválido. El ID proporcionado no corresponde a un empleado registrado")
	@PutMapping("update/{id}")
	public WebResponseDTO update( 
			@Parameter(description = "ID del empleado", example = "1")
			@PathVariable Integer id, 
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del empleado", required = true,
			content = @Content(schema = @Schema(implementation = Employee.class))) 
			@Valid @RequestBody Employee employee, 
			HttpServletResponse response ) {
		return service.update(response, id, employee);
	}//end update()

	@Operation(summary = "Elimina un empleado", description = "Obtiene todos los empleados registrados en la tabla empleado")
	@ApiResponse(responseCode = "200", description = "Ejecución correcta. Devuelve los registros disponibles")
	@ApiResponse(responseCode = "404", description = "Empleado inválido. El ID proporcionado no corresponde a un empleado registrado")
	@DeleteMapping("delete/{id}")
	public WebResponseDTO delete( 
			@Parameter(description = "ID del empleado", example = "1")
			@PathVariable Integer id, 
			HttpServletResponse response ) {
		return service.delete(response, id);
	}//end delete()

}
