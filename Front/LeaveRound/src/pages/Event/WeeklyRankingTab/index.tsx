import React, { useEffect, useState } from 'react';
import RankingList from './RankingList';
import WhiteDandelion from '@/assets/white-dandelion.png';
import { TMyRankInfo, TRankInfo } from '@/models/ranking';
import useAxios from '@/hooks/useAxios';
import { API } from '@/constants/url';

const WeeklyRankingTab: React.FC = () => {
  const [rankList, setRankList] = useState<TRankInfo[]>([]);
  const [myRankInfo, setMyRankInfo] = useState<TMyRankInfo>();

  const { response, refetch } = useAxios<{
    result: {
      rankInfos: TRankInfo[];
      myRankInfo: TMyRankInfo;
    };
  }>({
    url: API.event.whiteDandelionRanking,
    method: 'get',
    executeOnMount: false,
  });

  useEffect(() => {
    refetch();
  }, []);

  useEffect(() => {
    if (!response?.result) return;

    setRankList(response.result.rankInfos);
    setMyRankInfo(response.result.myRankInfo);
  }, [response]);

  return (
    <div className='h-full flex flex-col justify-between'>
      <div className='px-4 py-2 bg-primary-light h-[calc(100%-61px)]'>
        <RankingList users={rankList} />
      </div>
      <div className='px-4 py-1 bg-primary rounded-sm'>
        <div className='h-[53px] flex justify-between items-center'>
          <div className='w-12 h-12 flex justify-center items-center'>
            <div>{myRankInfo && <div>{myRankInfo.myRank > 10 ? '순위밖' : myRankInfo.myRank}</div>}</div>
          </div>
          <div>{myRankInfo?.myName}</div>
          <div className='flex w-14 gap-1'>
            <div>
              <img className='w-[20px] h-[20px]' src={WhiteDandelion} />
            </div>
            <div>{myRankInfo?.myDonateCount}</div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default WeeklyRankingTab;
