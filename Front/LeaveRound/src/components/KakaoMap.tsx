/* eslint-disable @typescript-eslint/no-explicit-any */
import { GoldDandelionLocation, WhiteDandelionLocation } from '@/models/dandelion';
import { useEffect, useRef, useState } from 'react';
import WhiteDandelionImg from '@/assets/whiteDan.png';
import GoldDandelionImg from '@/assets/goldDan.png';
import axios from '@/services/api';
import { API } from '@/constants/url';
import Modal from '@/components/Modal';
import WhiteDandelionIcon from '@/assets/white-dandelion.png';
import GoldDandelionIcon from '@/assets/yellow-dandelion.png';

declare global {
  interface Window {
    kakao: any;
  }
}

interface KakaoMapProps {
  lat: number;
  lng: number;
  onMapLoad?: (map: any) => void;
  dandelions: WhiteDandelionLocation[];
  goldDandelions: GoldDandelionLocation[];
  whiteCount: number;
  setWhiteCount: React.Dispatch<React.SetStateAction<number>>;
  goldCount: number;
  setGoldCount: React.Dispatch<React.SetStateAction<number>>;
}

const KakaoMap = ({
  lat,
  lng,
  onMapLoad,
  dandelions,
  goldDandelions,
  whiteCount,
  setWhiteCount,
  goldCount,
  setGoldCount,
}: KakaoMapProps) => {
  const [modalOpen, setModalOpen] = useState(false);
  const [modalMessage, setModalMessage] = useState({ mainMessage: '', detailMessage: '' });

  const mapRef = useRef<HTMLDivElement | null>(null);
  const markerMapRef = useRef<Map<number, any>>(new Map());

  const showModal = (mainMessage: string, detailMessage: string) => {
    setModalMessage({ mainMessage, detailMessage });
    setModalOpen(true);
  };

  const createMarker = (map: any, id: number, lat: number, lng: number, imageSrc: string, onClick: () => void) => {
    const marker = new window.kakao.maps.Marker({
      position: new window.kakao.maps.LatLng(lat, lng),
      map,
      image: new window.kakao.maps.MarkerImage(imageSrc, new window.kakao.maps.Size(30, 30), {
        offset: new window.kakao.maps.Point(15, 30),
      }),
    });
    markerMapRef.current.set(id, marker);
    window.kakao.maps.event.addListener(marker, 'click', onClick);
  };

  const handleDandelionClick = async (
    id: number,
    apiFn: (id: number) => string,
    countSetter: React.Dispatch<React.SetStateAction<number>>,
    count: number,
  ) => {
    try {
      const res = await axios.post(
        apiFn(id),
        { myLatitude: lat, myLongitude: lng },
        { headers: { 'Content-Type': 'application/json' } },
      );

      if (res.data?.isSuccess) {
        countSetter((prev) => prev + 1);

        showModal('홀씨를 획득했습니다', `지금까지 ${count + 1}개를 모았어요!`);

        const markerToRemove = markerMapRef.current.get(id);
        if (markerToRemove) {
          markerToRemove.setMap(null);
          markerMapRef.current.delete(id);
        }
      } else {
        showModal('획득 실패', res.data?.message || '알 수 없는 오류');
      }
    } catch (err) {
      console.error('민들레 획득 실패:', err);
      showModal('홀씨에 가까이 다가가 주세요', '지금 너무 멀리 있어요!');
    }
  };

  useEffect(() => {
    const scriptId = 'kakao-map-script';
    const existingScript = document.getElementById(scriptId);

    const loadScript = () => {
      return new Promise<void>((resolve) => {
        const script = document.createElement('script');
        script.id = scriptId;
        script.src = `https://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${import.meta.env.VITE_KAKAO_MAP_API_KEY}&libraries=services,clusterer`;
        script.async = true;
        script.onload = () => resolve();
        document.head.appendChild(script);
      });
    };

    const initMap = () => {
      if (!window.kakao || !mapRef.current) return;

      window.kakao.maps.load(() => {
        const container = mapRef.current;
        const map = new window.kakao.maps.Map(container, {
          center: new window.kakao.maps.LatLng(lat, lng),
          level: 3,
          draggable: false,
        });

        new window.kakao.maps.Marker({
          position: new window.kakao.maps.LatLng(lat, lng),
          map,
          title: '내 위치',
        });

        dandelions.forEach((d) => {
          createMarker(map, d.dandelionId, d.latitude, d.longitude, WhiteDandelionImg, () =>
            handleDandelionClick(d.dandelionId, API.event.getWhiteDandelion, setWhiteCount, whiteCount),
          );
        });

        goldDandelions.forEach((g) => {
          createMarker(map, g.goldDandelionId, g.latitude, g.longitude, GoldDandelionImg, () =>
            handleDandelionClick(g.goldDandelionId, API.event.getGoldDandelion, setGoldCount, goldCount),
          );
        });

        if (onMapLoad) onMapLoad(map);
      });
    };

    if (!existingScript) {
      loadScript().then(initMap);
    } else {
      initMap();
    }
  }, [lat, lng, onMapLoad, dandelions, goldDandelions]);

  return (
    <>
      <div ref={mapRef} className='h-[calc(100%-116px-58px)]' id='map'>
        <div className='absolute top-4 left-4 flex flex-col gap-2 z-[999]'>
          <div className='bg-primary-light rounded-full px-3 py-1 flex items-center gap-1 shadow'>
            <img src={WhiteDandelionIcon} className='w-4 h-4' />
            <span className='font-bold'>{whiteCount}</span>
          </div>
          <div className='bg-primary-light rounded-full px-3 py-1 flex items-center gap-1 shadow'>
            <img src={GoldDandelionIcon} className='w-4 h-4' />
            <span className='font-bold'>{goldCount}</span>
          </div>
        </div>
      </div>
      {modalOpen && (
        <Modal
          mainMessage={modalMessage.mainMessage}
          detailMessage={modalMessage.detailMessage}
          onClose={() => setModalOpen(false)}
        />
      )}
    </>
  );
};

export default KakaoMap;
