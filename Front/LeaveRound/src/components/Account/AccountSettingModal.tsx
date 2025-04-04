import { cardIcon } from '@/assets/aseets';
import AccountModal from './AccountModal';
import { AccountCard } from '../AccountCard';
import { SettingButton } from './SettingButton';
import { useState } from 'react';
import { BasicButton } from '../common/BasicButton';
import { ConfirmModal } from './ConfirmModal';
import { OrganizationModal } from './OrganizationModal';
import { createPortal } from 'react-dom';
import useAxios from '@/hooks/useAxios';
import { API } from '@/constants/url';

interface ApiResponse {
  isSuccess: boolean;
  code: string;
  message: string;
  result: null;
}

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
  { value: 'FOUR_THOUSAND_FIVE_HUNDRED', label: '4,500원' },
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
  const [settings, setSettings] = useState<DonationSettings>({
    amount: accountInfo.paymentUnit || 'ONE_THOUSAND',
    frequency: accountInfo.paymentFrequency || 'ONE_DAY',
  });
  const [isConfirmModalOpen, setIsConfirmModalOpen] = useState(false);
  const [isOrganizationModalOpen, setIsOrganizationModalOpen] = useState(false);
  const [selectedPurpose, setSelectedPurpose] = useState(accountInfo.paymentPurpose || '');
  const [organizationProjectId, setOrganizationProjectId] = useState<number>(1); // API 연동 시 실제 프로젝트 ID로 변경 필요

  const {
    response: updateResponse,
    loading: updateLoading,
    error: updateError,
    refetch: updateDonation,
  } = useAxios<ApiResponse>({
    url: accountInfo.autoDonationId ? API.autoDonation.update(accountInfo.autoDonationId.toString()) : '',
    method: 'put',
    executeOnMount: false,
  });

  const handleClose = () => {
    onClose();
  };

  const handleSave = () => {
    setIsConfirmModalOpen(true);
  };

  const handleConfirm = async () => {
    if (!accountInfo.autoDonationId) {
      console.error('자동기부 ID가 없습니다.');
      return;
    }

    const updateData = {
      accountNo: accountInfo.accountNo,
      sliceMoney: settings.amount,
      donationTime: settings.frequency,
      bankName: accountInfo.bankName,
      organizationProjectId: organizationProjectId,
    };

    try {
      await updateDonation(updateData);

      if (updateResponse?.isSuccess) {
        onClose();
      } else {
        console.error('자동기부 설정 업데이트 실패:', updateResponse?.message);
      }
    } catch (error) {
      console.error('자동기부 설정 업데이트 중 오류 발생:', error);
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

  if (updateLoading) return <div>처리 중...</div>;
  if (updateError) return <div>오류가 발생했습니다.</div>;

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
          <BasicButton text='설정하기' onClick={handleSave} className='w-[313px] h-[48px]' disabled={updateLoading} />
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
          <div className='relative z-[9999]'>
            <OrganizationModal
              onClose={() => setIsOrganizationModalOpen(false)}
              onSave={handleOrganizationSave}
              currentPurpose={selectedPurpose === '기부처 랜덤 선택' ? '리브라운드가 정해주세요!' : selectedPurpose}
            />
          </div>,
          document.body,
        )}
    </>
  );
};
