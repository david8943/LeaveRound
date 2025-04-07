export type DonationAmount =
  | 'FIVE_HUNDRED'
  | 'ONE_THOUSAND'
  | 'ONE_THOUSAND_FIVE_HUNDRED'
  | 'TWO_THOUSAND'
  | 'TWO_THOUSAND_FIVE_HUNDRED'
  | 'THREE_THOUSAND'
  | 'THREE_THOUSAND_FIVE_HUNDRED'
  | 'FOUR_THOUSAND'
  | 'FOUR_THOUSAND_FIVE_HUNDRED'
  | 'FIVE_THOUSAND';

export type DonationTime = 'ONE_DAY' | 'TWO_DAY' | 'THREE_DAY' | 'FOUR_DAY' | 'FIVE_DAY' | 'SIX_DAY' | 'SEVEN_DAY';

export interface AutoDonationDetailResponse {
  isSuccess: boolean;
  code: string;
  message: string;
  result: {
    autoDonationId: number;
    bankName: string;
    acountNo: string;
    sliceMoney: DonationAmount;
    donationTime: DonationTime;
    organizationName: string;
    isActive: boolean;
    totalBalance: number;
    autoDonationInfos: Array<{
      autoDonationInfoId: number;
      transactionBalance: number;
      createTime: string;
      organizationName: string;
    }>;
  };
}
