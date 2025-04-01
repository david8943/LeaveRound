import { PTitle } from '@/models/title';

const Title = ({ title, contents }: PTitle) => {
  return (
    <div className='pt-6 pb-4 flex flex-col items-center gap-1'>
      <div className='font-heading'>{title}</div>
      <div className='text-center'>{contents}</div>
    </div>
  );
};

export default Title;
