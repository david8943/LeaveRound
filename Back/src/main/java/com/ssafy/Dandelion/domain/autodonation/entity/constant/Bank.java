package com.ssafy.Dandelion.domain.autodonation.entity.constant;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Bank {
	HANKOOK("001", "한국은행"),
	SANEOB("002", "산업은행"),
	GIEOB("003", "기업은행"),
	KOOKMIN("004", "국민은행"),
	NONGHYEOB("011", "농협은행"),
	WOORI("020", "우리은행"),
	SC("023", "SC제일은행"),
	CITI("027", "시티은행"),
	DAEGU("032", "대구은행"),
	GWANGJU("034", "광주은행"),
	JEJU("035", "제주은행"),
	JEONBUK("037", "전북은행"),
	GYEONGNAM("039", "경남은행"),
	SAEMAEUL("045", "새마을금고"),
	KEB_HANA("081", "KEB하나은행"),
	SHINHAN("088", "신한은행"),
	KAKAO("090", "카카오뱅크"),
	SSAFY("999", "싸피은행");
	private final String bankCode;
	private final String bankName;

	public static Bank fromBankName(String bankName) {
		for (Bank bank : Bank.values()) {
			if (bank.getBankName().equals(bankName)) {
				return bank;
			}
		}
		return null;
	}
}
