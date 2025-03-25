import React, { useState } from 'react';
import GoldenDandelionTab from './GoldenDandelionTab';
import WeeklyRankingTab from './WeeklyRankingTab';
import TitleLayout from '@/components/layout/TitleLayout';
import MagicWand from '@/assets/icons/magic-wand.svg';

interface TabHeaderProps {
  activeTab: 'golden' | 'weekly';
  onChangeTab: (tab: 'golden' | 'weekly') => void;
}

const TabHeader: React.FC<TabHeaderProps> = ({ activeTab, onChangeTab }) => {
  return (
    <div className='flex justify-around h-[50px] border-b-2 border-transparent'>
      <button
        className={`w-1/2 py-3 ${activeTab === 'weekly' ? 'border-b-2 border-black' : ''}`}
        onClick={() => onChangeTab('weekly')}
      >
        이번 주
      </button>
      <button
        className={`w-1/2 py-3 ${activeTab === 'golden' ? 'border-b-2 border-black' : ''}`}
        onClick={() => onChangeTab('golden')}
      >
        황금 민들레
      </button>
    </div>
  );
};

const DonationKingPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState<'golden' | 'weekly'>('weekly');

  return (
    <TitleLayout title='최고의 기부왕'>
      <TabHeader activeTab={activeTab} onChangeTab={setActiveTab} />
      <div className='h-[calc(100%-108px-5rem)]'>
        {activeTab === 'golden' ? <GoldenDandelionTab /> : <WeeklyRankingTab />}
      </div>
      <div className='w-full h-[58px] flex justify-center items-center'>
        <div className='flex gap-1 items-center'>
          <img src={MagicWand} className='w-4 h-4' />
          <div>기부하기</div>
        </div>
      </div>
    </TitleLayout>
  );
};

export default DonationKingPage;
