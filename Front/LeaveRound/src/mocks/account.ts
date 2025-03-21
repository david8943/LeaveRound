const sampleAccountInfo = {
  bankName: '테스트은행',
  accountNumber: '1234-5678-9012',
  balance: 1000000,
  autoDonation: 'active' as const,
};

const sampleDonationAccountInfo = {
  bankName: '싸피은행',
  accountNumber: '9876-5432-1098',
  balance: 2000000,
  autoDonation: 'inactive' as const,
  paymentUnit: '10원',
  paymentFrequency: '매주',
  paymentPurpose: 'Charity Center',
  paymentAmount: 5000,
};

const sampleAccountDetailInfo = {
  donateDate: '2024-01-01 17:00:00',
  donateAmount: 32,
  donatePurpose: '싸피복지관',
};

export { sampleAccountInfo, sampleDonationAccountInfo, sampleAccountDetailInfo };
