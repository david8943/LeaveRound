package com.ssafy.Dandelion.global.auth.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.Dandelion.global.apiPayload.ApiResponse;
import com.ssafy.Dandelion.global.apiPayload.code.status.ErrorStatus;
import com.ssafy.Dandelion.global.apiPayload.exception.handler.ForbiddenHandler;
import com.ssafy.Dandelion.global.auth.util.JwtTokenProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;
	private final ObjectMapper objectMapper;

	public LoginFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
		ObjectMapper objectMapper) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
		this.objectMapper = objectMapper;
		setFilterProcessesUrl("/api/users/login");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {
		logger.info("LoginFilter.attemptAuthentication");
		String email = obtainUsername(request);
		String password = obtainPassword(request);

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password, List.of());
		return authenticationManager.authenticate(token);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authResult) throws IOException, ServletException {
		// JWT 토큰을 생성하고 쿠키로 설정
		ResponseCookie cookie = jwtTokenProvider.generateToken(authResult);
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

		// 성공 응답 생성
		writeResponse(response, ApiResponse.onSuccess(null));  // 성공 시 데이터가 없다면 null
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException failed) throws IOException, ServletException {
		throw new ForbiddenHandler(ErrorStatus.INVALID_CREDENTIALS);
	}

	@Override
	protected String obtainUsername(HttpServletRequest request) {
		return request.getParameter("email");
	}

	private void writeResponse(HttpServletResponse response, ApiResponse apiResponse) throws IOException {
		writeResponse(response, apiResponse, HttpServletResponse.SC_OK);  // 기본 상태 코드: 200 OK
	}

	private void writeResponse(HttpServletResponse response, ApiResponse apiResponse, int statusCode) throws
		IOException {
		String result = objectMapper.writeValueAsString(apiResponse);
		response.setContentType("application/json");
		response.setStatus(statusCode);
		response.getWriter().write(result);
	}
}
