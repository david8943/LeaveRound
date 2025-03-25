import { useState, useRef, useEffect } from 'react';
import { tossIcon } from '@/assets/aseets';
import bracketsIcon from '@/assets/icons/brackets.svg';
import menuIcon from '@/assets/icons/menu.svg';
import { AccountMenu } from '@/components/Account/AccountMenu';

// 전역 상태를 관리하기 위한 변수
let activeMenuId: string | null = null;
const menuCloseListeners: Map<string, () => void> = new Map();

interface DonationAccountCardProps {
  accountInfo: {
    userId: string;
    bankIcon?: string;
    bankName: string;
    accountNumber: string;
    balance: number;
    autoDonation?: 'active' | 'inactive' | 'unregistered';
    paymentUnit?: string;
    paymentFrequency?: string;
    paymentPurpose?: string;
    paymentAmount?: number;
  };
  id: string; // 카드 식별을 위한 고유 ID
}

export function DonationAccountCard({ accountInfo, id }: DonationAccountCardProps) {
  const [isExpanded, setIsExpanded] = useState(false);
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const menuRef = useRef<HTMLDivElement>(null);
  const buttonRef = useRef<HTMLButtonElement>(null);

  const {
    userId,
    bankName,
    accountNumber,
    balance,
    bankIcon = tossIcon,
    autoDonation = 'unregistered',
    paymentUnit = '10원',
    paymentFrequency = '매일',
    paymentPurpose = '싸피복지관',
    paymentAmount = 1330,
  } = accountInfo;

  // 금액 포맷팅 (1,000단위 콤마)
  const formattedBalance = balance.toLocaleString('ko-KR');
  const formattedPaymentAmount = paymentAmount.toLocaleString('ko-KR');

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
    console.log('메뉴 클릭');
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

  return (
    <div className={`flex flex-col mx-8 p-4 mb-[12px] rounded-[8px] border ${cardStyle()}`}>
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
          <div className='absolute top-0 right-0'>
            <button ref={buttonRef} onClick={handleMenuClick} className='relative'>
              <img src={menuIcon} alt='메뉴' className='w-5 h-5' />
            </button>
            {/* 메뉴 드롭다운 */}
            {isMenuOpen && (
              <div ref={menuRef} className='absolute top-full right-0 -mt-1 z-20'>
                <AccountMenu onClose={closeMenu} accountNumber={accountNumber} userId={userId} />
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
            <div className='text-right'>{paymentUnit}</div>

            <div className='text-gray-700'>기부 주기</div>
            <div className='text-right'>{paymentFrequency}</div>

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
  );
}
