import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import AddAccountIcon from '@/assets/icons/AddAccount.svg';
import ArrowRightIcon from '@/assets/icons/ArrowRight.svg';
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

const MainPage: React.FC = () => {
  const [accounts, setAccounts] = useState<Account[]>([]);

  const { response, refetch } = useAxios<{ result: Account[] }>({
    url: API.member.account,
    method: 'get',
    executeOnMount: false, // mount 시 자동 실행 방지
  });

  // 요청은 딱 한 번만 보내도록 설정
  useEffect(() => {
    refetch();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    if (response?.result) {
      setAccounts(response.result);
    }
  }, [response]);

  return (
    <div className="px-[32px] pt-[98px] pb-[95px]">
      {/* 총 기부금액 (하드코딩된 값) */}
      <p className="text-detail text-right">총 기부금액</p>
      <p className="text-[36px] text-right mt-[12px] font-heading">17,450,405 원</p>

      {/* 자동 기부 등록된 계좌 */}
      <div className="flex justify-between items-center mt-[45px]">
        <p className="text-body">자동 기부 등록된 계좌</p>
        <Link to="/manage" className="cursor-pointer ml-2">
          <img src={AddAccountIcon} alt="Add Account" />
        </Link>
        <div className="flex-grow flex justify-end">
          <Link to="/:userId/donate" className="cursor-pointer">
            <img src={ArrowRightIcon} alt="계좌 관리로 이동" />
          </Link>
        </div>
      </div>

      <div className="mt-[16px] space-y-[16px]">
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
      <p className="mt-[32px] text-body">등록되지 않은 계좌</p>
      <div className="mt-[16px] space-y-[16px]">
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
  );
};

export default MainPage;
