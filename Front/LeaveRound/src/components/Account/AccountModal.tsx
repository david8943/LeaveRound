import closeIcon from '@/assets/icons/close.svg';

interface AccountModalProps {
  onClose: () => void;
  children?: React.ReactNode;
}

const AccountModal: React.FC<AccountModalProps> = ({ onClose, children }) => {
  return (
    <div className='fixed inset-0 flex items-center justify-center bg-black bg-opacity-30'>
      <div className='relative w-[calc(100%-40px)] max-w-[373px] bg-white rounded-[24px] p-6 flex flex-col items-center justify-center mb-7'>
        <button className='absolute top-[16px] right-[18px]' onClick={onClose}>
          <img src={closeIcon} alt='닫기' width={20} height={20} />
        </button>
        {children}
      </div>
    </div>
  );
};

export default AccountModal;
