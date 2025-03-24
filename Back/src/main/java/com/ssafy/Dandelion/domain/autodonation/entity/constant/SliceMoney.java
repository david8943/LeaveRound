package com.ssafy.Dandelion.domain.autodonation.entity.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SliceMoney {
	FIVE_HUNDRED(500),
	ONE_THOUSAND(1000),
	ONE_THOUSAND_FIVE_HUNDRED(1500),
	TWO_THOUSAND(2000),
	TWO_THOUSAND_FIVE_HUNDRED(2500),
	THREE_THOUSAND(3000),
	THREE_THOUSAND_FIVE_HUNDRED(3500),
	FOUR_THOUSAND(4000),
	FOUR_THOUSAND_FIVE_HUNDRED(4500),
	FIVE_THOUSAND(5000)
	;
	private final Integer sliceMoney;

}
