package com.ssafy.Dandelion.domain.autodonation;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.Dandelion.domain.autodonation.dto.RequestDTO;
import com.ssafy.Dandelion.global.apiPayload.ApiResponse;
import com.ssafy.Dandelion.global.auth.user.CustomUserDetails;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auto-donations")
public class AutoDonationController {

	@PostMapping("")
	public ApiResponse<Void> createAutoDonation(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestBody @Valid RequestDTO.CreateAutoDonationDTO request
	) {
		return ApiResponse.onSuccess(null);
	}
}
