package com.jlmorab.data.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(name = "Employee", description = "Entidad que representa a un empleado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cat_employee")
public class Employee {

	@Schema(description = "Identificador único del empleado", example = "1")
	@Id
	@Column(name = "emp_id")
	@JsonProperty("emp_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer empId;
	
	@Schema(description = "Primer nombre del empleado", example = "Juan")
	@NotBlank(message = "El nombre es requerido. No puede estar vacío")
	@Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
	@Column(name = "emp_name")
	@JsonProperty("emp_name")
	private String empName;
	
	@Schema(description = "Apellido paterno del empleado", example = "Pérez")
	@NotBlank(message = "El apellido paterno es requerido. No puede estar vacío")
	@Size(min = 2, max = 50, message = "El apellido paterno debe tener entre 2 y 50 caracteres")
	@Column(name = "emp_paternal_surname")
	@JsonProperty("emp_paternal_surname")
	private String empPaternalSurname;
	
	@Schema(description = "Apellido materno del empleado", example = "Gómez")
	@NotBlank(message = "El apellido materno es requerido. No puede estar vacío")
	@Size(min = 2, max = 50, message = "El apellido materno debe tener entre 2 y 50 caracteres")
	@Column(name = "emp_maternal_surname")
	@JsonProperty("emp_maternal_surname")
	private String empMaternalSurname;
	
	@Schema(description = "Fecha de nacimiento del empleado (formato dd-MM-yyyy)", type = "string", pattern = "dd-MM-yyyy", example = "15-08-1990")
	@NotNull(message = "La fecha de nacimiento es requerida")
	@Past(message = "La fecha de nacimiento debe ser en el pasado")
	@Column(name = "emp_birthdate")
	@JsonProperty("emp_birthdate")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate empBirthdate;
	
	@Schema(description = "Sexo del empleado (M: Masculino, F: Femenino, X: No especificado)", example = "M")
	@NotNull(message = "El campo de sexo es requerido")
	@Pattern(regexp = "[MFX]", message = "El sexo debe ser 'M', 'F' o 'X'")
	@Column(name = "sex_id")
	@JsonProperty("sex_id")
	private String sexId;

}
