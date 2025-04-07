import { useEffect, useState, useRef } from 'react';
import Title from '@/components/Title/Title';
import { PTitle } from '@/models/title';
import MagicWand from '@/assets/icons/magic-wand.svg';
import ListIcon from '@/assets/icons/list.svg';
import { useNavigate } from 'react-router-dom';
import KakaoMap from '@/components/KakaoMap';
import useAxios from '@/hooks/useAxios';
import { API } from '@/constants/url';
import { WhiteDandelionLocation, GoldDandelionLocation } from '@/models/dandelion';
import { User } from '@/models/member';

const pageTitle: PTitle = {
  title: '이벤트',
  contents: (
    <>
      민들레 홀씨를 모아 선한 영향력을 퍼뜨려 보세요. <br />
      가까이 있는 민들레를 눌러보세요.
    </>
  ),
};

const INIT_LOCATION = {
  lat: 37.5665,
  lng: 126.978,
};

const DandelionPage = () => {
  const navigate = useNavigate();
  const [whiteCount, setWhiteCount] = useState(0);
  const [goldCount, setGoldCount] = useState(0);
  const [userLocation, setUserLocation] = useState(INIT_LOCATION);
  const [dandelions, setDandelions] = useState<WhiteDandelionLocation[]>([]);
  const [goldDandelions, setGoldDandelions] = useState<GoldDandelionLocation[]>([]);
  const prevLocationRef = useRef(INIT_LOCATION);

  const { response: userInfoRes, refetch: fetchUserInfo } = useAxios<{ result: User }>({
    url: API.member.signup,
    method: 'get',
    executeOnMount: false,
  });

  const { response: whiteRes, refetch: fetchWhiteDandelions } = useAxios<{
    isSuccess: boolean;
    result: WhiteDandelionLocation[];
  }>({
    url: API.event.whiteLocation,
    method: 'post',
    executeOnMount: false,
  });

  const { response: goldRes, refetch: fetchGoldDandelions } = useAxios<{
    isSuccess: boolean;
    result: GoldDandelionLocation[];
  }>({
    url: API.event.goldLocation,
    method: 'get',
    executeOnMount: false,
  });

  // 사용자 정보 요청 및 상태 반영
  useEffect(() => {
    fetchUserInfo();
  }, []);

  useEffect(() => {
    if (userInfoRes?.result) {
      setWhiteCount(userInfoRes.result.dandelionCount);
      setGoldCount(userInfoRes.result.goldDandelionCount);
    }
  }, [userInfoRes]);

  // 위치 기반 민들레 요청
  useEffect(() => {
    if (whiteRes?.isSuccess) setDandelions(whiteRes.result);
  }, [whiteRes]);

  useEffect(() => {
    if (goldRes?.isSuccess) setGoldDandelions(goldRes.result);
  }, [goldRes]);

  // 위치 추적 및 민들레 요청
  useEffect(() => {
    const updateLocation = () => {
      navigator.geolocation.getCurrentPosition(
        ({ coords }) => {
          const newLoc = { lat: coords.latitude, lng: coords.longitude };
          const prev = prevLocationRef.current;
          const isSame = Math.abs(prev.lat - newLoc.lat) < 0.0001 && Math.abs(prev.lng - newLoc.lng) < 0.0001;

          if (!isSame) {
            prevLocationRef.current = newLoc;
            setUserLocation(newLoc);
          }
        },
        (err) => console.error('위치 정보를 가져오는 데 실패:', err),
        { enableHighAccuracy: true },
      );
    };

    updateLocation();
    const interval = setInterval(updateLocation, 5000);
    return () => clearInterval(interval);
  }, []);

  useEffect(() => {
    fetchWhiteDandelions({ myLatitude: userLocation.lat, myLongitude: userLocation.lng });
    fetchGoldDandelions();
  }, [userLocation]);

  return (
    <div className='h-[calc(100vh-5rem)]'>
      <Title {...pageTitle} />

      <KakaoMap
        lat={userLocation.lat}
        lng={userLocation.lng}
        dandelions={dandelions}
        goldDandelions={goldDandelions}
        whiteCount={whiteCount}
        setWhiteCount={setWhiteCount}
        goldCount={goldCount}
        setGoldCount={setGoldCount}
      />

      <div className='gap-20 h-[58px] flex justify-center items-center'>
        <div className='flex gap-1 items-center' onClick={() => navigate('/event/donationking')}>
          <img src={ListIcon} className='w-4 h-4' />
          <div>기부현황</div>
        </div>
        <div className='flex gap-1 items-center' onClick={() => navigate('/event/donate')}>
          <img src={MagicWand} className='w-4 h-4' />
          <div>기부하기</div>
        </div>
      </div>
    </div>
  );
};

export default DandelionPage;
