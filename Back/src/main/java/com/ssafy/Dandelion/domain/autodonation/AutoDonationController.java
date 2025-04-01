package com.ssafy.Dandelion.domain.autodonation;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.Dandelion.domain.autodonation.dto.RequestDTO;
import com.ssafy.Dandelion.domain.autodonation.dto.ResponseDTO;
import com.ssafy.Dandelion.domain.autodonation.service.AutoDonationService;
import com.ssafy.Dandelion.global.apiPayload.ApiResponse;
import com.ssafy.Dandelion.global.auth.user.CustomUserDetails;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auto-donations")
public class AutoDonationController {

	private final AutoDonationService autoDonationService;

	@PostMapping("")
	public ApiResponse<Void> createAutoDonation(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestBody @Valid RequestDTO.AutoDonationDTO request
	) {
		autoDonationService.createAutoDonation(1, request);
		return ApiResponse.onSuccess(null);
	}

	@GetMapping("")
	public ApiResponse<ResponseDTO.ReadAllAutoDonationDTO> readAllAutoDonation(
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {
		return ApiResponse.onSuccess(autoDonationService.readAllAutoDonation(1));
	}

	@PatchMapping("/{autoDonationId}/active")
	public ApiResponse<Void> changeActive(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@PathVariable Integer autoDonationId
	) {
		autoDonationService.changeActive(1, autoDonationId);
		return ApiResponse.onSuccess(null);
	}

	@DeleteMapping("/{autoDonationId}")
	public ApiResponse<Void> deleteAutoDonation(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@PathVariable Integer autoDonationId
	) {
		autoDonationService.deleteAutoDonation(1, autoDonationId);
		return ApiResponse.onSuccess(null);
	}

	@PutMapping("/{autoDonationId}")
	public ApiResponse<Void> updateAutoDonation(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@PathVariable Integer autoDonationId,
		@RequestBody @Valid RequestDTO.AutoDonationDTO request
	) {
		autoDonationService.updateAutoDonation(1, autoDonationId, request);
		return ApiResponse.onSuccess(null);
	}

	@GetMapping("/{autoDonationId}")
	public ApiResponse<ResponseDTO.ReadAutoDonationDTO> updateAutoDonation(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@PathVariable Integer autoDonationId
	) {
		return ApiResponse.onSuccess(autoDonationService.readAutoDonation(1, autoDonationId));
	}

	@GetMapping("/user/{userId}/total")
	public ApiResponse<ResponseDTO.AutoDonationTotalAccountDTO> readTotalBalance(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@PathVariable Integer userId
	) {
		return ApiResponse.onSuccess(autoDonationService.readTotalBalance(userId));
	}
}
