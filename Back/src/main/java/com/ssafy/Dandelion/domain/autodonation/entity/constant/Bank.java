package com.ssafy.Dandelion.domain.autodonation.entity.constant;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.IntStream;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public enum Bank {
	HANKOOK("001", "한국은행", "001-1-3f424fd78ee94d"),
	SANEOB("002", "산업은행", "002-1-8168e2f2b57343"),
	GIEOB("003", "기업은행", "003-1-c65723ac8d2046"),
	KOOKMIN("004", "국민은행", "004-1-dbdfc83ef84e43"),
	NONGHYEOB("011", "농협은행", "011-1-4cdbead33e3041"),
	WOORI("020", "우리은행", "020-1-7b469345f76342"),
	SC("023", "SC제일은행", "023-1-658ed3e343f649"),
	CITI("027", "시티은행", "027-1-af1f0a6ca19643"),
	DAEGU("032", "대구은행", "032-1-4a2514f4bf964"),
	GWANGJU("034", "광주은행", "034-1-7d3cb07fdd0445"),
	JEJU("035", "제주은행", "035-1-bee86f8c75ff4d"),
	JEONBUK("037", "전북은행", "037-1-1565e4dd44db45"),
	GYEONGNAM("039", "경남은행", "039-1-034660f1c0d941"),
	SAEMAEUL("045", "새마을금고", "045-1-631b3c815e3f48"),
	KEB_HANA("081", "KEB하나은행", "081-1-68896c90e82b42"),
	SHINHAN("088", "신한은행", "088-1-d7953fc01fd14d"),
	KAKAO("090", "카카오뱅크", "090-1-ac2ce11f3db047"),
	SSAFY("999", "싸피은행", "999-1-2dad3dd6226c45");
	private final String bankCode;
	private final String bankName;
	private final String accountTypeUniqueNo;

	private static final SecureRandom RANDOM = new SecureRandom();

	public static Bank fromBankName(String bankName) {
		for (Bank bank : Bank.values()) {
			if (bank.getBankName().equals(bankName)) {
				return bank;
			}
		}
		return null;
	}

	public static Bank fromBankCode(String bankCode) {
		for (Bank bank : Bank.values()) {
			if (bank.getBankCode().equals(bankCode)) {
				return bank;
			}
		}
		return null;
	}

	public static List<Bank> getRandomBanks(int count) {
		List<Bank> bankList = List.of(Bank.values());

		return IntStream.range(0, count)
			.map(i -> RANDOM.nextInt(bankList.size()))
			.limit(count)
			.mapToObj(bankList::get)
			.toList();
	}

}
