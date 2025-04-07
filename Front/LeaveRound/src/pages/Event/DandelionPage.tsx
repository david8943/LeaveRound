import { useEffect, useState, useRef } from 'react';
import Title from '@/components/Title/Title';
import { PTitle } from '@/models/title';
import MagicWand from '@/assets/icons/magic-wand.svg';
import ListIcon from '@/assets/icons/list.svg';
import { useNavigate } from 'react-router-dom';
import Map from '@/components/Map';

const pageTitle: PTitle = {
  title: '이벤트',
  contents: (
    <>
      민들레 홀씨를 모아 선한 영향력을 퍼뜨려 보세요. <br />
      가까이 있는 민들레를 눌러보세요.
    </>
  ),
};

const DandelionPage = () => {
  const navigate = useNavigate();
  const [_map, _setMap] = useState<any>(null);
  const [userLocation, setUserLocation] = useState<{ lat: number; lng: number } | null>(null);
  const prevLocationRef = useRef<{ lat: number; lng: number } | null>(null);

  useEffect(() => {
    const updateLocation = () => {
      navigator.geolocation.getCurrentPosition(
        (pos) => {
          const { latitude, longitude } = pos.coords;
          const newLocation = { lat: latitude, lng: longitude };

          const prev = prevLocationRef.current;

          const isSameLocation =
            prev && Math.abs(prev.lat - newLocation.lat) < 0.0001 && Math.abs(prev.lng - newLocation.lng) < 0.0001;

          if (!isSameLocation) {
            prevLocationRef.current = newLocation;
            setUserLocation(newLocation);
          }
        },
        (err) => {
          console.error('위치 정보를 가져오는 데 실패했습니다:', err);
        },
        {
          enableHighAccuracy: true,
        },
      );
    };

    updateLocation();
    const interval = setInterval(updateLocation, 5000);

    return () => clearInterval(interval);
  }, []);

  return (
    <div className='h-[calc(100vh-5rem)]'>
      <Title {...pageTitle} />
      <Map lat={userLocation?.lat || 37.5665} lng={userLocation?.lng || 126.978} />
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
