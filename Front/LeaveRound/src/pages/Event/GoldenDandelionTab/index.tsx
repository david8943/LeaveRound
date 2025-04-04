import React, { useEffect, useState } from 'react';
import DandelionWinners from './DandelionWinners';
import YellowDandelion from '@/assets/yellow-dandelion.png';
import useAxios from '@/hooks/useAxios';
import { TGoldRankInfo, TMyGoldRankInfo } from '@/models/ranking';
import { API } from '@/constants/url';

const DandelionEmptyState: React.FC = () => <p className=''>아직 황금 민들레를 획득한 사람이 없어요!</p>;

const GoldenDandelionTab: React.FC = () => {
  const [goldRankList, setGoldRankList] = useState<TGoldRankInfo[]>([]);
  const [myGoldRankInfo, setMyGoldRankInfo] = useState<TMyGoldRankInfo>();

  const { response, refetch } = useAxios<{
    result: {
      goldInfos: TGoldRankInfo[];
      myGoldInfo: TMyGoldRankInfo;
    };
  }>({
    url: API.event.goldDandelionRanking,
    method: 'get',
    executeOnMount: false,
  });

  useEffect(() => {
    refetch();
  }, []);

  useEffect(() => {
    if (!response?.result) return;

    setGoldRankList(response.result.goldInfos);
    setMyGoldRankInfo(response.result.myGoldInfo);
  }, [response]);

  return (
    <div className='h-full'>
      <div className='h-[calc(100%-61px)] flex flex-col justify-center items-center bg-[rgba(255,217,95,0.2)]'>
        <div className='h-[154px] mb-8 flex flex-col items-center'>
          <img src={YellowDandelion} alt='dandelion' className='w-[130px]' />
          <p className='text-detail'>황금 민들레는 한 달에 한 번 새로운 위치에 5개가 생겨나요.</p>
        </div>
        <div className='h-[180px] flex flex-col'>
          {goldRankList.length === 0 ? <DandelionEmptyState /> : <DandelionWinners winners={goldRankList} />}
        </div>
      </div>
      <div className='px-4 py-1 bg-primary rounded-sm'>
        <div className='h-[53px] flex justify-center items-center gap-4'>
          <div>{myGoldRankInfo?.myName}</div>
          <div className='flex gap-2 items-center'>
            <div>
              <img className='w-[20px] h-[20px]' src={YellowDandelion} />
            </div>
            <div className='text-detail'>
              {myGoldRankInfo?.myGoldCount === 0 ? '행운의 황금 민들레를 찾아보세요!' : 0}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default GoldenDandelionTab;
