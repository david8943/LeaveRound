package com.ssafy.Dandelion.global.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ssafy.Dandelion.global.validation.validator.ExistBankValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = ExistBankValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistBank {
	String message() default "일치하는 은행이 단위가 없습니다";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
