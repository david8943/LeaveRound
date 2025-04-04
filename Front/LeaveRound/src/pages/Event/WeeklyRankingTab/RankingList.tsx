import React from 'react';
import Rank1Img from '@/assets/rank1.png';
import Rank2Img from '@/assets/rank2.png';
import Rank3Img from '@/assets/rank3.png';
import WhiteDandelion from '@/assets/white-dandelion.png';
import { TRankInfo } from '@/models/ranking';

interface PRankingList {
  users: TRankInfo[];
}

const rankImages = [Rank1Img, Rank2Img, Rank3Img];

const RankingList: React.FC<PRankingList> = ({ users }) => (
  <ul className='h-full flex flex-col justify-between'>
    {users.map((user) => (
      <li key={user.rank} className='flex justify-between items-center'>
        <div className='w-12 h-12 flex justify-center items-center'>
          {user.rank <= 3 ? (
            <img src={rankImages[user.rank - 1]} alt={`rank${user.rank}`} className='w-5 h-5' />
          ) : (
            <div>{user.rank}</div>
          )}
        </div>
        <div>{user.name}</div>
        <div className='flex w-14 gap-1'>
          <div>
            <img className='w-[20px] h-[20px]' src={WhiteDandelion} />
          </div>
          <div>{user.donateCount}</div>
        </div>
      </li>
    ))}
  </ul>
);

export default RankingList;
