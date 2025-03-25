import TitleLayout from '@/components/layout/TitleLayout';
import { cardIcon } from '@/assets/aseets';
import { DonationAccountCard } from '@/components/Account/DonationAccountCard';
import { sampleDonationAccountInfo, sampleDonationAccountInfo2 } from '@/mocks/account';
import { useParams } from 'react-router-dom';

export const AccountDonate = () => {
  const { userId } = useParams();

  // userId가 없는 경우 처리
  if (!userId) {
    return null;
  }

  // 모든 계좌 정보를 배열로 관리
  const allAccounts = [
    sampleDonationAccountInfo,
    sampleDonationAccountInfo,
    sampleDonationAccountInfo,
    sampleDonationAccountInfo2,
    sampleDonationAccountInfo2,
  ];

  const activeAccounts = allAccounts.filter((account) => account.autoDonation === 'active');
  const inactiveAccounts = allAccounts.filter((account) => account.autoDonation === 'inactive');

  return (
    <div>
      <TitleLayout
        title='자동기부 계좌목록'
        children={
          <>
            <div className='flex flex-col justify-center items-center mb-4'>
              <img src={cardIcon} alt='Card Icon' />
              <span className='text-detail text-sm'>자동기부가 설정되어 있는 나의 계좌 목록입니다.</span>
            </div>
            {activeAccounts.length > 0 && (
              <div className='flex flex-col mt-[2rem]'>
                <span className='ml-[2rem] mb-[0.75rem]'>자동 기부중인 계좌</span>
                <div>
                  {activeAccounts.map((account) => (
                    <DonationAccountCard
                      key={account.accountNumber} // 계좌번호를 key로 사용
                      accountInfo={account}
                      id={account.accountNumber} // id도 동일한 계좌번호 사용
                    />
                  ))}
                </div>
              </div>
            )}
            {inactiveAccounts.length > 0 && (
              <div className='flex flex-col mt-[2rem]'>
                <span className='ml-[2rem] mb-[0.75rem]'>기부 일시중지된 계좌</span>
                <div>
                  {inactiveAccounts.map((account) => (
                    <DonationAccountCard
                      key={account.accountNumber} // 계좌번호를 key로 사용
                      accountInfo={account}
                      id={account.accountNumber} // id도 동일한 계좌번호 사용
                    />
                  ))}
                </div>
              </div>
            )}
          </>
        }
      />
    </div>
  );
};
