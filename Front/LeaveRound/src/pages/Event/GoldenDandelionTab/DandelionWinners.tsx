import React from 'react';

interface DandelionWinnersProps {
  winners: string[];
}

const DandelionWinners: React.FC<DandelionWinnersProps> = ({ winners }) => (
  <ul className='text-center space-y-2'>
    {winners.map((name, index) => (
      <li key={index} className='flex justify-between w-48 mx-auto'>
        <span>{name}</span>
        <img src='/images/dandelion.png' alt='icon' className='w-4 h-4' />
      </li>
    ))}
  </ul>
);

export default DandelionWinners;
