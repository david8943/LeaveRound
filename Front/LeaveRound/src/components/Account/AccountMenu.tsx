import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { AccountSettingModal } from './AccountSettingModal';
import { createPortal } from 'react-dom';
import Modal from '@/components/Modal';
import useAxios from '@/hooks/useAxios';
import { API } from '@/constants/url';

// 쿠키에서 값을 가져오는 함수
const getCookie = (name: string): string | null => {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop()?.split(';').shift() || null;
  return null;
};

interface ApiResponse {
  isSuccess: boolean;
  code: string;
  message: string;
  result: any;
}

type DonationAmount =
  | 'FIVE_HUNDRED'
  | 'ONE_THOUSAND'
  | 'ONE_THOUSAND_FIVE_HUNDRED'
  | 'TWO_THOUSAND'
  | 'TWO_THOUSAND_FIVE_HUNDRED'
  | 'THREE_THOUSAND'
  | 'THREE_THOUSAND_FIVE_HUNDRED'
  | 'FOUR_THOUSAND'
  | 'FOUR_THOUSAND_FIVE_HUNDRED'
  | 'FIVE_THOUSAND';

type DonationTime = 'ONE_DAY' | 'TWO_DAY' | 'THREE_DAY' | 'FOUR_DAY' | 'FIVE_DAY' | 'SIX_DAY' | 'SEVEN_DAY';

interface AccountMenuProps {
  onClose: () => void;
  accountNumber: string;
  userId: string;
  accountInfo: {
    bankName: string;
    balance: number;
    paymentUnit?: DonationAmount;
    paymentFrequency?: DonationTime;
    paymentPurpose?: string;
    paymentAmount?: number;
    autoDonationId?: number;
    accountNo?: string;
  };
  onModify: () => void;
  isPaused?: boolean;
  onStatusChange?: () => void;
}

export const AccountMenu = ({
  onClose,
  accountNumber,
  userId,
  accountInfo,
  onModify,
  isPaused = false,
  onStatusChange,
}: AccountMenuProps) => {
  const navigate = useNavigate();
  const [isSettingModalOpen, setIsSettingModalOpen] = useState(false);
  const [modalState, setModalState] = useState<{
    isOpen: boolean;
    mainMessage: string;
    detailMessage: string;
    isConfirmAction: boolean;
  }>({
    isOpen: false,
    mainMessage: '',
    detailMessage: '',
    isConfirmAction: false,
  });

  const { refetch: toggleActive } = useAxios<ApiResponse>({
    url: accountInfo.autoDonationId ? API.autoDonation.toggleActive(accountInfo.autoDonationId.toString()) : '',
    method: 'patch',
    executeOnMount: false,
    config: {
      headers: {
        Authorization: `Bearer ${getCookie('accessToken')}`,
      },
    },
    data: { isActive: !isPaused },
  });

  const { refetch: deleteDonation } = useAxios<ApiResponse>({
    url: accountInfo.autoDonationId ? API.autoDonation.delete(accountInfo.autoDonationId.toString()) : '',
    method: 'delete',
    executeOnMount: false,
    config: {
      headers: {
        Authorization: `Bearer ${getCookie('accessToken')}`,
      },
    },
  });

  const handleDetail = () => {
    navigate(`/${userId}/donate/${accountInfo.autoDonationId}`);
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
        isConfirmAction: true,
      });
    } else {
      setModalState({
        isOpen: true,
        mainMessage: '자동기부를 일시중지 합니다',
        detailMessage: '계좌 메뉴탭에서 기부를 다시 시작할 수 있어요',
        isConfirmAction: true,
      });
    }
  };

  const handleDelete = () => {
    setModalState({
      isOpen: true,
      mainMessage: '자동기부설정을 삭제합니다',
      detailMessage: '메인화면에서 자동기부 계좌로 다시 등록 할 수 있어요',
      isConfirmAction: false,
    });
  };

  const handleModalConfirm = async () => {
    if (!accountInfo.autoDonationId) {
      console.error('자동기부 ID가 없습니다.');
      return;
    }

    try {
      if (modalState.isConfirmAction) {
        const response = await toggleActive();
        if (response.isSuccess) {
          setModalState({ ...modalState, isOpen: false });
          onClose();
          if (onStatusChange) {
            onStatusChange();
          }
        } else {
          console.error('자동기부 상태 변경 실패:', response.message || '알 수 없는 오류가 발생했습니다.');
          alert('자동기부 상태 변경에 실패했습니다.');
        }
      } else {
        const response = await deleteDonation();
        if (response.isSuccess) {
          setModalState({ ...modalState, isOpen: false });
          onClose();
          if (onStatusChange) {
            onStatusChange();
          }
        } else {
          console.error('자동기부 삭제 실패:', response.message || '알 수 없는 오류가 발생했습니다.');
          alert('자동기부 삭제에 실패했습니다.');
        }
      }
    } catch (error: any) {
      console.error('자동기부 작업 중 오류 발생:', error?.response?.data?.message || error);
      alert('작업 처리 중 오류가 발생했습니다.');
    }
  };

  const handleModalClose = () => {
    setModalState({ ...modalState, isOpen: false });
  };

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
          onConfirmClick={modalState.isConfirmAction ? handleModalConfirm : undefined}
          confirmText={modalState.isConfirmAction ? '확인' : undefined}
        />
      )}
    </>
  );
};

const testAccountInfo = {
  bankName: '테스트은행',
  balance: 100000,
  autoDonationId: 1,
  accountNo: '1234567890',
  paymentUnit: 'ONE_THOUSAND' as DonationAmount,
  paymentFrequency: 'ONE_DAY' as DonationTime,
};
