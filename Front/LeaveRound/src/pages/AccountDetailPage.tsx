import { dandelionIcon } from '@/assets/aseets';
import { AccountDetailCard } from '@/components/Account/AccountDetailCard';
import TitleLayout from '@/components/layout/TitleLayout';
import { useState } from 'react';
import bracketsIcon from '@/assets/icons/brackets.svg';
import { useParams } from 'react-router-dom';
import useAxios from '@/hooks/useAxios';
import { API } from '@/constants/url';
import { AutoDonationDetailResponse } from '@/types/donation';

export const AccountDetail = () => {
  const { userId, autoDonationId } = useParams<{ userId: string; autoDonationId: string }>();
  const [isExpanded, setIsExpanded] = useState(false);

  const { response, loading, error } = useAxios<AutoDonationDetailResponse>({
    url: `/${userId}${API.autoDonation.detail(autoDonationId || '')}`,
    method: 'get',
    config: {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
      },
    },
  });

  if (loading) return <div>로딩 중...</div>;
  if (error) return <div>오류가 발생했습니다.</div>;
  if (!response?.result) return <div>데이터를 찾을 수 없습니다.</div>;

  const { bankName, acountNo, totalBalance, autoDonationInfos } = response.result;
  const displayCards = isExpanded ? autoDonationInfos : autoDonationInfos.slice(0, 5);

  return (
    <TitleLayout
      title='계좌 기부 내역'
      children={
        <div className='flex flex-col justify-center items-center mt-[2rem]'>
          <span>
            {bankName} {acountNo}
          </span>
          <img src={dandelionIcon} className='mt-[0.5rem]' alt='민들레' />
          <span>총 기부금액</span>
          <span className='text-heading font-bold scale-125 mb-[2rem]'>{totalBalance.toLocaleString('ko-KR')} 원</span>
          <div className='flex flex-col items-center w-full'>
            <div className='mb-4'>
              {displayCards.map((info) => (
                <AccountDetailCard
                  key={info.autoDonationInfoId}
                  accountDetailInfo={{
                    userId: userId || '',
                    accountId: info.autoDonationInfoId.toString(),
                    donateDate: new Date(info.createTime).toLocaleDateString('ko-KR'),
                    donateAmount: info.transactionBalance,
                    donatePurpose: info.organizationName,
                  }}
                />
              ))}
            </div>
            {autoDonationInfos.length > 5 && (
              <button
                onClick={() => setIsExpanded(!isExpanded)}
                className='flex justify-center items-center w-full mb-8'
              >
                <img
                  src={bracketsIcon}
                  alt={isExpanded ? '접기' : '펼치기'}
                  className={`w-3 h-3 transition-transform ${isExpanded ? 'rotate-180' : ''}`}
                />
              </button>
            )}
          </div>
        </div>
      }
    />
  );
};
