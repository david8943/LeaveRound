import { donateIcon, homeIcon, eventIcon, donateActiveIcon, homeActiveIcon, eventActiveIcon } from '@/assets/aseets';
import { MenuBarItem } from '@/components/common/MenuBarItem';
import { useLocation } from 'react-router-dom';
import { useRef } from 'react';
import { ConcaveBackground } from './ConcaveBackground';

const TRANSITION_CLASSES = 'transition-all duration-500 ease-out transform-gpu';

const MENU_POSITIONS = {
  donate: '21.5%',
  home: '50%',
  event: '78.5%',
} as const;

const menuItems = [
  {
    menuId: 'donate',
    inactiveIcon: donateIcon,
    activeIcon: donateActiveIcon,
    alt: 'donate',
    to: '/donate',
    label: '기부처',
  },
  { menuId: 'home', inactiveIcon: homeIcon, activeIcon: homeActiveIcon, alt: 'home', to: '/', label: '홈' },
  {
    menuId: 'event',
    inactiveIcon: eventIcon,
    activeIcon: eventActiveIcon,
    alt: 'event',
    to: '/event',
    label: '이벤트',
  },
];

export const MenuBar = () => {
  const location = useLocation();
  const listRef = useRef<HTMLUListElement>(null);

  // 오목한 배경 요소의 위치 계산
  const getActiveItemPosition = () => {
    const activeItem = menuItems.find((item) => item.to === location.pathname);
    return activeItem ? MENU_POSITIONS[activeItem.menuId as keyof typeof MENU_POSITIONS] : MENU_POSITIONS.home;
  };

  return (
    <div className='flex w-full h-[5rem] bg-white rounded-t-3xl fixed bottom-0 left-0 right-0 shadow-[0px_-2px_10px_0px_rgba(109,107,107,0.08)]'>
      {/* 오목한 배경 요소 */}
      <div
        className={`absolute w-[5rem] h-[3rem] top-1 ${TRANSITION_CLASSES}`}
        style={{
          left: getActiveItemPosition(),
          transform: 'translateX(-50%)',
        }}
      >
        {/* 오목한 배경 */}
        <div
          className={`absolute w-[5rem] h-[3.1rem] -top-4 left-1/2 -translate-x-1/2 rounded-b-full bg-[#FBF9F4] ${TRANSITION_CLASSES} opacity-100 translate-y-3 z-0`}
        >
          <ConcaveBackground />
        </div>
      </div>

      <ul ref={listRef} className='flex justify-between items-center w-full max-w-screen-md mx-auto px-8'>
        {menuItems.map((item) => (
          <MenuBarItem key={item.menuId} {...item} isActive={location.pathname === item.to} />
        ))}
      </ul>
    </div>
  );
};
