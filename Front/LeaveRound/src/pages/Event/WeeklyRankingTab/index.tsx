import React from 'react';
import RankingList from './RankingList';

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

  const userData: User[] = [{ rank: 1237, name: '한*민', score: 80 }];

  return (
    <div className='h-full'>
      <div className='px-4 py-2 bg-primary-light'>
        <RankingList users={dummyData} />
      </div>
      <div className='px-4 py-2 bg-primary rounded-sm'>
        <RankingList users={userData} />
      </div>
    </div>
  );
};

export default WeeklyRankingTab;
