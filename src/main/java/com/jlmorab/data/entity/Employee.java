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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cat_employee")
@Schema(name = "Employee", description = "Entidad que representa a un empleado")
public class Employee {

	@Id
	@Column(name = "emp_id")
	@JsonProperty("emp_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "Identificador único del empleado", example = "1")
	private Integer empId;

	@NotBlank(message = "El nombre es requerido. No puede estar vacío")
	@Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
	@Column(name = "emp_name")
	@JsonProperty("emp_name")
	@Schema(description = "Primer nombre del empleado", example = "Juan")
	private String empName;

	@NotBlank(message = "El apellido paterno es requerido. No puede estar vacío")
	@Size(min = 2, max = 50, message = "El apellido paterno debe tener entre 2 y 50 caracteres")
	@Column(name = "emp_paternal_surname")
	@JsonProperty("emp_paternal_surname")
	@Schema(description = "Apellido paterno del empleado", example = "Pérez")
	private String empPaternalSurname;

	@NotBlank(message = "El apellido materno es requerido. No puede estar vacío")
	@Size(min = 2, max = 50, message = "El apellido materno debe tener entre 2 y 50 caracteres")
	@Column(name = "emp_maternal_surname")
	@JsonProperty("emp_maternal_surname")
	@Schema(description = "Apellido materno del empleado", example = "Gómez")
	private String empMaternalSurname;

	@NotNull(message = "La fecha de nacimiento es requerida")
	@Past(message = "La fecha de nacimiento debe ser en el pasado")
	@Column(name = "emp_birthdate")
	@JsonProperty("emp_birthdate")
	@JsonFormat(pattern = "dd-MM-yyyy")
	@Schema(description = "Fecha de nacimiento del empleado (formato dd-MM-yyyy)", type = "string", pattern = "dd-MM-yyyy", example = "15-08-1990")
	private LocalDate empBirthdate;

	@NotNull(message = "El campo de sexo es requerido")
	@Pattern(regexp = "[MFX]", message = "El sexo debe ser 'M', 'F' o 'X'")
	@Column(name = "sex_id")
	@JsonProperty("sex_id")
	@Schema(description = "Sexo del empleado (M: Masculino, F: Femenino, X: No especificado)", example = "M")
	private String sexId;

}
