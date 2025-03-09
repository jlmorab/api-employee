package com.jlmorab.actuator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseHealthIndicator implements HealthIndicator {
	
	private final JdbcTemplate template;
	
	private static final String DB_NAME = "jlmorab";
	private static final String RESPONSE_PATTERN = "The query was successfully made to the BD: %s";
	
	@Value("${app.config.actuator.db.execute-query:false}")
	boolean executeQuery;
	
	@Override
    public Health health() {
		String response;
		Map<String, Object> detail = new HashMap<>();
		detail.put("dbname", DB_NAME);
		try {
			if( executeQuery ) {
				response = template.query("SELECT 1", (ResultSet rs) -> {
					String result = "";
					if( rs.next() )
						result = String.format(RESPONSE_PATTERN, DB_NAME);
					return result;
				});
			} else {
				DataSource dataSource = template.getDataSource();
				if( dataSource == null )
					throw new NullPointerException("DataSource wasn't loaded");
				try(Connection connection = dataSource.getConnection()) {
					response = String.format(RESPONSE_PATTERN, DB_NAME);
				}//end try
			}//end if
			detail.put("message", response);
			return Health.up()
				.withDetails( detail )
				.build();
		} catch( Exception e ) {
			log.error("Error ocurred when evaluate DB health check: {}", DB_NAME, e);
			detail.put("message", e.getMessage());
			return Health.down()
				.withDetails( detail )
				.build();
		}//end try
    }

}
