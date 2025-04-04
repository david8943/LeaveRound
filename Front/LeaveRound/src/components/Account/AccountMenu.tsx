import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { AccountSettingModal } from './AccountSettingModal';
import { createPortal } from 'react-dom';
import Modal from '@/components/Modal';

interface AccountMenuProps {
  onClose: () => void;
  accountNumber: string;
  userId: string;
  accountInfo: {
    bankName: string;
    balance: number;
    paymentUnit?: string;
    paymentFrequency?: string;
    paymentPurpose?: string;
    paymentAmount?: number;
  };
  onModify: () => void;
  isPaused?: boolean;
}

export const AccountMenu = ({
  onClose,
  accountNumber,
  userId,
  accountInfo,
  onModify,
  isPaused = false,
}: AccountMenuProps) => {
  const navigate = useNavigate();
  const [isSettingModalOpen, setIsSettingModalOpen] = useState(false);
  const [modalState, setModalState] = useState<{
    isOpen: boolean;
    mainMessage: string;
    detailMessage: string;
  }>({
    isOpen: false,
    mainMessage: '',
    detailMessage: '',
  });

  const handleDetail = () => {
    navigate(`/${userId}/donate/${accountNumber}`);
    onClose();
  };

  const handleModify = () => {
    onModify();
  };

  const handleStop = () => {
    if (isPaused) {
      setModalState({
        isOpen: true,
        mainMessage: '자동기부를 다시 시작 합니다',
        detailMessage: '계좌 메뉴탭에서 기부를 다시 중지할 수 있어요',
      });
    } else {
      setModalState({
        isOpen: true,
        mainMessage: '자동기부를 일시중지 합니다',
        detailMessage: '계좌 메뉴탭에서 기부를 다시 시작할 수 있어요',
      });
    }
  };

  const handleDelete = () => {
    setModalState({
      isOpen: true,
      mainMessage: '자동기부설정을 삭제합니다',
      detailMessage: '메인화면에서 자동기부 계좌로 다시 등록 할 수 있어요',
    });
  };

  const handleModalClose = () => {
    setModalState({ ...modalState, isOpen: false });
    onClose();
  };

  // 버튼 호버 공통 스타일 변수
  const menuItemClass =
    'w-full py-2 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white transition-colors duration-200 rounded-lg';

  return (
    <>
      <div className='flex flex-col justify-center w-[6rem] rounded-lg bg-white bg-opacity-97 shadow-[0_0_4.5px_rgba(214,214,214,0.10)] z-50'>
        <ul className='flex flex-col items-center justify-center text-gray-700 text-detail'>
          <li className='w-full'>
            <button className={menuItemClass} onClick={handleDetail}>
              상세 내역
            </button>
          </li>
          <li className='w-full'>
            <button className={menuItemClass} onClick={handleModify}>
              기부 수정
            </button>
          </li>
          <li className='w-full'>
            <button className={menuItemClass} onClick={handleStop}>
              {isPaused ? '기부 시작' : '기부 중지'}
            </button>
          </li>
          <li className='w-full'>
            <button className={`${menuItemClass} text-complementary-alert`} onClick={handleDelete}>
              기부 삭제
            </button>
          </li>
        </ul>
      </div>
      {isSettingModalOpen &&
        createPortal(
          <AccountSettingModal onClose={() => setIsSettingModalOpen(false)} accountInfo={accountInfo} />,
          document.body,
        )}
      {modalState.isOpen && (
        <Modal
          mainMessage={modalState.mainMessage}
          detailMessage={modalState.detailMessage}
          onClose={handleModalClose}
        />
      )}
    </>
  );
};
