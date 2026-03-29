package com.kimy1212.progressmeter.infrastructure.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http,
			ClientRegistrationRepository clientRegistrationRepository) throws Exception {
		DefaultOAuth2AuthorizationRequestResolver resolver = new DefaultOAuth2AuthorizationRequestResolver(
				clientRegistrationRepository, "/oauth2/authorization");
		resolver.setAuthorizationRequestCustomizer(
				customizer -> customizer.additionalParameters(params -> params.put("prompt", "select_account")));

		http
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/login", "/error").permitAll()
						.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
						.anyRequest().authenticated())
				.oauth2Login(oauth2 -> oauth2
						.loginPage("/login")
						.defaultSuccessUrl("/", true)
						.authorizationEndpoint(endpoint -> endpoint
								.authorizationRequestResolver(resolver)))
				.logout(logout -> logout
						.logoutSuccessUrl("/login")
						.deleteCookies("JSESSIONID"))
				.headers(headers -> headers
						.httpStrictTransportSecurity(hsts -> hsts
								.includeSubDomains(true)
								.maxAgeInSeconds(31536000))
						.contentSecurityPolicy(csp -> csp
								.policyDirectives(
										"default-src 'self'; " +
										"script-src 'self'; " +
										"style-src 'self' https://fonts.googleapis.com https://cdnjs.cloudflare.com; " +
										"font-src 'self' https://fonts.gstatic.com https://cdnjs.cloudflare.com; " +
										"img-src 'self' https://*.googleusercontent.com; " +
										"connect-src 'self'; " +
										"frame-ancestors 'none'")));

		return http.build();
	}

}
