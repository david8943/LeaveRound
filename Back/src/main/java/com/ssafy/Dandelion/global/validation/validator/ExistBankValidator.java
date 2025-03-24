package com.ssafy.Dandelion.global.validation.validator;

import org.springframework.stereotype.Component;

import com.ssafy.Dandelion.domain.autodonation.entity.constant.Bank;
import com.ssafy.Dandelion.domain.autodonation.entity.constant.DonationTime;
import com.ssafy.Dandelion.global.validation.annotation.ExistBank;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExistBankValidator implements ConstraintValidator<ExistBank, String> {


	@Override
	public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
		try {
			Bank.valueOf(s);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
}
