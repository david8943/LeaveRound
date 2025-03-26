package com.ssafy.Dandelion.domain.user.service;

import com.ssafy.Dandelion.domain.user.dto.request.UserLoginRequestDTO;
import com.ssafy.Dandelion.domain.user.dto.request.UserSignUpRequestDTO;
import com.ssafy.Dandelion.domain.user.dto.response.UserInfoResponseDTO;
import com.ssafy.Dandelion.domain.user.entity.User;
import com.ssafy.Dandelion.domain.user.repository.UserRepository;
import com.ssafy.Dandelion.global.apiPayload.code.status.ErrorStatus;
import com.ssafy.Dandelion.global.apiPayload.exception.handler.MemberHandler;
import com.ssafy.Dandelion.global.config.SsafyApiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final SsafyApiProperties ssafyApiProperties;

    /**
     * 회원가입 처리
     *
     * @param userSignUpRequestDTO 회원가입 요청 정보
     * @throws MemberHandler   이메일이 이미 존재하는 경우 예외 발생
     * @throws SsafyApiHandler SSAFY API 연동 실패 시 예외 발생
     */
    @Override
    @Transactional
    /**
     * 회원가입 처리
     * @param userSignUpRequestDTO 회원가입 요청 정보
     * @throws MemberHandler 이메일이 이미 존재하는 경우 예외 발생
     * @throws SsafyApiHandler SSAFY API 연동 실패 시 예외 발생
     */
    public void signup(UserSignUpRequestDTO userSignUpRequestDTO) {
        // 이메일 중복 체크
        if (userRepository.exitsByEmail(userSignUpRequestDTO.getEmail())) {
            throw new MemberHandler(ErrorStatus.MEMBER_DUPLICATE_EMAIL);
        }
        //SSAFY API를 통해 userKey 발급
        String userKey = createUser(userSignUpRequestDTO.getEmail());

        // 사용자 생성
        User user = User.builder()
                .name(userSignUpRequestDTO.getName())
                .email(userSignUpRequestDTO.getEmail())
                .password(passwordEncoder.encode(userSignUpRequestDTO.getPassword()))
                .userKey(userKey)
                .dandelionCount(0)
                .dandelionUseCount(0)
                .goldDandelionCount(0)
                .goldDandelionUseCount(0)
                .build();
        userRepository.save(user);
    }


    // 로그인
    @Override
    public User authenticate(UserLoginRequestDTO userLoginRequestDTO) {

        return null;
    }

    // 사용자 정보 조회
    @Override
    public UserInfoResponseDTO getUserInfo(Integer userId) {
        return null;
    }


    /**
     * SSAFY API를 호출하여 사용자 생성 및 userKey 발급받기
     *
     * @param email 사용자 이메일
     * @return 발급받은 userKey
     * @throws SsafyApiHandler API 호출 실패 시 예외 발생
     */
    private String createUser(String email) {

        return email;
    }
}
