package com.ssafy.Dandelion.domain.user.service;

import static com.ssafy.Dandelion.domain.autodonation.entity.constant.Bank.getRandomBanks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.ssafy.Dandelion.domain.autodonation.entity.AutoDonation;
import com.ssafy.Dandelion.domain.autodonation.entity.constant.Bank;
import com.ssafy.Dandelion.domain.autodonation.repository.AutoDonationRepository;
import com.ssafy.Dandelion.domain.user.dto.UserRequestDTO;
import com.ssafy.Dandelion.domain.user.dto.UserResponseDTO;
import com.ssafy.Dandelion.domain.user.dto.constant.AccountStatus;
import com.ssafy.Dandelion.domain.user.dto.request.UserCreateRequestDTO;
import com.ssafy.Dandelion.domain.user.dto.request.UserLoginRequestDTO;
import com.ssafy.Dandelion.domain.user.dto.request.UserSignUpRequestDTO;
import com.ssafy.Dandelion.domain.user.dto.response.SsafyUserResponseDTO;
import com.ssafy.Dandelion.domain.user.dto.response.UserInfoResponseDTO;
import com.ssafy.Dandelion.domain.user.entity.User;
import com.ssafy.Dandelion.domain.user.repository.UserRepository;
import com.ssafy.Dandelion.global.apiPayload.code.status.ErrorStatus;
import com.ssafy.Dandelion.global.apiPayload.exception.handler.MemberHandler;
import com.ssafy.Dandelion.global.apiPayload.exception.handler.SsafyApiHandler;
import com.ssafy.Dandelion.global.config.SsafyApiProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final AutoDonationRepository autoDonationRepository;
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
	/*
	 * 회원가입 처리
	 * @param userSignUpRequestDTO 회원가입 요청 정보
	 * @throws MemberHandler 이메일이 이미 존재하는 경우 예외 발생
	 * @throws SsafyApiHandler SSAFY API 연동 실패 시 예외 발생
	 */
	public void signup(UserSignUpRequestDTO userSignUpRequestDTO) {

		if (checkUserEmail(userSignUpRequestDTO.getEmail())) {
			log.info("SSAFY API에 이미 존재하는 이메일입니다.");
			throw new MemberHandler(ErrorStatus.MEMBER_DUPLICATE_SSAFY_EMAIL);
		}

		// 이메일 중복 체크
		if (userRepository.existsByEmail(userSignUpRequestDTO.getEmail())) {
			throw new MemberHandler(ErrorStatus.MEMBER_DUPLICATE_EMAIL);
		}

		//SSAFY API를 통해 userKey 발급
		String userKey = createUser(userSignUpRequestDTO.getEmail());
		createUserAccounts(userKey);
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

	/**
	 * 로그인 처리
	 *
	 * @param userLoginRequestDTO 로그인 요청 정보
	 * @return 인증된 사용자 엔티티
	 * @throws MemberHandler 인증 실패 시 예외 발생
	 */
	@Override
	public User authenticate(UserLoginRequestDTO userLoginRequestDTO) {
		// 이메일로 사용자 조회
		User user = userRepository.findByEmail(userLoginRequestDTO.getEmail())
			.orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

		// 비밀번호 검증
		if (!passwordEncoder.matches(userLoginRequestDTO.getPassword(), user.getPassword())) {
			throw new MemberHandler(ErrorStatus.MEMBER_INVALID_PASSWORD);
		}
		return user;
	}

	@Override
	public List<UserResponseDTO.AccountDTO> readUserAllAccounts(Integer userId) {
		String userKey = getUserKeyById(userId);
		List<UserResponseDTO.AccountDTO> accountDTOS = getAccountDTOs(userKey);
		Map<String, AutoDonation> autoDonationMap = getAutoDonationMap(userId);

		updateAccountStatus(accountDTOS, autoDonationMap);

		return accountDTOS;
	}

	@Override
	public void depositUserAccount(Integer userId, UserRequestDTO.DepositAccount depositAccount) {
		String userKey = getUserKeyById(userId);

		ssafyApiProperties.depositUserAccount(userKey, depositAccount);
	}

	/**
	 * 사용자 정보 조회
	 *
	 * @param userId 조회할 사용자 ID
	 * @return 사용자 정보 DTO
	 * @throws MemberHandler 사용자가 존재하지 않는 경우 예외 발생
	 */
	@Override
	public UserInfoResponseDTO getUserInfo(Integer userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
		return UserInfoResponseDTO.fromEntity(user);
	}

	private boolean checkUserEmail(String email) {
		try {
			//API 호출을 위한 URL 및 요청 객체 생성
			String url = ssafyApiProperties.createApiUrl("/ssafy/api/v1/member/search");
			UserCreateRequestDTO requestDTO = UserCreateRequestDTO.builder()
				.apiKey(ssafyApiProperties.getApiKey())
				.userId(email)
				.build();
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			HttpEntity<UserCreateRequestDTO> requestEntity = new HttpEntity<>(requestDTO, headers);

			ResponseEntity<SsafyUserResponseDTO> response = restTemplate.exchange(
				url,
				HttpMethod.POST,
				requestEntity,
				SsafyUserResponseDTO.class
			);

			if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
				return true;
			}

			return false;
		} catch (HttpClientErrorException.BadRequest e) {
			return false;
		} catch (Exception e) {
			throw new SsafyApiHandler(ErrorStatus.SSAFY_API_CONNECTION_ERROR);
		}
	}

	/**
	 * SSAFY API를 호출하여 사용자 생성 및 userKey 발급받기
	 *
	 * @param email 사용자 이메일
	 * @return 발급받은 userKey
	 * @throws SsafyApiHandler API 호출 실패 시 예외 발생
	 */
	private String createUser(String email) {
		try {
			//API 호출을 위한 URL 및 요청 객체 생성
			String url = ssafyApiProperties.createApiUrl("/ssafy/api/v1/member/");
			UserCreateRequestDTO requestDTO = UserCreateRequestDTO.builder()
				.apiKey(ssafyApiProperties.getApiKey())
				.userId(email)
				.build();
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			HttpEntity<UserCreateRequestDTO> requestEntity = new HttpEntity<>(requestDTO, headers);

			// API 호출
			ResponseEntity<SsafyUserResponseDTO> response = restTemplate.exchange(
				url,
				HttpMethod.POST,
				requestEntity,
				SsafyUserResponseDTO.class
			);
			// 응답 처리
			if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
				return response.getBody().getUserKey();
			}
			throw new SsafyApiHandler(ErrorStatus.SSAFY_API_ERROR);
		} catch (Exception e) {
			throw new SsafyApiHandler(ErrorStatus.SSAFY_API_CONNECTION_ERROR);
		}
	}

	private void createUserAccounts(String userKey) {
		List<Bank> selectedBanks = getRandomBanks(8);

		for (Bank bank : selectedBanks) {
			createUserAccount(userKey, bank);
		}
	}

	private void createUserAccount(String userKey, Bank bank) {
		String url = ssafyApiProperties.createApiUrl("/ssafy/api/v1/edu/demandDeposit/createDemandDepositAccount");
		UserResponseDTO.AccountCreateDTO body = UserResponseDTO.AccountCreateDTO.builder()
			.header(SsafyApiProperties.SsafyApiHeader.createSsafyApiHeaderTemplate(
				"createDemandDepositAccount",
				ssafyApiProperties.getApiKey(),
				userKey
			))
			.accountTypeUniqueNo(bank.getAccountTypeUniqueNo())
			.build();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<UserResponseDTO.AccountCreateDTO> requestEntity = new HttpEntity<>(body, headers);

		restTemplate.exchange(url, HttpMethod.POST, requestEntity, void.class);

	}

	private String getUserKeyById(Integer userId) {
		String userKey = userRepository.findUserKeyByUserId(userId)
			.orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

		return userKey;
	}

	private List<UserResponseDTO.AccountDTO> getAccountDTOs(String userKey) {
		ResponseEntity<UserRequestDTO.AccountInfos> accountInfos = getUserAllAcounts(userKey);

		return accountInfos.getBody().getRec().stream()
			.map(accountInfo -> UserResponseDTO.AccountDTO.builder()
				.bankName(accountInfo.getBankName())
				.accountNo(accountInfo.getAccountNo())
				.accountMoney(accountInfo.getAccountBalance())
				.accountStatus(AccountStatus.AUTO_DISABLED.toString())
				.build())
			.toList();
	}

	private Map<String, AutoDonation> getAutoDonationMap(Integer userId) {
		List<AutoDonation> autoDonations = autoDonationRepository.findAllByUserId(userId);

		return autoDonations.stream()
			.collect(Collectors.toMap(AutoDonation::getAccountNo, Function.identity()));
	}

	private ResponseEntity<UserRequestDTO.AccountInfos> getUserAllAcounts(String userKey) {
		String url = ssafyApiProperties.createApiUrl("/ssafy/api/v1/edu/demandDeposit/inquireDemandDepositAccountList");

		Map<String, Object> body = new HashMap<>();
		body.put("Header", SsafyApiProperties.SsafyApiHeader.createSsafyApiHeaderTemplate(
			"inquireDemandDepositAccountList",
			ssafyApiProperties.getApiKey(),
			userKey
		));

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

		// API 호출

		ResponseEntity<UserRequestDTO.AccountInfos> accountInfos = restTemplate.exchange(
			url,
			HttpMethod.POST,
			requestEntity,
			UserRequestDTO.AccountInfos.class
		);
		return accountInfos;

	}

	private void updateAccountStatus(List<UserResponseDTO.AccountDTO> accountDTOS,
		Map<String, AutoDonation> autoDonationMap) {
		for (UserResponseDTO.AccountDTO accountDTO : accountDTOS) {
			AutoDonation autoDonation = autoDonationMap.get(accountDTO.getAccountNo());

			if (autoDonation != null) {
				accountDTO.setAutoDonationId(autoDonation.getAutoDonationId());
				accountDTO.setAccountStatus(autoDonation.isActive()
					? AccountStatus.AUTO_ENABLED.toString()
					: AccountStatus.AUTO_PAUSED.toString());
			}
		}
	}

}
