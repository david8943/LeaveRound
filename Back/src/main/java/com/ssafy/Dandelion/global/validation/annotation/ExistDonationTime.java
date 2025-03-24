package com.ssafy.Dandelion.global.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ssafy.Dandelion.global.validation.validator.ExistDonationTimeValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = ExistDonationTimeValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistDonationTime {
	String message() default "일치하는 자동 기부 시간이 없습니다";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
