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

// 쿠키에서 값을 가져오는 함수
const getCookie = (name: string) => {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop()?.split(';').shift();
  return null;
};

export const AccountDonate = () => {
  const { userId } = useParams();
  const [autoDonationAccounts, setAutoDonationAccounts] = useState<AutoDonationResponse>({
    activeAccounts: [],
    inactiveAccounts: [],
  });

  // 자동기부 계좌 목록 조회
  const { response: autoDonationResponse, refetch: refetchAutoDonation } = useAxios<{ result: AutoDonationResponse }>({
    url: '/api/auto-donations',
    method: 'get',
    executeOnMount: false,
    withCredentials: true,
  });

  useEffect(() => {
    refetchAutoDonation();
  }, []);

  useEffect(() => {
    if (autoDonationResponse?.result) {
      setAutoDonationAccounts(autoDonationResponse.result);
    }
  }, [autoDonationResponse]);

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
          {autoDonationAccounts.activeAccounts.length > 0 && (
            <div>
              {autoDonationAccounts.activeAccounts.map((account) => (
                <DonationAccountCard
                  key={account.autoDonationId}
                  id={account.autoDonationId.toString()}
                  accountInfo={{
                    userId: userId || '',
                    bankName: account.bankName,
                    accountNumber: account.acountNo || '',
                    balance: 0,
                    autoDonation: 'active',
                    paymentUnit: account.sliceMoney,
                    paymentFrequency: account.donationTime,
                    paymentPurpose: account.organizationName,
                    autoDonationId: account.autoDonationId,
                  }}
                  onStatusChange={refetchAutoDonation}
                />
              ))}
            </div>
          )}
        </div>

        {/* 비활성화된 계좌 섹션 */}
        <div>
          <h2 className='text-lg font-semibold mb-3 ml-[1rem]'>기부 일시중지된 계좌</h2>
          {autoDonationAccounts.inactiveAccounts.length > 0 && (
            <div>
              {autoDonationAccounts.inactiveAccounts.map((account) => (
                <DonationAccountCard
                  key={account.autoDonationId}
                  id={account.autoDonationId.toString()}
                  accountInfo={{
                    userId: userId || '',
                    bankName: account.bankName,
                    accountNumber: account.acountNo || '',
                    balance: 0,
                    autoDonation: 'inactive',
                    paymentUnit: account.sliceMoney,
                    paymentFrequency: account.donationTime,
                    paymentPurpose: account.organizationName,
                    autoDonationId: account.autoDonationId,
                  }}
                  onStatusChange={refetchAutoDonation}
                />
              ))}
            </div>
          )}
        </div>
      </div>
    </TitleLayout>
  );
};
