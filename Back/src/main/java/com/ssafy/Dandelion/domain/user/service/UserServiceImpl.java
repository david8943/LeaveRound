package com.ssafy.Dandelion.domain.user.service;

import com.ssafy.Dandelion.domain.user.dto.request.UserLoginRequestDTO;
import com.ssafy.Dandelion.domain.user.dto.request.UserSignUpRequestDTO;
import com.ssafy.Dandelion.domain.user.dto.response.UserInfoResponseDTO;
import com.ssafy.Dandelion.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    @Override
    public void signup(UserSignUpRequestDTO userSignUpRequestDTO) {

    }

    @Override
    public User authenticate(UserLoginRequestDTO userLoginRequestDTO) {
        return null;
    }

    @Override
    public UserInfoResponseDTO getUserInfo(Integer userId) {
        return null;
    }
}
