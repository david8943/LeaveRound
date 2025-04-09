import { dandelionIcon } from '@/assets/aseets';
import { AccountDetailCard } from '@/components/Account/AccountDetailCard';
import TitleLayout from '@/components/layout/TitleLayout';
import { useState } from 'react';
import bracketsIcon from '@/assets/icons/brackets.svg';
import { useParams } from 'react-router-dom';
import useAxios from '@/hooks/useAxios';
import { API } from '@/constants/url';

// 토큰을 가져오는 함수
const getToken = () => {
  return localStorage.getItem('access_token');
};

interface AutoDonationInfo {
  autoDonationInfoId: number;
  transactionBalance: number;
  createTime: string;
  organizationName: string;
}

interface AutoDonationDetailResponse {
  isSuccess: boolean;
  code: string;
  message: string;
  result: {
    autoDonationId: number;
    bankName: string;
    acountNo: string;
    sliceMoney: string;
    donationTime: string;
    organizationName: string;
    isActive: boolean;
    totalBalance: number;
    autoDonationInfos: AutoDonationInfo[];
  };
}

export const AccountDetail = () => {
  const { autoDonationId } = useParams<{ autoDonationId: string }>();
  const [isExpanded, setIsExpanded] = useState(false);

  const { response, loading, error } = useAxios<AutoDonationDetailResponse>({
    url: API.autoDonation.detail(autoDonationId || ''),
    method: 'get',
    config: {
      headers: {
        Authorization: `Bearer ${getToken()}`,
      },
    },
    executeOnMount: true,
  });

  if (loading) {
    return <div>로딩 중...</div>;
  }
  if (error) {
    return <div>오류가 발생했습니다: {error.message}</div>;
  }
  if (!response?.result) {
    return <div>데이터를 찾을 수 없습니다.</div>;
  }

  const { autoDonationInfos = [] } = response.result;

  if (autoDonationInfos.length === 0) {
    return (
      <TitleLayout title='자동기부 상세내역'>
        <div className='p-5'>
          <div className='flex flex-col justify-center items-center mb-4'>
            <img src={dandelionIcon} alt='Dandelion Icon' className='mb-2' />
            <div className='flex items-center mt-2'>
              <span className='text-detail text-sm'>총 기부 금액</span>
            </div>
            <div className='flex items-center mt-2'>
              <span className='text-heading font-heading'>0원</span>
            </div>
          </div>
          <div className='flex justify-center items-center h-40'>
            <span>아직 기부 내역이 없습니다</span>
          </div>
        </div>
      </TitleLayout>
    );
  }

  const displayCards = isExpanded ? autoDonationInfos : autoDonationInfos.slice(0, 5);
  const totalDonationAmount = autoDonationInfos.reduce((sum, info) => sum + info.transactionBalance, 0);

  return (
    <TitleLayout title='자동기부 상세내역'>
      <div className='p-5'>
        <div className='flex flex-col justify-center items-center mb-4'>
          <img src={dandelionIcon} alt='Dandelion Icon' className='mb-2' />
          <div className='flex items-center mt-2'>
            <span className='text-detail text-sm'>총 기부 금액</span>
          </div>
          <div className='flex items-center mt-2'>
            <span className='text-heading font-heading'>{totalDonationAmount.toLocaleString()}원</span>
          </div>
        </div>

        <div className='mb-4'>
          <div className='flex flex-col items-center'>
            {displayCards.map((info) => (
              <AccountDetailCard
                key={info.autoDonationInfoId}
                accountDetailInfo={{
                  accountId: autoDonationId || '',
                  donateDate: new Date(info.createTime).toLocaleDateString(),
                  donateAmount: info.transactionBalance,
                  donatePurpose: info.organizationName,
                }}
              />
            ))}
          </div>
        </div>

        {autoDonationInfos.length > 5 && (
          <div className='flex justify-center mt-4'>
            <button onClick={() => setIsExpanded(!isExpanded)} className='flex items-center text-gray-600'>
              <img
                src={bracketsIcon}
                alt='더보기'
                className={`w-4 h-4 transform transition-transform ${isExpanded ? 'rotate-180' : ''}`}
              />
              <span className='ml-1'>{isExpanded ? '접기' : '더보기'}</span>
            </button>
          </div>
        )}
      </div>
    </TitleLayout>
  );
};
