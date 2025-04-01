package com.ssafy.Dandelion.domain.user.service;

import java.util.List;

import com.ssafy.Dandelion.domain.user.dto.UserResponseDTO;
import com.ssafy.Dandelion.domain.user.dto.request.UserLoginRequestDTO;
import com.ssafy.Dandelion.domain.user.dto.request.UserSignUpRequestDTO;
import com.ssafy.Dandelion.domain.user.dto.response.UserInfoResponseDTO;
import com.ssafy.Dandelion.domain.user.entity.User;

public interface UserService {
	// 회원가입
	void signup(UserSignUpRequestDTO userSignUpRequestDTO);

	// 로그인
	User authenticate(UserLoginRequestDTO userLoginRequestDTO);

	// 사용자 정보 조회
	UserInfoResponseDTO getUserInfo(Integer userId);

	List<UserResponseDTO.AccountDTO> readUserAllAccounts(Integer userId);
}
