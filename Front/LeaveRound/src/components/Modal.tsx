import closeIcon from '@/assets/icons/close.svg';

interface ModalProps {
  mainMessage: string;
  detailMessage: string;
  onClose: () => void;
  onConfirmClick?: () => Promise<void>;
  confirmText?: string;
}

const Modal: React.FC<ModalProps> = ({ mainMessage, detailMessage, onClose, onConfirmClick, confirmText = '확인' }) => {
  return (
    <div className='fixed inset-0 flex items-center justify-center bg-black bg-opacity-30'>
      <div className='relative w-[359px] h-[162px] bg-white rounded-[24px] p-6 flex flex-col items-center justify-center'>
        <button className='absolute top-[16px] right-[18px]' onClick={onClose}>
          <img src={closeIcon} alt='닫기' width={20} height={20} />
        </button>
        <p className='text-[20px] text-[#364663]'>{mainMessage}</p>
        <p className='text-detail mt-2 text-[#96A6C2]'>{detailMessage}</p>
        <button
          className='mt-[18px] w-[327px] h-[50px] bg-primary text-white rounded-[8px]'
          onClick={onConfirmClick || onClose}
        >
          {confirmText}
        </button>
      </div>
    </div>
  );
};

export default Modal;
