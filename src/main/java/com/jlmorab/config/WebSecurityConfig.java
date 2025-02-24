package com.jlmorab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

/**
 * Primary safety filter
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Bean
	SecurityFilterChain filterChain( HttpSecurity http ) throws Exception {
		return http
			.csrf( csrf -> csrf.disable() )
			.logout( logout -> logout.disable() )
			.authorizeHttpRequests(authorize -> authorize
					.anyRequest().permitAll())
			.headers( headers -> headers 
					.contentSecurityPolicy( policy -> policy 
							.policyDirectives("script-src 'self'")
							.reportOnly())
					.addHeaderWriter( new StaticHeadersWriter("Expect-CT", "max-age=3600, enforce") )
					.httpStrictTransportSecurity( hsts -> hsts 
							.maxAgeInSeconds(31622400)
							.includeSubDomains(true)
							.preload(true))
			)
			.build();
	}//end filterChain()
	
}
