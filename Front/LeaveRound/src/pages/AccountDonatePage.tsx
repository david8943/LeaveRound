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

      // 로컬 스토리지에서 데이터 읽어와 업데이트
      const updatedAccounts = {
        activeAccounts: result.activeAccounts.map((account) => {
          // 로컬 스토리지에서 저장된 정보 확인
          const autoDonationKey = `autoDonation_${account.autoDonationId}`;
          const savedDataStr = localStorage.getItem(autoDonationKey);

          if (savedDataStr) {
            try {
              const savedData = JSON.parse(savedDataStr);
              // 저장된 데이터가 있으면 업데이트
              return {
                ...account,
                sliceMoney: savedData.sliceMoney || account.sliceMoney,
                donationTime: savedData.donationTime || account.donationTime,
                organizationName: savedData.purpose || account.organizationName,
              };
            } catch (error) {
              // 오류 처리
            }
          }
          return account;
        }),
        inactiveAccounts: result.inactiveAccounts.map((account) => {
          // 로컬 스토리지에서 저장된 정보 확인
          const autoDonationKey = `autoDonation_${account.autoDonationId}`;
          const savedDataStr = localStorage.getItem(autoDonationKey);

          if (savedDataStr) {
            try {
              const savedData = JSON.parse(savedDataStr);
              // 저장된 데이터가 있으면 업데이트
              return {
                ...account,
                sliceMoney: savedData.sliceMoney || account.sliceMoney,
                donationTime: savedData.donationTime || account.donationTime,
                organizationName: savedData.purpose || account.organizationName,
              };
            } catch (error) {
              // 오류 처리
            }
          }
          return account;
        }),
      };

      setAutoDonationAccounts(updatedAccounts);
    }
  }, [response]);

  // 자동기부 상태 변경 핸들러
  const handleStatusChange = async (autoDonationId: number) => {
    try {
      const toggleUrl = API.autoDonation.toggleActive(autoDonationId.toString());
      await api({
        url: toggleUrl,
        method: 'PATCH',
        headers: {
          Authorization: `Bearer ${getCookie('accessToken')}`,
        },
      });

      // 로컬 상태 업데이트
      setAutoDonationAccounts((prev) => {
        const activeAccount = prev.activeAccounts.find((acc) => acc.autoDonationId === autoDonationId);
        const inactiveAccount = prev.inactiveAccounts.find((acc) => acc.autoDonationId === autoDonationId);

        if (activeAccount) {
          // 활성 -> 비활성
          return {
            activeAccounts: prev.activeAccounts.filter((acc) => acc.autoDonationId !== autoDonationId),
            inactiveAccounts: [...prev.inactiveAccounts, { ...activeAccount, isActive: false }],
          };
        } else if (inactiveAccount) {
          // 비활성 -> 활성
          return {
            activeAccounts: [...prev.activeAccounts, { ...inactiveAccount, isActive: true }],
            inactiveAccounts: prev.inactiveAccounts.filter((acc) => acc.autoDonationId !== autoDonationId),
          };
        }
        return prev;
      });
    } catch (error) {
      // 오류 처리
    }
  };

  // 자동기부 삭제 핸들러
  const handleDelete = async (autoDonationId: number) => {
    try {
      const deleteUrl = API.autoDonation.delete(autoDonationId.toString());
      await api({
        url: deleteUrl,
        method: 'DELETE',
        headers: {
          Authorization: `Bearer ${getCookie('accessToken')}`,
        },
      });

      // 로컬 상태에서 삭제된 계좌 제거
      setAutoDonationAccounts((prev) => ({
        activeAccounts: prev.activeAccounts.filter((acc) => acc.autoDonationId !== autoDonationId),
        inactiveAccounts: prev.inactiveAccounts.filter((acc) => acc.autoDonationId !== autoDonationId),
      }));
    } catch (error) {
      // 오류 처리
    }
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
