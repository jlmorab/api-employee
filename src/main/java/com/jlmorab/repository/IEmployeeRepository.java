package com.jlmorab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jlmorab.data.entity.Employee;

@Repository
public interface IEmployeeRepository extends JpaRepository<Employee, Integer> {

	@Query(value = "SELECT fn_delete_employee(:id)", nativeQuery = true)
	boolean deleteEmployeeById(@Param("id") Integer id);

}
