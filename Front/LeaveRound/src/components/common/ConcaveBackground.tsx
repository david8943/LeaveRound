interface ConcaveBackgroundProps {
  className?: string;
}

export const ConcaveBackground = ({ className = '' }: ConcaveBackgroundProps) => {
  return (
    <div className={`w-[5rem] h-[3.1rem] rounded-b-full bg-[#FBF9F4] ${className}`}>
      {/* 왼쪽 부채꼴 */}
      <div className='absolute left-[-1.7rem] top-4 -translate-y-1/2 w-[2rem] h-[2rem] bg-white rounded-tr-full transition-all duration-500 ease-out transform-gpu opacity-100 scale-100 z-10' />

      {/* 왼쪽 직각 삼각형 */}
      <div className='absolute left-[-1.7rem] -top-1 scale-75 border-l-[2rem] border-b-[2rem] border-l-transparent border-background transform -rotate-90 transition-transform duration-500 ease-out' />

      {/* 오른쪽 부채꼴 */}
      <div className='absolute right-[-1.7rem] top-4 -translate-y-1/2 w-[2rem] h-[2rem] bg-white rounded-tl-full transition-all duration-500 ease-out transform-gpu opacity-100 scale-100 z-10' />

      {/* 오른쪽 직각 삼각형 */}
      <div className='absolute right-[-1.7rem] border-l-[2rem] border-b-[2rem] border-l-transparent border-background transform -rotate-180 transition-transform duration-500 ease-out' />
    </div>
  );
};
