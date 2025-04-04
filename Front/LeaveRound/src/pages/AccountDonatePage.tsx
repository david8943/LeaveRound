import TitleLayout from '@/components/layout/TitleLayout';
import { cardIcon } from '@/assets/aseets';
import { DonationAccountCard } from '@/components/Account/DonationAccountCard';
import { useParams } from 'react-router-dom';
import useAxios from '@/hooks/useAxios';
import { API } from '@/constants/url';
import { useState, useEffect } from 'react';

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
  const { userId } = useParams();
  const [autoDonationAccounts, setAutoDonationAccounts] = useState<AutoDonationResponse>({
    activeAccounts: [
      {
        autoDonationId: 1,
        bankName: '신한은행',
        acountNo: '110-123-456789',
        sliceMoney: 'ONE_THOUSAND',
        donationTime: 'ONE_DAY',
        organizationName: '초록우산 어린이재단',
        isActive: true,
      },
      {
        autoDonationId: 2,
        bankName: '국민은행',
        acountNo: '123-45-6789012',
        sliceMoney: 'TWO_THOUSAND_FIVE_HUNDRED',
        donationTime: 'THREE_DAY',
        organizationName: '세이브더칠드런',
        isActive: true,
      },
    ],
    inactiveAccounts: [
      {
        autoDonationId: 3,
        bankName: '우리은행',
        acountNo: '1002-123-456789',
        sliceMoney: 'FIVE_THOUSAND',
        donationTime: 'SEVEN_DAY',
        organizationName: '유니세프',
        isActive: false,
      },
    ],
  });

  const { response, refetch } = useAxios<{ result: AutoDonationResponse }>({
    url: API.autoDonation.list(userId || ''),
    method: 'get',
    executeOnMount: false,
  });

  // 실제 API 호출 대신 목데이터 사용
  useEffect(() => {
    if (process.env.NODE_ENV === 'development') {
      // 개발 환경에서는 API 호출하지 않음
      return;
    }
    if (userId) {
      refetch();
    }
  }, [userId, refetch]);

  // 실제 API 응답 처리
  useEffect(() => {
    if (process.env.NODE_ENV === 'development') {
      // 개발 환경에서는 목데이터 유지
      return;
    }
    if (response?.result) {
      setAutoDonationAccounts(response.result);
    }
  }, [response]);

  // 자동기부 상태 변경 핸들러 (목데이터용)
  const handleStatusChange = (autoDonationId: number) => {
    setAutoDonationAccounts((prev) => {
      const activeAccount = prev.activeAccounts.find((acc) => acc.autoDonationId === autoDonationId);
      const inactiveAccount = prev.inactiveAccounts.find((acc) => acc.autoDonationId === autoDonationId);

      if (activeAccount) {
        return {
          activeAccounts: prev.activeAccounts.filter((acc) => acc.autoDonationId !== autoDonationId),
          inactiveAccounts: [...prev.inactiveAccounts, { ...activeAccount, isActive: false }],
        };
      } else if (inactiveAccount) {
        return {
          activeAccounts: [...prev.activeAccounts, { ...inactiveAccount, isActive: true }],
          inactiveAccounts: prev.inactiveAccounts.filter((acc) => acc.autoDonationId !== autoDonationId),
        };
      }
      return prev;
    });
  };

  // 자동기부 삭제 핸들러 (목데이터용)
  const handleDelete = (autoDonationId: number) => {
    setAutoDonationAccounts((prev) => ({
      activeAccounts: prev.activeAccounts.filter((acc) => acc.autoDonationId !== autoDonationId),
      inactiveAccounts: prev.inactiveAccounts.filter((acc) => acc.autoDonationId !== autoDonationId),
    }));
  };

  return (
    <TitleLayout title='자동기부 계좌목록'>
      <div className='p-5'>
        <div className='flex flex-col justify-center items-center mb-4'>
          <img src={cardIcon} alt='Card Icon' />
          <span className='text-detail text-sm'>자동기부가 설정되어 있는 나의 계좌 목록입니다.</span>
        </div>

        {/* 활성화된 계좌 섹션 */}
        {autoDonationAccounts.activeAccounts.length > 0 && (
          <div className='mb-6'>
            <h2 className='text-lg font-semibold mb-3 ml-[1rem]'>자동 기부중인 계좌</h2>
            <div>
              {autoDonationAccounts.activeAccounts.map((account) => (
                <DonationAccountCard
                  key={account.autoDonationId}
                  id={account.autoDonationId.toString()}
                  accountInfo={{
                    userId: userId || '',
                    bankName: account.bankName,
                    accountNumber: account.acountNo || '',
                    balance: 100000,
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
        )}

        {/* 비활성화된 계좌 섹션 */}
        {autoDonationAccounts.inactiveAccounts.length > 0 && (
          <div>
            <h2 className='text-lg font-semibold mb-3 ml-[1rem]'>기부 일시중지된 계좌</h2>
            <div>
              {autoDonationAccounts.inactiveAccounts.map((account) => (
                <DonationAccountCard
                  key={account.autoDonationId}
                  id={account.autoDonationId.toString()}
                  accountInfo={{
                    userId: userId || '',
                    bankName: account.bankName,
                    accountNumber: account.acountNo || '',
                    balance: 100000,
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
        )}
      </div>
    </TitleLayout>
  );
};
