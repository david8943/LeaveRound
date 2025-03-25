import { dandelionIcon } from '@/assets/aseets';
import { AccountDetailCard } from '@/components/Account/AccountDetailCard';
import TitleLayout from '@/components/layout/TitleLayout';
import { sampleAccountDetailInfo } from '@/mocks/account';
import { useState } from 'react';
import bracketsIcon from '@/assets/icons/brackets.svg';

export const AccountDetail = () => {
  const [isExpanded, setIsExpanded] = useState(false);
  const formattedDonateAmount = sampleAccountDetailInfo.donateAmount.toLocaleString('ko-KR');

  // 표시할 카드 목록 생성 (API에서 받아올 데이터)
  const allCards = Array(7).fill(sampleAccountDetailInfo);
  const displayCards = isExpanded ? allCards : allCards.slice(0, 5);

  return (
    <TitleLayout
      title='계좌 기부 내역'
      children={
        <div className='flex flex-col justify-center items-center mt-[2rem]'>
          <span>
            {sampleAccountDetailInfo.bankName} {sampleAccountDetailInfo.accountNumber}
          </span>
          <img src={dandelionIcon} className='mt-[0.5rem]' />
          <span>총 기부금액</span>
          <span className='text-heading font-bold scale-125 mb-[2rem]'>{formattedDonateAmount} 원</span>
          <div className='flex flex-col items-center w-full'>
            <div className='mb-4'>
              {displayCards.map((card, index) => (
                <AccountDetailCard key={index} accountDetailInfo={card} />
              ))}
            </div>
            {allCards.length > 5 && (
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
