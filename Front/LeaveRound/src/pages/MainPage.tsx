import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import AddAccountIcon from '@/assets/icons/AddAccount.svg';
import ArrowRightIcon from '@/assets/icons/ArrowRight.svg';
import LeaveRound from '@/assets/icons/Seed.svg';
import AccountPreview from '@/components/Account/AccountPreview';
import useAxios from '@/hooks/useAxios';
import { API } from '@/constants/url';
import { getBankIcon } from '@/constants/bankIconMap';

type Account = {
  autoDonationId: number | null;
  bankName: string;
  accountNo: string;
  accountMoney: string;
  accountStatus: 'AUTO_ENABLED' | 'AUTO_PAUSED' | 'AUTO_DISABLED';
};

type User = {
  name: string;
}

const MainPage: React.FC = () => {
  const [accounts, setAccounts] = useState<Account[]>([]);
  const [totalAmount, setTotalAmount] = useState<number>(0);
  const [user, setUser] = useState<User | null>(null);

  const { response: accountsResponse, refetch: refetchAccounts } = useAxios<{ result: Account[] }>({
    url: API.member.account,
    method: 'get',
    executeOnMount: false,
  });

  const { response: donationResponse, refetch: refetchTotalDonation } = useAxios<{ result: { totalAccount: number } }>({
    url: API.autoDonation.totalAmount,
    method: 'get',
    executeOnMount: false,
  });

  const { response: userResponse, refetch: refetchUser } = useAxios<{ result: User }>({
    url: API.member.info,
    method: 'get',
    executeOnMount: false,
  });

  useEffect(() => {
    refetchAccounts();
    refetchTotalDonation();
    refetchUser();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    if (accountsResponse?.result) {
      setAccounts(accountsResponse.result);
    }
  }, [accountsResponse]);

  useEffect(() => {
    if (donationResponse?.result?.totalAccount != null) {
      setTotalAmount(donationResponse.result.totalAccount);
    }
  }, [donationResponse]);

  useEffect(() => {
    if (userResponse?.result) {
      setUser(userResponse.result);
    }
  }, [userResponse]);

  return (
    <div className="relative">
      {user && (
        <div className="absolute top-[25px] w-full flex justify-center items-center">
          <img src={LeaveRound} alt="감사 아이콘" className="w-10 h-10 mr-2" />
          <span className="text-body font-medium whitespace-nowrap">
            {user.name} 님, 함께해주셔서 감사합니다.
          </span>
        </div>
      )}
      <div className='relative px-[32px] pt-[98px] pb-[95px]'>


        <p className='text-detail text-right'>총 기부금액</p>
        <p className='text-[36px] text-right mt-[12px] font-heading'>{totalAmount.toLocaleString()} 원</p>

        {/* 자동 기부 등록된 계좌 */}
        <div className='flex justify-between items-center mt-[45px]'>
          <p className='text-body'>자동 기부 등록된 계좌</p>
          <Link to='/manage' className='cursor-pointer ml-2'>
            <img src={AddAccountIcon} alt='Add Account' />
          </Link>
          <div className='flex-grow flex justify-end'>
            <Link to='/donate' className='cursor-pointer'>
              <img src={ArrowRightIcon} alt='계좌 관리로 이동' />
            </Link>
          </div>
        </div>

        <div className='mt-[16px] space-y-[16px]'>
          {accounts
            .filter((acc) => acc.accountStatus !== 'AUTO_DISABLED')
            .map((acc, idx) => (
              <AccountPreview
                key={idx}
                bankIcon={getBankIcon(acc.bankName)}
                bankName={acc.bankName}
                accountNumber={acc.accountNo}
                balance={Number(acc.accountMoney)}
                accountStatus={acc.accountStatus}
              />
            ))}
        </div>

        {/* 등록되지 않은 계좌 */}
        <p className='mt-[32px] text-body'>등록되지 않은 계좌</p>
        <div className='mt-[16px] space-y-[16px]'>
          {accounts
            .filter((acc) => acc.accountStatus === 'AUTO_DISABLED')
            .map((acc, idx) => (
              <AccountPreview
                key={idx}
                bankIcon={getBankIcon(acc.bankName)}
                bankName={acc.bankName}
                accountNumber={acc.accountNo}
                balance={Number(acc.accountMoney)}
                accountStatus={acc.accountStatus}
              />
            ))}
        </div>
      </div>
    </div>
  );
};

export default MainPage;
