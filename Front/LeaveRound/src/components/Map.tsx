/* eslint-disable @typescript-eslint/no-explicit-any */
import { dandelionLocation } from '@/models/dandelion';
import { useEffect, useRef } from 'react';
import WhiteDandelionImg from '@/assets/whiteDan.png';

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

const Map = ({ lat, lng, level = 2, onMapLoad, dandelions }: KakaoMapProps) => {
  const mapRef = useRef<HTMLDivElement | null>(null);

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
          disableZoom: true,
          draggable: false,
        };

        const map = new window.kakao.maps.Map(container, options);

        new window.kakao.maps.Marker({
          position: new window.kakao.maps.LatLng(lat, lng),
          map,
          title: '내 위치',
        });

        dandelions.forEach((d) => {
          new window.kakao.maps.Marker({
            position: new window.kakao.maps.LatLng(d.latitude, d.longitude),
            map,
            title: `민들레 #${d.dandelionId}`,
            image: new window.kakao.maps.MarkerImage(WhiteDandelionImg, new window.kakao.maps.Size(30, 30), {
              offset: new window.kakao.maps.Point(15, 30),
            }),
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

export default Map;
