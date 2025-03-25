const sampleAccountInfo = {
  userId: '1',
  accountId: '1',
  bankName: '테스트은행',
  accountNumber: '123456789012',
  balance: 1000000,
  autoDonation: 'active' as const,
};

const sampleDonationAccountInfo = {
  userId: '1',
  donationId: '1',
  bankName: '싸피은행',
  accountNumber: '987654321098',
  balance: 3487000,
  autoDonation: 'active' as const,
  paymentUnit: '10원',
  paymentFrequency: '매주',
  paymentPurpose: 'Charity Center',
  paymentAmount: 5000,
};

const sampleDonationAccountInfo2 = {
  userId: '1',
  donationId: '2',
  bankName: '국민은행',
  accountNumber: '123456789012',
  balance: 2000000,
  autoDonation: 'inactive' as const,
  paymentUnit: '100원',
  paymentFrequency: '매월',
  paymentPurpose: 'Charity Center',
  paymentAmount: 5000,
};

const sampleAccountDetailInfo = {
  userId: '1',
  donationId: '1',
  accountId: '1',
  bankName: '싸피은행',
  accountNumber: '987654321098',
  donateDate: '2024-01-01 17:00:00',
  donateAmount: 321180,
  donatePurpose: '싸피복지관',
};

export { sampleAccountInfo, sampleDonationAccountInfo, sampleAccountDetailInfo, sampleDonationAccountInfo2 };
