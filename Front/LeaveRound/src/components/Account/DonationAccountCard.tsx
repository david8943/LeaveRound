import { useState, useRef, useEffect } from 'react';
import { tossIcon } from '@/assets/aseets';
import bracketsIcon from '@/assets/icons/brackets.svg';
import menuIcon from '@/assets/icons/menu.svg';
import { AccountMenu } from '@/components/Account/AccountMenu';
import { AccountSettingModal } from './AccountSettingModal';
import { createPortal } from 'react-dom';
import useAxios from '@/hooks/useAxios';
import { API } from '@/constants/url';

// 타입 정의
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

// 전역 상태를 관리하기 위한 변수
let activeMenuId: string | null = null;
const menuCloseListeners: Map<string, () => void> = new Map();

interface DonationAccountCardProps {
  accountInfo: {
    bankIcon?: string;
    bankName: string;
    accountNumber: string;
    balance: number;
    autoDonation?: 'active' | 'inactive' | 'unregistered';
    paymentUnit?: DonationAmount;
    paymentFrequency?: DonationTime;
    paymentPurpose?: string;
    paymentAmount?: number;
    autoDonationId?: number;
  };
  id: string;
  userId: string;
  onStatusChange?: () => void;
  onDelete?: () => void;
}

export function DonationAccountCard({ accountInfo, id, userId, onStatusChange, onDelete }: DonationAccountCardProps) {
  const [isExpanded, setIsExpanded] = useState(false);
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [isSettingModalOpen, setIsSettingModalOpen] = useState(false);
  const [totalDonationAmount, setTotalDonationAmount] = useState(0);
  const menuRef = useRef<HTMLDivElement>(null);
  const buttonRef = useRef<HTMLButtonElement>(null);

  // 로컬 상태로 계좌 정보 관리
  const [localAccountInfo, setLocalAccountInfo] = useState(accountInfo);

  // 자동기부 상세 내역 가져오기
  const { response: donationHistoryResponse } = useAxios({
    url: accountInfo.autoDonationId ? API.autoDonation.detail(accountInfo.autoDonationId.toString()) : '',
    method: 'get',
    executeOnMount: !!accountInfo.autoDonationId,
  });

  // 기부 내역이 변경될 때마다 총 기부 금액 계산
  useEffect(() => {
    if (donationHistoryResponse?.result?.autoDonationInfos) {
      const totalAmount = donationHistoryResponse.result.autoDonationInfos.reduce(
        (sum: number, info: any) => sum + info.transactionBalance,
        0,
      );
      setTotalDonationAmount(totalAmount);
    }
  }, [donationHistoryResponse]);

  // 로컬 스토리지에서 데이터 읽어서 계좌 정보 업데이트하는 함수
  const updateAccountInfoFromStorage = (autoDonationId?: number) => {
    if (!autoDonationId) return;

    const autoDonationKey = `autoDonation_${autoDonationId}`;
    const savedDataStr = localStorage.getItem(autoDonationKey);

    if (savedDataStr) {
      try {
        const savedData = JSON.parse(savedDataStr);

        // 로컬 계좌 정보 업데이트
        setLocalAccountInfo((prev) => ({
          ...prev,
          paymentUnit: savedData.sliceMoney || prev.paymentUnit,
          paymentFrequency: savedData.donationTime || prev.paymentFrequency,
          paymentPurpose: savedData.purpose || prev.paymentPurpose,
        }));
      } catch (error) {
        console.error('자동기부 정보 로딩 오류:', error);
      }
    }
  };

  // 로컬 스토리지에서 데이터 불러오기
  useEffect(() => {
    updateAccountInfoFromStorage(accountInfo.autoDonationId);
  }, [accountInfo.autoDonationId]);

  const {
    bankName,
    accountNumber,
    balance,
    bankIcon = tossIcon,
    autoDonation = 'unregistered',
    paymentUnit,
    paymentFrequency,
    paymentPurpose,
    paymentAmount,
    autoDonationId,
  } = localAccountInfo;

  // 금액 포맷팅 (1,000단위 콤마)
  const formattedBalance = balance.toLocaleString('ko-KR');
  const formattedPaymentAmount = totalDonationAmount.toLocaleString('ko-KR');

  // 메뉴 닫기 함수
  const closeMenu = () => {
    setIsMenuOpen(false);
    if (activeMenuId === id) {
      activeMenuId = null;
    }
  };

  // 컴포넌트 마운트 시 메뉴 닫기 리스너 등록
  useEffect(() => {
    menuCloseListeners.set(id, closeMenu);

    return () => {
      menuCloseListeners.delete(id);
      if (activeMenuId === id) {
        activeMenuId = null;
      }
    };
  }, [id]);

  // 메뉴 외부 클릭 감지를 위한 이벤트 리스너
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (
        isMenuOpen &&
        menuRef.current &&
        buttonRef.current &&
        !menuRef.current.contains(event.target as Node) &&
        !buttonRef.current.contains(event.target as Node)
      ) {
        closeMenu();
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [isMenuOpen]);

  const handleMenuClick = () => {
    // 다른 열린 메뉴가 있으면 닫기
    if (activeMenuId && activeMenuId !== id) {
      const closeOtherMenu = menuCloseListeners.get(activeMenuId);
      if (closeOtherMenu) {
        closeOtherMenu();
      }
    }

    // 현재 메뉴 상태 토글
    setIsMenuOpen(!isMenuOpen);
    activeMenuId = isMenuOpen ? null : id;
  };

  const handleModify = () => {
    closeMenu();
    setIsSettingModalOpen(true);
  };

  const handleDelete = () => {
    closeMenu();
    if (onDelete) {
      onDelete();
    }
  };

  // 자동기부 상태에 따른 스타일 결정
  const cardStyle = () => {
    switch (autoDonation) {
      case 'active':
        return 'border-primary bg-[rgba(255,217,95,0.2)]';
      case 'inactive':
        return 'border-primary-light bg-[rgba(255,239,169,0.2)]';
      case 'unregistered':
      default:
        return 'border-gray-300 bg-[rgba(224,224,224,0.2)]';
    }
  };

  const toggleExpand = () => {
    setIsExpanded(!isExpanded);
  };

  const getDonationAmountLabel = (value: DonationAmount) => {
    const amounts: { [key in DonationAmount]: string } = {
      FIVE_HUNDRED: '500원',
      ONE_THOUSAND: '1,000원',
      ONE_THOUSAND_FIVE_HUNDRED: '1,500원',
      TWO_THOUSAND: '2,000원',
      TWO_THOUSAND_FIVE_HUNDRED: '2,500원',
      THREE_THOUSAND: '3,000원',
      THREE_THOUSAND_FIVE_HUNDRED: '3,500원',
      FOUR_THOUSAND: '4,000원',
      FIVE_THOUSAND: '5,000원',
    };
    return amounts[value] || value;
  };

  const getDonationTimeLabel = (value: DonationTime) => {
    const times: { [key in DonationTime]: string } = {
      ONE_DAY: '1일',
      TWO_DAY: '2일',
      THREE_DAY: '3일',
      FOUR_DAY: '4일',
      FIVE_DAY: '5일',
      SIX_DAY: '6일',
      SEVEN_DAY: '7일',
    };
    return times[value] || value;
  };

  // 설정 모달 닫힐 때 호출되는 함수
  const handleSettingModalClose = () => {
    setIsSettingModalOpen(false);
    updateAccountInfoFromStorage(autoDonationId);
  };

  return (
    <>
      <div className={`flex flex-col mx-4 p-4 mb-[12px] rounded-[8px] border ${cardStyle()}`}>
        <div className='relative ml-[1rem]'>
          {/* 계좌 정보 */}
          <div className='flex items-center gap-[21px] flex-shrink-0'>
            <div className='flex items-center justify-center'>
              <img src={bankIcon} alt={`${bankName} 아이콘`} className='w-9 h-9' />
            </div>
            <div className='flex flex-col flex-grow'>
              <div className='flex flex-col'>
                <span className='text-[var(--text-deepgray)] text-[0.75rem]'>
                  {bankName} {accountNumber}
                </span>
              </div>
              <div className='mt-1'>
                <span className='text-[20px] font-semibold'>{formattedBalance}원</span>
              </div>
            </div>

            {/* 메뉴 버튼 (오른쪽 상단) */}
            <div className='absolute top-0 right-0 '>
              <button ref={buttonRef} onClick={handleMenuClick} className='relative'>
                <img src={menuIcon} alt='메뉴' className='w-5 h-5' />
              </button>
              {/* 메뉴 드롭다운 */}
              {isMenuOpen && (
                <div ref={menuRef} className='absolute top-full right-0 -mt-1 z-20'>
                  <AccountMenu
                    onClose={closeMenu}
                    accountNumber={accountNumber}
                    userId={userId}
                    accountInfo={{
                      bankName,
                      balance,
                      paymentUnit,
                      paymentFrequency,
                      paymentPurpose,
                      paymentAmount,
                      autoDonationId,
                    }}
                    onModify={handleModify}
                    isPaused={autoDonation === 'inactive'}
                    onStatusChange={onStatusChange}
                    onDelete={handleDelete}
                  />
                </div>
              )}
            </div>
          </div>
        </div>

        {/* 상세 정보 (펼쳐졌을 때만 표시) */}
        {isExpanded && (
          <div className='mt-4 pt-4 border-t border-gray-500'>
            <div className='grid grid-cols-2 gap-y-2 ml-3 mr-3'>
              <div className='text-gray-700'>기부 금액 단위</div>
              <div className='text-right'>{paymentUnit && getDonationAmountLabel(paymentUnit)}</div>

              <div className='text-gray-700'>기부 주기</div>
              <div className='text-right'>{paymentFrequency && getDonationTimeLabel(paymentFrequency)}</div>

              <div className='text-gray-700'>기부하는 곳</div>
              <div className='text-right'>{paymentPurpose}</div>

              <div className='text-gray-700 font-semibold'>기부한 금액</div>
              <div className='text-right font-semibold'>{formattedPaymentAmount}원</div>
            </div>
          </div>
        )}

        {/* 펼치기/접기 버튼 */}
        <div className='flex justify-center mt-2'>
          <button onClick={toggleExpand}>
            <img
              src={bracketsIcon}
              alt={isExpanded ? '접기' : '펼치기'}
              className={`w-3 h-3 transition-transform ${isExpanded ? 'rotate-180' : ''}`}
            />
          </button>
        </div>
      </div>

      {/* 설정 모달 */}
      {isSettingModalOpen &&
        createPortal(
          <AccountSettingModal
            onClose={handleSettingModalClose}
            onSuccess={() => {
              handleSettingModalClose(); // 모달 닫기
              if (onStatusChange) onStatusChange(); // 외부에서 계좌 목록 새로고침 트리거
            }}
            accountInfo={{
              bankName,
              balance,
              paymentUnit,
              paymentFrequency,
              paymentPurpose,
              paymentAmount,
              autoDonationId,
              accountNo: accountNumber,
            }}
          />,
          document.body,
        )}
    </>
  );
}
