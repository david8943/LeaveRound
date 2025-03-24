import { BasicButton } from '@/components/common/BasicButton';
import Logo from '@/assets/Logo.png';
import BtnRightArrow from '@/assets/icons/btn-right-arrow.svg';
import WhiteDandelion from '@/assets/white-dandelion.png';
import YellowDandelion from '@/assets/yellow-dandelion.png';
import { useState } from 'react';

const DonatePage = () => {
  const [whiteCnt, setWhiteCnt] = useState<number>(30);
  const [yellowCnt, setYelloCnt] = useState<number>(0);

  return (
    <div className='h-[calc(100%-62px)] flex flex-col justify-center gap-20'>
      <div className='flex flex-col justify-center items-center gap-4'>
        <img className='w-[100px] h-[100px]' src={Logo} />
        <div>민들레 홀씨로 선한 영향력을 퍼뜨려 보세요.</div>
      </div>
      <div className='flex justify-center'>
        <div className='flex flex-col gap-6'>
          <div className='flex gap-4'>
            <div className='font-bold'>기부한 홀씨</div>
            <div>98개</div>
          </div>
          <div className='flex gap-4 items-center'>
            <div className='font-bold'>현재 기부처</div>
            <div>초록우산</div>
            <div>
              <button className='flex items-center justify-center gap-[4px] w-[80px] h-[30px] bg-primary-light text-detail rounded-[4px]'>
                <div className='pt-[1px]'>지정하기</div>
                <img src={BtnRightArrow} className='w-[10px] h-[10px]' />
              </button>
            </div>
          </div>
        </div>
      </div>
      <div className='flex flex-col items-center gap-6'>
        <div className='flex items-center gap-1'>
          <div>
            <img className='w-[40px]' src={WhiteDandelion} />
          </div>
          <div>
            <input
              type='number'
              placeholder={whiteCnt.toString()}
              className='placeholder-gray-400 px-1 text-center bg-transparent w-[46px] border-b-2 border-gray-600 mr-1'
            />
            <span>개</span>
          </div>
        </div>
        <div className='flex items-center gap-1'>
          <div>
            <img className='w-[40px]' src={YellowDandelion} />
          </div>
          <div>
            {yellowCnt ? (
              <div>
                <input className='bg-transparent w-[46px] border-b-2 border-gray-500 mr-1' />
                <span>개</span>
              </div>
            ) : (
              <div className='text-gray-400'>황금 민들레가 없어요!</div>
            )}
          </div>
        </div>
      </div>
      <div>
        <BasicButton className='w-[80%]' text='기부하기' />
      </div>
    </div>
  );
};

export default DonatePage;
