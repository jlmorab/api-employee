package com.jlmorab;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.jlmorab.repository.IEmployeeRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
        })
class ApplicationTest {
	
	@MockitoBean
	IEmployeeRepository employeeRepository;

	@Test
	void contextLoads() {
		log.info("Application deploy correctly");
	}//end contextLoads()
	
	@Test
	void executeSpringBootContext() {
		try ( MockedStatic<SpringApplication> mockedStatic = Mockito.mockStatic(SpringApplication.class) ) {
			assertDoesNotThrow( () -> Application.main(new String[] {}) );
			mockedStatic.verify( () -> SpringApplication.run(Application.class, new String[] {}) );
		}//end try
	}//end executeSpringBootContext()

}