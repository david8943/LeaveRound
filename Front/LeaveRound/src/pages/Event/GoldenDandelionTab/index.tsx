import React from 'react';
import DandelionWinners from './DandelionWinners';
import YellowDandelion from '@/assets/yellow-dandelion.png';

const DandelionEmptyState: React.FC = () => <p className=''>아직 황금 민들레를 획득한 사람이 없어요!</p>;

const dummyWinners: string[] = [];

const GoldenDandelionTab: React.FC = () => {
  return (
    <div className='h-full'>
      <div className='h-[calc(100%-61px)] flex flex-col justify-center items-center bg-[rgba(255,217,95,0.2)]'>
        <div className='h-[154px] mb-8 flex flex-col items-center'>
          <img src={YellowDandelion} alt='dandelion' className='w-[130px]' />
          <p className='text-detail'>황금 민들레는 한 달에 한 번 새로운 위치에 5개가 생겨나요.</p>
        </div>
        <div className='h-[180px] flex flex-col'>
          {dummyWinners.length === 0 ? <DandelionEmptyState /> : <DandelionWinners winners={dummyWinners} />}
        </div>
      </div>
      <div className='px-4 py-1 bg-primary rounded-sm'>
        <div className='h-[53px] flex justify-between items-center'>
          <div className='w-12 h-12 flex justify-center items-center'>
            <div>순위밖</div>
          </div>
          <div>힌*민민</div>
          <div className='flex w-14 gap-1'>
            <div>
              <img className='w-[20px] h-[20px]' src={YellowDandelion} />
            </div>
            <div>80</div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default GoldenDandelionTab;
