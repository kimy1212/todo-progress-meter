package com.kimy1212.progressmeter.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(auth -> auth
				.anyRequest().authenticated())
			.oauth2Login(Customizer.withDefaults())
			.logout(logout -> logout
				.logoutSuccessUrl("/")
				.deleteCookies("JSESSIONID"))
			.csrf(csrf -> csrf
				.ignoringRequestMatchers("/api/**"));

		return http.build();
	}

}
