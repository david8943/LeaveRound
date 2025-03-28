import AccountModal from './AccountModal';
import { BasicButton } from '../common/BasicButton';

interface SettingConfirmModalProps {
  onClose: () => void;
  settings: {
    amount: number;
    frequency: string;
    purpose: string;
  };
}

export const SettingConfirmModal = ({ onClose, settings }: SettingConfirmModalProps) => {
  const handleConfirm = () => {
    console.log('기부 설정 확인:', {
      기부금액: `${settings.amount}원`,
      기부주기: settings.frequency,
      기부처: settings.purpose,
    });
    onClose();
  };

  return (
    <AccountModal onClose={onClose}>
      <div className='flex flex-col items-center'>
        <p className='font-bold'>기부 설정을 확인해주세요</p>
        <div className='flex flex-col items-start mt-4 w-full px-6'>
          <div className='flex justify-between w-full mb-2'>
            <span className='text-gray-700'>기부 금액</span>
            <span className='font-semibold'>{settings.amount}원</span>
          </div>
          <div className='flex justify-between w-full mb-2'>
            <span className='text-gray-700'>기부 주기</span>
            <span className='font-semibold'>{settings.frequency}</span>
          </div>
          <div className='flex justify-between w-full mb-2'>
            <span className='text-gray-700'>기부처</span>
            <span className='font-semibold'>{settings.purpose}</span>
          </div>
        </div>
      </div>
      <div className='flex gap-2 mt-6'>
        <BasicButton text='취소' onClick={onClose} />
        <BasicButton text='기부하기' onClick={handleConfirm} />
      </div>
    </AccountModal>
  );
};
