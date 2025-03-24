import React, { ReactNode } from 'react';
import LeftArrow from '../../assets/icons/left-arrow.svg';

interface TitleLayoutProps {
  title: string;
  children: ReactNode;
}

const TitleLayout: React.FC<TitleLayoutProps> = ({ title, children }) => {
  return (
    <div className='h-screen'>
      <div className='w-full h-[62px] relative flex items-center'>
        <div className='absolute left-[30px]'>
          <img src={LeftArrow} />
        </div>
        <div className='w-full text-center font-semibold'>{title}</div>
      </div>
      {children}
    </div>
  );
};

export default TitleLayout;
