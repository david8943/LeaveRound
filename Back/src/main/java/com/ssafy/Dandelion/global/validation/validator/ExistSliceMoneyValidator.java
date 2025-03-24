package com.ssafy.Dandelion.global.validation.validator;

import org.springframework.stereotype.Component;

import com.ssafy.Dandelion.domain.autodonation.entity.constant.DonationTime;
import com.ssafy.Dandelion.domain.autodonation.entity.constant.SliceMoney;
import com.ssafy.Dandelion.global.validation.annotation.ExistDonationTime;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExistSliceMoneyValidator implements ConstraintValidator<ExistDonationTime, String> {
	@Override
	public void initialize(ExistDonationTime constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
		try {
			SliceMoney.valueOf(s);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
}
