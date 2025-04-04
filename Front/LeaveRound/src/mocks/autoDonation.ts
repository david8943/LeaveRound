export const mockAutoDonationResponse = {
  isSuccess: true,
  code: 'COMMON200',
  message: '성공입니다.',
  result: {
    activeAccounts: [
      {
        autoDonationId: 2,
        bankName: '카카오뱅크',
        acountNo: '3333-11-1111111',
        sliceMoney: 'FIVE_HUNDRED',
        donationTime: 'ONE_DAY',
        organizationName: '싸피',
        isActive: true,
      },
      {
        autoDonationId: 4,
        bankName: '신한은행',
        acountNo: '110-333-444444',
        sliceMoney: 'ONE_THOUSAND',
        donationTime: 'ONE_WEEK',
        organizationName: '싸피',
        isActive: true,
      },
    ],
    inactiveAccounts: [
      {
        autoDonationId: 1,
        bankName: '국민은행',
        acountNo: '123-456-789012',
        sliceMoney: 'FIVE_HUNDRED',
        donationTime: 'ONE_DAY',
        organizationName: '싸피',
        isActive: false,
      },
      {
        autoDonationId: 3,
        bankName: '우리은행',
        acountNo: '1002-123-456789',
        sliceMoney: 'ONE_THOUSAND',
        donationTime: 'ONE_WEEK',
        organizationName: '싸피',
        isActive: false,
      },
    ],
  },
};

export const mockAccountResponse = {
  isSuccess: true,
  code: 'COMMON200',
  message: '성공입니다.',
  result: {
    accounts: [
      {
        accountId: 1,
        bankName: '카카오뱅크',
        accountNumber: '3333-11-1111111',
        balance: 50000,
      },
      {
        accountId: 2,
        bankName: '신한은행',
        accountNumber: '110-333-444444',
        balance: 100000,
      },
      {
        accountId: 3,
        bankName: '국민은행',
        accountNumber: '123-456-789012',
        balance: 75000,
      },
      {
        accountId: 4,
        bankName: '우리은행',
        accountNumber: '1002-123-456789',
        balance: 30000,
      },
    ],
  },
};
