import React from 'react';
import RankingList from './RankingList';
import WhiteDandelion from '@/assets/white-dandelion.png';

interface User {
  rank: number;
  name: string;
  score: number;
}

const WeeklyRankingTab: React.FC = () => {
  const dummyData: User[] = [
    { rank: 1, name: '김*성', score: 360 },
    { rank: 2, name: '박*진', score: 356 },
    { rank: 3, name: '이*정', score: 354 },
    { rank: 4, name: '홍*성', score: 333 },
    { rank: 5, name: '조*수', score: 321 },
    { rank: 6, name: '김*성', score: 309 },
    { rank: 7, name: '김*진', score: 300 },
    { rank: 8, name: '이*성', score: 299 },
    { rank: 9, name: '김*성', score: 287 },
    { rank: 10, name: '이*민아', score: 286 },
  ];

  return (
    <div className='h-full flex flex-col justify-between'>
      <div className='px-4 py-2 bg-primary-light h-[calc(100%-61px)]'>
        <RankingList users={dummyData} />
      </div>
      <div className='px-4 py-1 bg-primary rounded-sm'>
        <div className='h-[53px] flex justify-between items-center'>
          <div className='w-12 h-12 flex justify-center items-center'>
            <div>순위밖</div>
          </div>
          <div>힌*민민</div>
          <div className='flex w-14 gap-1'>
            <div>
              <img className='w-[20px] h-[20px]' src={WhiteDandelion} />
            </div>
            <div>80</div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default WeeklyRankingTab;
