package com.jlmorab.actuator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class DatabaseHealthIndicatorTest {

	private static final Exception TEST_EXCEPTION = new CannotGetJdbcConnectionException("test-exception");
	
	@Mock
    private JdbcTemplate template;
	
	private DatabaseHealthIndicator healthIndicator;
	
	@BeforeEach
	void setUp() {
		healthIndicator = new DatabaseHealthIndicator( template );
		reset( template );
	}//end setUp()
	
	@Test
	void healthWithExecuteQueryTrue_shouldBeUpWhenResponseQuery() throws SQLException {
		ReflectionTestUtils.setField(healthIndicator, "executeQuery", true);
		
		ResultSet rs = mock(ResultSet.class);
		
		when( rs.next() ).thenReturn( true );
		when( template.query(anyString(), any(ResultSetExtractor.class)) ).thenAnswer( invocation -> {
			ResultSetExtractor<String> extractor = invocation.getArgument(1);
			return extractor.extractData(rs);
		});
		
		Health actual = healthIndicator.health();
		
		assertNotNull( actual );
		assertEquals( Status.UP, actual.getStatus() );
		assertThat( actual.getDetails() )
			.containsKey("dbname")
			.containsKey("message");
	}//end healthWithExecuteQueryTrue_shouldBeUpWhenResponseQuery()
	
	@Test
	void healthWithExecuteQueryFalse_shouldBeUpWhenGetConnection() throws SQLException {
		ReflectionTestUtils.setField(healthIndicator, "executeQuery", false);
		
		DataSource ds = mock( DataSource.class );
		Connection cnn = mock( Connection.class );
		
		when( template.getDataSource() ).thenReturn( ds );
		when( ds.getConnection() ).thenReturn( cnn );
		
		Health actual = healthIndicator.health();
		
		assertNotNull( actual );
		assertEquals( Status.UP, actual.getStatus() );
		assertThat( actual.getDetails() )
			.containsKey("dbname")
			.containsKey("message");
	}//end healthWithExecuteQueryFalse_shouldBeUpWhenGetConnection()
	
	@Test
	void healthWithExecuteQueryTrue_shouldBeDownWhenQueryThrowException() {
		ReflectionTestUtils.setField(healthIndicator, "executeQuery", true);
		
		when( template.query(anyString(), any(ResultSetExtractor.class)) ).thenThrow( TEST_EXCEPTION );
		
		Health actual = healthIndicator.health();
		System.out.println( actual );
		
		assertNotNull( actual );
		assertEquals( Status.DOWN, actual.getStatus() );
		assertThat( actual.getDetails() )
			.containsKey("dbname")
			.containsKey("message");
	}//end healthWithExecuteQueryTrue_shouldBeDownWhenQueryThrowException()
	
	@Test
	void healthWithExecuteQueryFalse_shouldBeDownWhenGetConnection() throws SQLException {
		ReflectionTestUtils.setField(healthIndicator, "executeQuery", false);
		
		DataSource ds = mock( DataSource.class );
		
		when( template.getDataSource() ).thenReturn( ds );
		when( ds.getConnection() ).thenThrow( TEST_EXCEPTION );
		
		Health actual = healthIndicator.health();
		
		assertNotNull( actual );
		assertEquals( Status.DOWN, actual.getStatus() );
		assertThat( actual.getDetails() )
			.containsKey("dbname")
			.containsKey("message");
	}//end healthWithExecuteQueryFalse_shouldBeDownWhenGetConnection()
	
	@Test
	void healthWithExecuteQueryFalse_shouldBeDownWhenDataSourceIsNull() {
		ReflectionTestUtils.setField(healthIndicator, "executeQuery", false);
		
		when( template.getDataSource() ).thenReturn( null );
		
		Health actual = healthIndicator.health();
		
		assertNotNull( actual );
		assertEquals( Status.DOWN, actual.getStatus() );
		assertThat( actual.getDetails() )
			.containsKey("dbname")
			.containsKey("message");
	}//end healthWithExecuteQueryFalse_shouldBeDownWhenDataSourceIsNull()

}
