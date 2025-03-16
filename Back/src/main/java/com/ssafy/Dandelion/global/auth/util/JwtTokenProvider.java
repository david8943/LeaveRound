package com.ssafy.Dandelion.global.auth.util;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.ssafy.Dandelion.global.apiPayload.code.status.ErrorStatus;
import com.ssafy.Dandelion.global.apiPayload.exception.handler.AuthHandler;
import com.ssafy.Dandelion.global.auth.user.CustomUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {

	private final Key key;
	private final int expirationTime;
	//private final UserRepository userRepository;

	public JwtTokenProvider(
		@Value("${jwt.secret}") String secretKey,
		@Value("${jwt.token-validity-in-seconds}") int expirationTime
		//UserRepository userRepository
		) {
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
		this.expirationTime = expirationTime;
		//this.userRepository = userRepository;
	}

	public ResponseCookie generateToken(Authentication authentication) {
		CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
		Claims claims = buildClaims(userDetails);
		String accessToken = createToken(claims);

		return ResponseCookie.from("access_token", accessToken)
			.httpOnly(true)
			.secure(true)
			.path("/")
			.maxAge(expirationTime)
			.sameSite("None")
			.build();
	}

	private Claims buildClaims(CustomUserDetails userDetails) {
		Claims claims = Jwts.claims().setSubject(userDetails.getEmail());
		claims.put("userId", userDetails.getUserId());
		claims.put("name", userDetails.getName());
		claims.put("email", userDetails.getEmail());
		claims.put("userKey", userDetails.getUserKey());
		return claims;
	}

	private String createToken(Claims claims) {
		return Jwts.builder()
			.setClaims(claims)
			.setExpiration(new Date(System.currentTimeMillis() + expirationTime))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	public Authentication getAuthentication(String accessToken) {
		Claims claims = parseClaims(accessToken);
		CustomUserDetails userDetails = buildUserDetails(claims);

		/*
		userRepository.findByMemberId(userDetails.getUserrId()).orElseThrow(
			() -> new AuthHandler(ErrorCode.INVALID_TOKEN)
		);
		 */

		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	private CustomUserDetails buildUserDetails(Claims claims) {
		return CustomUserDetails.builder()
			.userId(claims.get("userId", Integer.class))
			.email(claims.getSubject())
			.name(claims.get("name", String.class))
			.email(claims.get("email", String.class))
			.userKey(claims.get("userKey", String.class))
			.build();
	}

	public void validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
		} catch (JwtException e) {
			log.info("Invalid JWT Token", e);
			throw new AuthHandler(determineErrorCode(e));
		}
	}

	private ErrorStatus determineErrorCode(JwtException e) {
		if (e instanceof ExpiredJwtException) {
			return ErrorStatus.EXPIRED_TOKEN;
		}
		return ErrorStatus.INVALID_TOKEN;
	}

	private Claims parseClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	public String extractAccessToken(HttpServletRequest request) {
		return Optional.ofNullable(request.getCookies())
			.flatMap(cookies -> Optional.ofNullable(findToken(cookies)))
			.orElseThrow(() -> new AuthHandler(ErrorStatus.UNAUTHORIZED));
	}

	private String findToken(Cookie[] cookies) {
		for (Cookie cookie : cookies) {
			if ("access_token".equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}
}
