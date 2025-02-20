package com.jlmorab;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class ApplicationTest {

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
