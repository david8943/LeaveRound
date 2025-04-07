import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { AccountSettingModal } from './AccountSettingModal';
import { createPortal } from 'react-dom';
import Modal from '@/components/Modal';
import useAxios from '@/hooks/useAxios';
import { API } from '@/constants/url';

type DonationAmount =
  | 'FIVE_HUNDRED'
  | 'ONE_THOUSAND'
  | 'ONE_THOUSAND_FIVE_HUNDRED'
  | 'TWO_THOUSAND'
  | 'TWO_THOUSAND_FIVE_HUNDRED'
  | 'THREE_THOUSAND'
  | 'THREE_THOUSAND_FIVE_HUNDRED'
  | 'FOUR_THOUSAND'
  | 'FIVE_THOUSAND';

type DonationTime = 'ONE_DAY' | 'TWO_DAY' | 'THREE_DAY' | 'FOUR_DAY' | 'FIVE_DAY' | 'SIX_DAY' | 'SEVEN_DAY';

interface ApiResponse {
  isSuccess: boolean;
  code: string;
  message: string;
  result: null;
}

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

export const AccountMenu = ({ onClose, accountInfo, onModify, isPaused = false, onStatusChange }: AccountMenuProps) => {
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

  const {
    refetch: toggleActive,
    loading: toggleLoading,
    error: toggleError,
  } = useAxios<ApiResponse>({
    url: accountInfo.autoDonationId ? API.autoDonation.toggleActive(accountInfo.autoDonationId.toString()) : '',
    method: 'patch',
    executeOnMount: false,
  });

  const {
    refetch: deleteDonation,
    loading: deleteLoading,
    error: deleteError,
  } = useAxios<ApiResponse>({
    url: accountInfo.autoDonationId ? API.autoDonation.delete(accountInfo.autoDonationId.toString()) : '',
    method: 'delete',
    executeOnMount: false,
  });

  const handleDetail = () => {
    navigate(`/donate/${accountInfo.autoDonationId}`);
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
      return;
    }

    try {
      if (modalState.isConfirmAction) {
        // 활성화/비활성화 처리
        await toggleActive();

        if (onStatusChange) {
          await onStatusChange();
        }
        setModalState({ ...modalState, isOpen: false });
        onClose();
      } else {
        // 삭제 처리
        await deleteDonation();

        if (onStatusChange) {
          await onStatusChange();
        }
        setModalState({ ...modalState, isOpen: false });
        onClose();
      }
    } catch (error) {
      // 오류 처리
    }
  };

  const handleModalClose = () => {
    setModalState({ ...modalState, isOpen: false });
  };

  const menuItemClass =
    'w-full py-2 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white transition-colors duration-200 rounded-lg';

  if (toggleLoading || deleteLoading) return <div>처리 중...</div>;
  if (toggleError || deleteError) return <div>오류가 발생했습니다.</div>;

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
          <AccountSettingModal
            onClose={() => setIsSettingModalOpen(false)}
            accountInfo={{
              bankName: accountInfo.bankName,
              balance: accountInfo.balance,
              paymentUnit: accountInfo.paymentUnit,
              paymentFrequency: accountInfo.paymentFrequency,
              paymentPurpose: accountInfo.paymentPurpose,
              paymentAmount: accountInfo.paymentAmount,
              autoDonationId: accountInfo.autoDonationId,
              accountNo: accountInfo.accountNo,
            }}
          />,
          document.body,
        )}
      {modalState.isOpen && (
        <Modal
          mainMessage={modalState.mainMessage}
          detailMessage={modalState.detailMessage}
          onClose={handleModalClose}
          onConfirm={handleModalConfirm}
          confirmText='확인'
        />
      )}
    </>
  );
};
