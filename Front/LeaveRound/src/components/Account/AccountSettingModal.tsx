import { cardIcon } from '@/assets/aseets';
import AccountModal from './AccountModal';
import { AccountCard } from '../AccountCard';
import { SettingButton } from './SettingButton';
import { useState } from 'react';
import { BasicButton } from '../common/BasicButton';
import { ConfirmModal } from './ConfirmModal';
import { OrganizationModal } from './OrganizationModal';
import { createPortal } from 'react-dom';
import { API } from '@/constants/url';
import api from '@/services/api';

// 쿠키에서 값을 가져오는 함수
const getCookie = (name: string) => {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop()?.split(';').shift();
  return null;
};

// API enum 값과 일치하는 기부 금액 단위
const DONATION_AMOUNTS = [
  { value: 'FIVE_HUNDRED', label: '500원' },
  { value: 'ONE_THOUSAND', label: '1,000원' },
  { value: 'ONE_THOUSAND_FIVE_HUNDRED', label: '1,500원' },
  { value: 'TWO_THOUSAND', label: '2,000원' },
  { value: 'TWO_THOUSAND_FIVE_HUNDRED', label: '2,500원' },
  { value: 'THREE_THOUSAND', label: '3,000원' },
  { value: 'THREE_THOUSAND_FIVE_HUNDRED', label: '3,500원' },
  { value: 'FOUR_THOUSAND', label: '4,000원' },
  { value: 'FIVE_THOUSAND', label: '5,000원' },
] as const;

// API enum 값과 일치하는 기부 주기
const DONATION_TIME = [
  { value: 'ONE_DAY', label: '1일' },
  { value: 'TWO_DAY', label: '2일' },
  { value: 'THREE_DAY', label: '3일' },
  { value: 'FOUR_DAY', label: '4일' },
  { value: 'FIVE_DAY', label: '5일' },
  { value: 'SIX_DAY', label: '6일' },
  { value: 'SEVEN_DAY', label: '7일' },
] as const;

type DonationAmount = (typeof DONATION_AMOUNTS)[number]['value'];
type DonationTime = (typeof DONATION_TIME)[number]['value'];

interface DonationSettings {
  amount: DonationAmount;
  frequency: DonationTime;
}

interface AccountSettingModalProps {
  onClose: () => void;
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
}

export const AccountSettingModal: React.FC<AccountSettingModalProps> = ({ onClose, accountInfo }) => {
  const [_selectedOrganizationId, _setSelectedOrganizationId] = useState<number>();
  const [settings, setSettings] = useState<DonationSettings>({
    amount: accountInfo.paymentUnit || 'ONE_THOUSAND',
    frequency: accountInfo.paymentFrequency || 'ONE_DAY',
  });
  const [isConfirmModalOpen, setIsConfirmModalOpen] = useState(false);
  const [isOrganizationModalOpen, setIsOrganizationModalOpen] = useState(false);
  const [selectedPurpose, setSelectedPurpose] = useState(accountInfo.paymentPurpose || '');
  const [organizationProjectId, setOrganizationProjectId] = useState<number | undefined>(undefined);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleClose = () => {
    onClose();
  };

  const handleSave = () => {
    setIsConfirmModalOpen(true);
  };

  const handleConfirm = async () => {
    if (!accountInfo.autoDonationId) {
      return;
    }

    if (!organizationProjectId) {
      alert('기부처를 선택해주세요.');
      return;
    }

    const updateData = {
      accountNo: accountInfo.accountNo,
      sliceMoney: settings.amount,
      donationTime: settings.frequency,
      bankName: accountInfo.bankName,
      organizationProjectId: organizationProjectId,
    };

    setIsLoading(true);
    setError(null);

    try {
      // 직접 API 호출 - 인증 토큰 포함
      const accessToken = getCookie('access_token');

      // axios 옵션 설정
      const axiosConfig = {
        headers: {
          'Content-Type': 'application/json',
          Authorization: accessToken ? `Bearer ${accessToken}` : '',
        },
        withCredentials: true,
      };

      // API 요청 시도
      const response = await api.put(
        API.autoDonation.update(accountInfo.autoDonationId.toString()),
        updateData,
        axiosConfig,
      );

      if (response.data.isSuccess) {
        onClose();
      } else {
        setError(response.data.message || '업데이트에 실패했습니다.');
      }
    } catch (err) {
      const error = err as any;

      if (error.response) {
        const errorMsg = error.response.data?.message || '업데이트 중 오류가 발생했습니다.';
        setError(errorMsg);
      } else if (error.request) {
        setError('서버에서 응답이 없습니다.');
      } else {
        setError('요청을 보낼 수 없습니다. 네트워크 연결을 확인해주세요.');
      }
    } finally {
      setIsLoading(false);
    }
  };

  const handleSelectOrganization = () => {
    setIsOrganizationModalOpen(true);
  };

  const handleOrganizationSave = (purpose: string, projectId?: number) => {
    setSelectedPurpose(purpose === '리브라운드가 정해주세요!' ? '기부처 랜덤 선택' : purpose);
    if (projectId) {
      setOrganizationProjectId(projectId);
    }
    setIsOrganizationModalOpen(false);
  };

  const handleSelectedId = (projectId: number) => {
    setOrganizationProjectId(projectId);
  };

  // 기부처가 선택되었는지 확인
  const isPurposeSelected = selectedPurpose !== '' && organizationProjectId !== undefined;

  if (isLoading) return <div>처리 중...</div>;
  if (error) return <div>오류가 발생했습니다: {error}</div>;

  return (
    <>
      <AccountModal onClose={handleClose}>
        <div className='flex flex-col items-center mb-[0.5rem] relative z-50'>
          <p className='font-bold'>자동기부 계좌설정</p>
          <img src={cardIcon} alt='카드' className='my-[0.5rem] w-[78px] h-[78px]' />
        </div>
        <div className='w-[120%] mb-[1rem] relative z-50'>
          <AccountCard
            accountInfo={{
              accountNumber: accountInfo.accountNo || '',
              bankName: accountInfo.bankName,
              balance: accountInfo.balance,
              autoDonation: 'active',
            }}
          />
          <div className='flex flex-col items-start ml-[2.5rem] mt-[2rem]'>
            <p>얼마 단위로 기부할까요?</p>
            <div className='grid grid-cols-3 gap-x-2 gap-y-1'>
              {DONATION_AMOUNTS.map((amount) => (
                <SettingButton
                  key={amount.value}
                  text={amount.label}
                  isSelected={settings.amount === amount.value}
                  onClick={() => setSettings((prev) => ({ ...prev, amount: amount.value }))}
                />
              ))}
            </div>
            <p>얼마 주기로 기부할까요?</p>
            <div className='grid grid-cols-4 gap-x-2 gap-y-1'>
              {DONATION_TIME.slice(0, 4).map((time) => (
                <SettingButton
                  key={time.value}
                  text={time.label}
                  isSelected={settings.frequency === time.value}
                  onClick={() => setSettings((prev) => ({ ...prev, frequency: time.value }))}
                />
              ))}
            </div>
            <div className='grid grid-cols-3 gap-x-2 gap-y-1'>
              {DONATION_TIME.slice(4).map((time) => (
                <SettingButton
                  key={time.value}
                  text={time.label}
                  isSelected={settings.frequency === time.value}
                  onClick={() => setSettings((prev) => ({ ...prev, frequency: time.value }))}
                />
              ))}
            </div>
            <p>어디에 기부할까요?</p>
            <div className='flex items-center gap-2'>
              <div className='flex items-center px-[0.5rem] w-[200px] h-[30px] border-gray-300 border-[1px] rounded-full my-[0.5rem]'>
                <p className='flex justify-items-center'>{selectedPurpose}</p>
              </div>
              <button
                onClick={handleSelectOrganization}
                className='flex items-center justify-center w-[83px] h-[30px] bg-primary-light rounded-[4px] text-detail'
              >
                <p>지정하기</p>
                <p className='ml-[0.5rem]'>&gt;</p>
              </button>
            </div>

            <p className='text-detail text-gray-700 mb-[0.5rem]'>*기부처를 선택하지 않으면 랜덤으로 지정됩니다.</p>
          </div>
        </div>
        <div className='flex gap-2 relative z-50'>
          <BasicButton
            text='설정하기'
            onClick={handleSave}
            className='w-[313px] h-[48px]'
            disabled={isLoading || !isPurposeSelected}
          />
        </div>
      </AccountModal>
      {isConfirmModalOpen &&
        createPortal(
          <div className='relative z-[9999]'>
            <ConfirmModal
              onClose={() => setIsConfirmModalOpen(false)}
              onConfirm={handleConfirm}
              amount={parseInt(
                DONATION_AMOUNTS.find((opt) => opt.value === settings.amount)?.label.replace(/[^0-9]/g, '') || '0',
              )}
              frequency={DONATION_TIME.find((opt) => opt.value === settings.frequency)?.label || '1일'}
              purpose={selectedPurpose}
            />
          </div>,
          document.body,
        )}
      {isOrganizationModalOpen &&
        createPortal(
          <OrganizationModal
            onClose={() => setIsOrganizationModalOpen(false)}
            onSave={handleOrganizationSave}
            selectedId={handleSelectedId}
            currentPurpose={selectedPurpose === '기부처 랜덤 선택' ? '리브라운드가 정해주세요!' : selectedPurpose}
          />,
          document.body,
        )}
    </>
  );
};
