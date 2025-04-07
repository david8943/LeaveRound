/* eslint-disable @typescript-eslint/no-explicit-any */
import { dandelionLocation } from '@/models/dandelion';
import { useEffect, useRef, useState } from 'react';
import WhiteDandelionImg from '@/assets/whiteDan.png';
import axios from '@/services/api';
import { API } from '@/constants/url';
import Modal from '@/components/Modal';

declare global {
  interface Window {
    kakao: any;
  }
}

interface KakaoMapProps {
  lat: number;
  lng: number;
  level?: number;
  onMapLoad?: (map: any) => void;
  dandelions: dandelionLocation[];
}

const KakaoMap = ({ lat, lng, level = 2, onMapLoad, dandelions }: KakaoMapProps) => {
  const [modalOpen, setModalOpen] = useState(false);
  const [modalMessage, setModalMessage] = useState({
    mainMessage: '',
    detailMessage: '',
  });

  const mapRef = useRef<HTMLDivElement | null>(null);
  const markerMapRef = useRef<Map<number, any>>(new Map());

  const showModal = (mainMessage: string, detailMessage: string) => {
    setModalMessage({ mainMessage, detailMessage });
    setModalOpen(true);
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
        const options = {
          center: new window.kakao.maps.LatLng(lat, lng),
          level,
          draggable: false,
        };

        const map = new window.kakao.maps.Map(container, options);

        // 내 위치 마커
        new window.kakao.maps.Marker({
          position: new window.kakao.maps.LatLng(lat, lng),
          map,
          title: '내 위치',
        });

        // 민들레 마커 생성
        dandelions.forEach((d) => {
          const marker = new window.kakao.maps.Marker({
            position: new window.kakao.maps.LatLng(d.latitude, d.longitude),
            map,
            title: `민들레 #${d.dandelionId}`,
            image: new window.kakao.maps.MarkerImage(WhiteDandelionImg, new window.kakao.maps.Size(30, 30), {
              offset: new window.kakao.maps.Point(15, 30),
            }),
          });

          // 마커 저장
          markerMapRef.current.set(d.dandelionId, marker);

          // 클릭 이벤트 등록
          window.kakao.maps.event.addListener(marker, 'click', async () => {
            try {
              const res = await axios.post(
                API.event.getWhiteDandelion(d.dandelionId),
                {
                  myLatitude: lat,
                  myLongitude: lng,
                },
                {
                  headers: {
                    'Content-Type': 'application/json',
                  },
                },
              );

              if (res.data?.isSuccess) {
                showModal('홀씨를 획득했습니다', '고맙습니다');

                // 마커 제거
                const markerToRemove = markerMapRef.current.get(d.dandelionId);
                if (markerToRemove) {
                  markerToRemove.setMap(null);
                  markerMapRef.current.delete(d.dandelionId);
                }
              } else {
                showModal('획득 실패', res.data?.message || '알 수 없는 오류');
              }
            } catch (err) {
              console.error('민들레 획득 실패:', err);
              showModal('홀씨에 가까이 다가가 주세요', '지금 너무 멀리 있어요!');
            }
          });
        });

        if (onMapLoad) onMapLoad(map);
      });
    };

    if (!existingScript) {
      loadScript().then(initMap);
    } else {
      initMap();
    }
  }, [lat, lng, level, onMapLoad, dandelions]);

  return (
    <>
      <div ref={mapRef} className='h-[calc(100%-116px-58px)]' id='map' />
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
