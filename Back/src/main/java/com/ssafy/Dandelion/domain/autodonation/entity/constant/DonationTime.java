package com.ssafy.Dandelion.domain.autodonation.entity.constant;

public enum DonationTime {
	ONE_DAY(1), TWO_DAY(2), THREE_DAY(3), FOUR_DAY(4), FIVE_DAY(5), SIX_DAY(6), SEVEN_DAY(7);

	private final int days;

	DonationTime(int days) {
		this.days = days;
	}

	public int getDays() {
		return days;
	}
}
