package com.jlmorab.data.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
public class Employee {

	@Id
	@Column(name = "emp_id")
	@JsonProperty("emp_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer empId;

	@Column(name = "emp_name")
	@JsonProperty("emp_name")
	private String empName;

	@Column(name = "emp_paternal_surname")
	@JsonProperty("emp_paternal_surname")
	private String empPaternalSurname;

	@Column(name = "emp_maternal_surname")
	@JsonProperty("emp_maternal_surname")
	private String empMaternalSurname;

	@Column(name = "emp_birthdate")
	@JsonProperty("emp_birthdate")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate empBirthdate;

	@Column(name = "sex_id")
	@JsonProperty("sex_id")
	private String sexId;

}
