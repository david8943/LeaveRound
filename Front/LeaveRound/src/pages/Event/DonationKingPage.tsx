import React, { useState } from 'react';
import GoldenDandelionTab from './GoldenDandelionTab';
import WeeklyRankingTab from './WeeklyRankingTab';
import MagicWand from '@/assets/icons/magic-wand.svg';
import { useNavigate } from 'react-router-dom';

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
  const navigate = useNavigate();

  return (
    <div className='h-[calc(100%-5rem)]'>
      <TabHeader activeTab={activeTab} onChangeTab={setActiveTab} />
      <div className='h-[calc(100%-108px)] overflow-y-auto scrollbar-hide'>
        {activeTab === 'golden' ? <GoldenDandelionTab /> : <WeeklyRankingTab />}
      </div>
      <div className='w-full h-[58px] flex justify-center items-center' onClick={() => navigate('/event/donate')}>
        <div className='flex gap-1 items-center'>
          <img src={MagicWand} className='w-4 h-4' />
          <div>기부하기</div>
        </div>
      </div>
    </div>
  );
};

export default DonationKingPage;
