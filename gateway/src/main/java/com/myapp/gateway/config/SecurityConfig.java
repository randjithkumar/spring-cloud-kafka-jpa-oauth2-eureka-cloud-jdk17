package com.myapp.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http.authorizeExchange(exchanges -> exchanges.pathMatchers("/actuator/**", "/eureka/**").permitAll()
				.anyExchange().authenticated()).oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
				.csrf(csrf -> csrf.disable());
		return http.build();
	}

	@Bean
	public ReactiveJwtDecoder jwtDecoder() {
		return ReactiveJwtDecoders.fromIssuerLocation("http://localhost:9999");
	}
}
