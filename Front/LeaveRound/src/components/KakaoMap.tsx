/* eslint-disable @typescript-eslint/no-explicit-any */
import { dandelionLocation } from '@/models/dandelion';
import { useEffect, useRef } from 'react';
import WhiteDandelionImg from '@/assets/whiteDan.png';
import axios from '@/services/api';
import { API } from '@/constants/url';

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
  const mapRef = useRef<HTMLDivElement | null>(null);
  const markerMapRef = useRef<Map<number, any>>(new Map()); // 🔑 마커 저장소

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
                alert(`민들레 #${d.dandelionId} 획득!`);

                // 지도에서 마커 제거
                const markerToRemove = markerMapRef.current.get(d.dandelionId);
                if (markerToRemove) {
                  markerToRemove.setMap(null); // 👈 지도에서 제거
                  markerMapRef.current.delete(d.dandelionId); // 메모리에서도 제거
                }
              } else {
                alert('획득 실패: ' + res.data.message);
              }
            } catch (err) {
              console.error('민들레 획득 실패:', err);
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

  return <div ref={mapRef} className='h-[calc(100%-116px-58px)]' id='map' />;
};
export default KakaoMap;
