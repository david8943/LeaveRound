import TitleLayout from '@/components/layout/TitleLayout';
import { cardIcon } from '@/assets/aseets';
import { DonationAccountCard } from '@/components/Account/DonationAccountCard';
import useAxios from '@/hooks/useAxios';
import { API } from '@/constants/url';
import { useState, useEffect } from 'react';
import api from '@/services/api';

// 쿠키에서 값을 가져오는 함수
const getCookie = (name: string) => {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop()?.split(';').shift();
  return null;
};

type DonationAmount =
  | 'FIVE_HUNDRED'
  | 'ONE_THOUSAND'
  | 'ONE_THOUSAND_FIVE_HUNDRED'
  | 'TWO_THOUSAND'
  | 'TWO_THOUSAND_FIVE_HUNDRED'
  | 'THREE_THOUSAND'
  | 'THREE_THOUSAND_FIVE_HUNDRED'
  | 'FOUR_THOUSAND'
  | 'FIVE_THOUSAND';

type DonationTime = 'ONE_DAY' | 'TWO_DAY' | 'THREE_DAY' | 'FOUR_DAY' | 'FIVE_DAY' | 'SIX_DAY' | 'SEVEN_DAY';

type Account = {
  autoDonationId: number | null;
  bankName: string;
  accountNo: string;
  accountMoney: string;
  accountStatus: 'AUTO_ENABLED' | 'AUTO_PAUSED' | 'AUTO_DISABLED';
};

interface AutoDonationAccount {
  autoDonationId: number;
  bankName: string;
  acountNo: string | null;
  sliceMoney: DonationAmount;
  donationTime: DonationTime;
  organizationName: string;
  isActive: boolean;
}

interface AutoDonationResponse {
  activeAccounts: AutoDonationAccount[];
  inactiveAccounts: AutoDonationAccount[];
}

export const AccountDonate = () => {
  const userId = getCookie('userId') || '';
  const [autoDonationAccounts, setAutoDonationAccounts] = useState<AutoDonationResponse>({
    activeAccounts: [],
    inactiveAccounts: [],
  });
  const [accountBalances, setAccountBalances] = useState<Record<string, number>>({});

  const { response, refetch } = useAxios<{ result: AutoDonationResponse }>({
    url: '/api/auto-donations',
    method: 'get',
    config: {
      headers: {
        Authorization: `Bearer ${getCookie('accessToken')}`,
      },
    },
    executeOnMount: false,
  });

  const { response: accountsResponse, refetch: refetchAccounts } = useAxios<{ result: Account[] }>({
    url: API.member.account,
    method: 'get',
    config: {
      headers: {
        Authorization: `Bearer ${getCookie('accessToken')}`,
      },
    },
    executeOnMount: false,
  });

  useEffect(() => {
    refetch();
    refetchAccounts();
  }, []);

  useEffect(() => {
    if (accountsResponse?.result) {
      const balances: Record<string, number> = {};
      accountsResponse.result.forEach((account) => {
        balances[account.accountNo] = Number(account.accountMoney);
      });
      setAccountBalances(balances);
    }
  }, [accountsResponse]);

  useEffect(() => {
    if (response?.result) {
      const result = response.result;
      setAutoDonationAccounts(result);
    }
  }, [response]);

  // 자동기부 상태 변경 핸들러
  const handleStatusChange = async (autoDonationId: number) => {
    // API 호출 후 즉시 상태 업데이트
    const updatedAccounts = {
      activeAccounts: autoDonationAccounts.activeAccounts.map((account) => {
        if (account.autoDonationId === autoDonationId) {
          return {
            ...account,
            isActive: !account.isActive,
          };
        }
        return account;
      }),
      inactiveAccounts: autoDonationAccounts.inactiveAccounts.map((account) => {
        if (account.autoDonationId === autoDonationId) {
          return {
            ...account,
            isActive: !account.isActive,
          };
        }
        return account;
      }),
    };

    // 활성/비활성 계좌 재분류
    const newActiveAccounts = updatedAccounts.activeAccounts.filter((account) => account.isActive);
    const newInactiveAccounts = updatedAccounts.inactiveAccounts.filter((account) => !account.isActive);

    setAutoDonationAccounts({
      activeAccounts: newActiveAccounts,
      inactiveAccounts: newInactiveAccounts,
    });

    // API 호출
    refetch();
    refetchAccounts();
  };

  // 자동기부 삭제 핸들러
  const handleDelete = async (autoDonationId: number) => {
    // API 호출은 AccountMenu에서 처리하므로 여기서는 데이터만 다시 불러옴
    refetch();
    refetchAccounts();
  };

  return (
    <TitleLayout title='자동기부 계좌목록'>
      <div className='p-5'>
        <div className='flex flex-col justify-center items-center mb-4'>
          <img src={cardIcon} alt='Card Icon' />
          <span className='text-detail text-sm'>자동기부가 설정되어 있는 나의 계좌 목록입니다.</span>
        </div>

        {/* 활성화된 계좌 섹션 */}
        <div className='mb-6'>
          <h2 className='text-lg font-semibold mb-3 ml-[1rem]'>자동 기부중인 계좌</h2>
          <div>
            {autoDonationAccounts.activeAccounts.map((account) => (
              <DonationAccountCard
                key={account.autoDonationId}
                id={account.autoDonationId.toString()}
                userId={userId}
                accountInfo={{
                  bankName: account.bankName,
                  accountNumber: account.acountNo || '',
                  balance: accountBalances[account.acountNo || ''] || 0,
                  autoDonation: 'active',
                  paymentUnit: account.sliceMoney,
                  paymentFrequency: account.donationTime,
                  paymentPurpose: account.organizationName,
                  autoDonationId: account.autoDonationId,
                }}
                onStatusChange={() => handleStatusChange(account.autoDonationId)}
                onDelete={() => handleDelete(account.autoDonationId)}
              />
            ))}
          </div>
        </div>

        {/* 비활성화된 계좌 섹션 */}
        <div>
          <h2 className='text-lg font-semibold mb-3 ml-[1rem]'>기부 일시중지된 계좌</h2>
          <div>
            {autoDonationAccounts.inactiveAccounts.map((account) => (
              <DonationAccountCard
                key={account.autoDonationId}
                id={account.autoDonationId.toString()}
                userId={userId}
                accountInfo={{
                  bankName: account.bankName,
                  accountNumber: account.acountNo || '',
                  balance: accountBalances[account.acountNo || ''] || 0,
                  autoDonation: 'inactive',
                  paymentUnit: account.sliceMoney,
                  paymentFrequency: account.donationTime,
                  paymentPurpose: account.organizationName,
                  autoDonationId: account.autoDonationId,
                }}
                onStatusChange={() => handleStatusChange(account.autoDonationId)}
                onDelete={() => handleDelete(account.autoDonationId)}
              />
            ))}
          </div>
        </div>
      </div>
    </TitleLayout>
  );
};
