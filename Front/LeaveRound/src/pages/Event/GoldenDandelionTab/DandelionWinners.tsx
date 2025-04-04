import { TGoldRankInfo } from '@/models/ranking';
import React from 'react';
import GoldenIcon from '@/assets/yellow-dandelion.png';

interface PDandelionWinners {
  winners: TGoldRankInfo[];
}

const DandelionWinners: React.FC<PDandelionWinners> = ({ winners }) => (
  <ul className='text-center space-y-2'>
    {winners.map((winner, index) => (
      <li key={index} className='flex justify-between w-48 mx-auto'>
        <span>{winner.name}</span>
        <img src={GoldenIcon} alt='icon' className='w-4 h-4' />
      </li>
    ))}
  </ul>
);

export default DandelionWinners;
