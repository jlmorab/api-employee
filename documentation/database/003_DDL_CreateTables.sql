/* === ROLLBACK ===
* drop table if exists cat_employee;
*/

-- create table cat_employee
create table cat_employee(
	emp_id serial not null,
	emp_name varchar(50) not null,
	emp_paternal_surname varchar(50) not null,
	emp_maternal_surname varchar(50) not null,
	emp_birthdate date not null,
	sex_id char(1) not null,
	constraint pk_employee
		primary key(emp_id),
	constraint un_employee_name
		unique(emp_name, emp_paternal_surname, emp_maternal_surname)
);