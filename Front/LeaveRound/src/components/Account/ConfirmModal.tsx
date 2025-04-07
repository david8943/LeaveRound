import { dollarIcon } from '@/assets/aseets';
import AccountModal from './AccountModal';
import { BasicButton } from '../common/BasicButton';

interface ConfirmModalProps {
  onClose: () => void;
  onConfirm: () => Promise<void>;
  amount: number;
  frequency: string;
  purpose: string;
}

export const ConfirmModal = ({ onClose, onConfirm, amount, frequency, purpose }: ConfirmModalProps) => {
  const handleConfirm = async () => {
    await onConfirm();
  };

  return (
    <div>
      <AccountModal onClose={onClose}>
        <div className='flex flex-col items-center justify-center mt-[2rem]'>
          <p>이렇게 기부할까요?</p>
          <img src={dollarIcon} alt='달러' className='mt-[1.5rem]' />
          <div className='flex flex-col items-center justify-center mt-[2rem]'>
            <div className='flex items-center'>
              <p className='flex font-heading text-heading'>{amount}원</p>
              <p className='ml-[0.5rem]'>단위로</p>
            </div>
            <div className='flex items-center'>
              <p className='flex font-heading text-heading'>{frequency}</p>
              <p className='ml-[0.5rem]'>마다</p>
            </div>
            <div className='flex items-center'>
              <p className='flex font-heading text-heading'>{purpose}</p>
              <p className='ml-[0.5rem]'>에</p>
            </div>
          </div>
          <BasicButton text='기부하기' onClick={handleConfirm} className='mt-[2rem] w-[313px] h-[48px]' />
        </div>
      </AccountModal>
    </div>
  );
};
