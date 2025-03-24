package com.ssafy.Dandelion.global.auth;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.Dandelion.global.apiPayload.exception.handler.AuthHandler;
import com.ssafy.Dandelion.global.auth.filter.ExceptionHandlerFilter;
import com.ssafy.Dandelion.global.auth.filter.JwtAuthenticationFilter;
import com.ssafy.Dandelion.global.auth.filter.LoginFilter;
import com.ssafy.Dandelion.global.auth.filter.LogoutFilter;
import com.ssafy.Dandelion.global.auth.util.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtTokenProvider jwtTokenProvider;
	private final AuthenticationConfiguration authenticationConfiguration;
	private final ObjectMapper objectMapper;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity,
		CorsConfigurationSource corsConfigurationSource) throws Exception {
		return httpSecurity
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.csrf(AbstractHttpConfigurer::disable)
			.logout(logout -> logout
				.logoutUrl("logouturl")
				.addLogoutHandler(new LogoutFilter(objectMapper))
				.logoutSuccessHandler(new LogoutFilter(objectMapper))
			)
			.authorizeHttpRequests(
				authHttp -> authHttp
					.anyRequest().permitAll()
					/*
					.requestMatchers(
						HttpMethod.GET
					)
					.permitAll()
					.requestMatchers(
						HttpMethod.POST
					)
					.permitAll()
					.requestMatchers(
						HttpMethod.PUT
					)
					.permitAll()
					.anyRequest().authenticated()
					 */
			)
			// .sessionManagement(
			// 	sessionManagement -> sessionManagement
			// 		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			// )
			// .addFilterBefore(
			// 	new ExceptionHandlerFilter(objectMapper),
			// 	UsernamePasswordAuthenticationFilter.class
			// )
			// .addFilterBefore(
			// 	new LoginFilter(
			// 		authenticationManager(authenticationConfiguration),
			// 		jwtTokenProvider,
			// 		objectMapper
			// 	),
			// 	UsernamePasswordAuthenticationFilter.class
			// )
			// .addFilterBefore(
			// 	new JwtAuthenticationFilter(jwtTokenProvider),
			// 	UsernamePasswordAuthenticationFilter.class
			// )
			.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOrigins(Arrays.asList(
			"http://localhost:5173",
			"https://localhost:5173",
			"https://localhost:5174"
		));

		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true);
		configuration.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}

}

