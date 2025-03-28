package com.ssafy.Dandelion.global.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
// SSAFY API 관련 설정 값을 관리
public class SsafyApiProperties {

	@Value("${ssafy.api.baseUrl}")
	private String baseUrl;

	@Value("${ssafy.api.managerId}")
	private String managerId;

	@Value("${ssafy.api.key}")
	private String apiKey;

	public String createApiUrl(String endpoint) {
		return baseUrl + endpoint;
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class SsafyApiHeader {
		private String apiName;
		private String transmissionDate;
		private String transmissionTime;
		private String institutionCode;
		private String fintechAppNo;
		private String apiServiceCode;
		private String institutionTransactionUniqueNo;
		private String apiKey;
		private String userKey;

		public static SsafyApiHeader createSsafyApiHeaderTemplate(String serviceName, String apiKey, String userKey) {
			SsafyApiHeader header = new SsafyApiHeader();
			header.apiName = serviceName;
			header.apiServiceCode = serviceName;
			header.transmissionDate = getCurrentDate();
			header.transmissionTime = getCurrentTime();
			header.institutionTransactionUniqueNo = generateUniqueTransactionId();
			header.institutionCode = "00100";
			header.fintechAppNo = "001";
			header.apiKey = apiKey;
			header.userKey = userKey;

			return header;
		}

		private static String getCurrentDate() {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
			return dateFormat.format(new Date());
		}

		private static String getCurrentTime() {
			SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
			timeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
			return timeFormat.format(new Date());
		}

		private static String generateUniqueTransactionId() {
			String date = getCurrentDate();
			String time = getCurrentTime();
			String randomDigits = generateRandomDigits(6);
			return date + time + randomDigits;
		}

		private static String generateRandomDigits(int length) {
			SecureRandom random = new SecureRandom();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < length; i++) {
				sb.append(random.nextInt(10));
			}
			return sb.toString();
		}
	}
}
