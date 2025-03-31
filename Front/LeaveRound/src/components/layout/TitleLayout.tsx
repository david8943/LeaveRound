import React, { ReactNode } from 'react';
import LeftArrow from '../../assets/icons/left-arrow.svg';

interface TitleLayoutProps {
  title: string;
  children: ReactNode;
}

const TitleLayout: React.FC<TitleLayoutProps> = ({ title, children }) => {
  return (
    <div className='h-screen'>
      <div className='w-full h-[62px] bg-background z-10'>
        <div className='w-full h-full relative flex items-center'>
          <div className='absolute left-[30px]'>
            <img src={LeftArrow} />
          </div>
          <div className='w-full text-center font-semibold'>{title}</div>
        </div>
      </div>
      <div className='h-[calc(100%-5rem)] overflow-y-auto scrollbar-hide'>{children}</div>
    </div>
  );
};

export default TitleLayout;
