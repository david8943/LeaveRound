import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { AccountSettingModal } from './AccountSettingModal';
import { createPortal } from 'react-dom';

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
}

export const AccountMenu = ({ onClose, accountNumber, userId, accountInfo, onModify }: AccountMenuProps) => {
  const navigate = useNavigate();
  const [isSettingModalOpen, setIsSettingModalOpen] = useState(false);

  const handleDetail = () => {
    navigate(`/${userId}/donate/${accountNumber}`);
    onClose();
  };

  const handleModify = () => {
    onModify();
  };

  const handleStop = () => {
    console.log('기부 중지 모달 열기');
    onClose();
  };

  const handleDelete = () => {
    console.log('기부 삭제 모달 열기');
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
              기부 중지
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
    </>
  );
};
