import { donateIcon, homeIcon, eventIcon, donateActiveIcon, homeActiveIcon, eventActiveIcon } from '@/assets/aseets';
import { MenuBarItem } from '@/components/common/MenuBarItem';
import { useLocation } from 'react-router-dom';
import { useRef } from 'react';
import { ConcaveBackground } from './ConcaveBackground';

const TRANSITION_CLASSES = 'transition-all duration-500 ease-out transform-gpu';

const MENU_POSITIONS = {
  donate: '21.8%',
  home: '50%',
  event: '78.2%',
} as const;

const menuItems = [
  {
    menuId: 'donate',
    inactiveIcon: donateIcon,
    activeIcon: donateActiveIcon,
    alt: 'organization',
    to: '/organization',
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

  // 현재 경로에 따른 활성화 메뉴 아이템 확인
  const isItemActive = (itemPath: string) => {
    // AccountDetailPage와 AccountDonatePage에서는 홈 버튼 활성화
    if (itemPath === '/' && (location.pathname.includes('/donate') || location.pathname.includes('/main'))) {
      return true;
    }
    return location.pathname === itemPath;
  };

  // 오목한 배경 요소의 위치 계산
  const getActiveItemPosition = () => {
    const activeItem = menuItems.find((item) => isItemActive(item.to));
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
          <MenuBarItem key={item.menuId} {...item} isActive={isItemActive(item.to)} />
        ))}
      </ul>
    </div>
  );
};
