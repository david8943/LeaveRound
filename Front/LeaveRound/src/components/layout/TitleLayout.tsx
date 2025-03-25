import React, { ReactNode } from 'react';
import LeftArrow from '../../assets/icons/left-arrow.svg';

interface TitleLayoutProps {
  title: string;
  children: ReactNode;
}

const TitleLayout: React.FC<TitleLayoutProps> = ({ title, children }) => {
  return (
    <div className='h-screen'>
      <div className='fixed top-0 left-0 right-0 w-full h-[62px] bg-background z-10'>
        <div className='w-full h-full relative flex items-center'>
          <div className='absolute left-[30px]'>
            <img src={LeftArrow} />
          </div>
          <div className='w-full text-center font-semibold'>{title}</div>
        </div>
      </div>

      <div className='pt-[62px] h-screen pb-[5rem] overflow-y-auto'>{children}</div>
    </div>
  );
};

export default TitleLayout;
