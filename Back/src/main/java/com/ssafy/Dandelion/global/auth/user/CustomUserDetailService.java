package com.ssafy.Dandelion.global.auth.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ssafy.Dandelion.domain.user.entity.User;
import com.ssafy.Dandelion.domain.user.repository.UserRepository;
import com.ssafy.Dandelion.global.apiPayload.code.status.ErrorStatus;
import com.ssafy.Dandelion.global.apiPayload.exception.handler.MemberHandler;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class CustomUserDetailService implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return userRepository.findByEmail(email)
			.map(this::createUserDetails)
			.orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
	}

	private UserDetails createUserDetails(User user) {
		return CustomUserDetails.builder()
			.userId(user.getUserId())
			.email(user.getEmail())
			.password(user.getPassword())
			.name(user.getName())
			.userKey(user.getUserKey())
			.build();
	}
}
