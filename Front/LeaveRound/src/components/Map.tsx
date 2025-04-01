/* eslint-disable @typescript-eslint/no-explicit-any */
import { useEffect, useRef } from 'react';

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
}

const KakaoMap = ({ lat, lng, level = 3, onMapLoad }: KakaoMapProps) => {
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
        };

        const map = new window.kakao.maps.Map(container, options);

        new window.kakao.maps.Marker({
          position: new window.kakao.maps.LatLng(lat, lng),
          map,
        });

        if (onMapLoad) onMapLoad(map);
      });
    };

    if (!existingScript) {
      loadScript().then(initMap);
    } else {
      initMap();
    }
  }, [lat, lng, level, onMapLoad]);

  return <div ref={mapRef} className='h-[calc(100%-116px-58px)]' id='map' />;
};

export default KakaoMap;
