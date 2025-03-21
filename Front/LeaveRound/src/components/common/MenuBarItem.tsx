import { Link } from 'react-router-dom';

const TRANSITION_CLASSES = 'transition-all duration-500 ease-out transform-gpu';

interface MenuBarItemProps {
  inactiveIcon: string;
  activeIcon: string;
  alt: string;
  to: string;
  label: string;
  isActive: boolean;
}

export const MenuBarItem = ({ inactiveIcon, activeIcon, alt, to, label, isActive }: MenuBarItemProps) => {
  return (
    <Link to={to} className='relative flex-1'>
      <li className='flex flex-col items-center'>
        <div
          className={`relative flex items-center justify-center w-12 h-12 rounded-full z-30 bg-white 
            ${TRANSITION_CLASSES}
            ${isActive ? 'scale-125 -translate-y-4' : 'scale-100'}`}
        >
          <img
            className={`w-6 h-6 ${TRANSITION_CLASSES}
              ${isActive ? 'scale-125' : 'scale-100'}`}
            src={isActive ? activeIcon : inactiveIcon}
            alt={alt}
          />
        </div>

        <span
          className={`text-sm mt-0 ${TRANSITION_CLASSES}
            ${isActive ? 'text-primary font-medium' : 'text-gray-700'}`}
        >
          {label}
        </span>
      </li>
    </Link>
  );
};
