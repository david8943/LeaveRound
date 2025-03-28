package com.ssafy.Dandelion.global.auth;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		// 인증 매니저 설정
		AuthenticationManager authenticationManager = authenticationManager(authenticationConfiguration);

		// 로그인 필터 생성
		LoginFilter loginFilter = new LoginFilter(authenticationManager, jwtTokenProvider, objectMapper);

		return httpSecurity
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.csrf(AbstractHttpConfigurer::disable)
			// 세션 비활성화
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			// 로그아웃 URL
			.logout(logout -> logout
				.logoutUrl("/api/users/logout")
				.addLogoutHandler(new LogoutFilter(objectMapper))
				.logoutSuccessHandler(new LogoutFilter(objectMapper))
			)
			// 요청 권한 설정
			.authorizeHttpRequests(authHttp -> authHttp
				// 회원가입, 로그인은 인증 없이 접근 가능
				.requestMatchers("/api/users", "/api/users/login").permitAll()
				// 그 외 모든 요청은 인증 필요
				.anyRequest().authenticated()
			)
			// 필터 추가
			.addFilterBefore(new ExceptionHandlerFilter(objectMapper), UsernamePasswordAuthenticationFilter.class)
			.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterAfter(new JwtAuthenticationFilter(jwtTokenProvider), LoginFilter.class)
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
