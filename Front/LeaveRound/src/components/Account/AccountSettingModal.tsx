import { cardIcon } from '@/assets/aseets';
import AccountModal from './AccountModal';
import { AccountCard } from '../AccountCard';
import { SettingButton } from './SettingButton';
import { useState, useEffect } from 'react';
import { BasicButton } from '../common/BasicButton';
import { ConfirmModal } from './ConfirmModal';
import { OrganizationModal } from './OrganizationModal';
import { createPortal } from 'react-dom';

interface DonationSettings {
  amount: number;
  frequency: 'daily' | 'weekly' | 'monthly';
}

const AMOUNT_OPTIONS = [10, 100, 500, 1000];
const FREQUENCY_OPTIONS = [
  { value: 'daily', label: '매일' },
  { value: 'weekly', label: '매주' },
  { value: 'monthly', label: '매월' },
];

interface AccountSettingModalProps {
  onClose: () => void;
  accountInfo: {
    bankName: string;
    balance: number;
    paymentUnit?: string;
    paymentFrequency?: string;
    paymentPurpose?: string;
    paymentAmount?: number;
  };
}

export const AccountSettingModal: React.FC<AccountSettingModalProps> = ({ onClose, accountInfo }) => {
  const [_selectedOrganizationId, setSelectedOrganizationId] = useState<number>();
  const [settings, setSettings] = useState<DonationSettings>({
    amount: 10,
    frequency: 'daily',
  });
  const [isConfirmModalOpen, setIsConfirmModalOpen] = useState(false);
  const [isOrganizationModalOpen, setIsOrganizationModalOpen] = useState(false);
  const [selectedPurpose, setSelectedPurpose] = useState(accountInfo.paymentPurpose || '기부처 랜덤 선택');

  // 계좌 정보가 변경될 때마다 설정값 업데이트
  useEffect(() => {
    if (accountInfo) {
      const amount = parseInt(accountInfo.paymentUnit?.replace(/[^0-9]/g, '') || '10');
      const frequency =
        accountInfo.paymentFrequency === '매일'
          ? 'daily'
          : accountInfo.paymentFrequency === '매주'
            ? 'weekly'
            : 'monthly';

      setSettings({
        amount,
        frequency: frequency as 'daily' | 'weekly' | 'monthly',
      });
    }
  }, [accountInfo]);

  const handleClose = () => {
    onClose();
  };

  const handleSave = () => {
    setIsConfirmModalOpen(true);
  };

  const handleSelectOrganization = () => {
    setIsOrganizationModalOpen(true);
  };

  const handleOrganizationSave = (purpose: string) => {
    setSelectedPurpose(purpose === '리브라운드가 정해주세요!' ? '기부처 랜덤 선택' : purpose);
    setIsOrganizationModalOpen(false);
  };

  return (
    <>
      <AccountModal onClose={handleClose}>
        <div className='flex flex-col items-center mb-[2rem]'>
          <p className='font-bold'>자동기부 계좌설정</p>
          <img src={cardIcon} alt='카드' className='my-[0.5rem] w-[78px] h-[78px]' />
          <p className='text-detail'>자동기부 계좌를 설정합니다.</p>
        </div>
        <div className='w-[120%] mb-[1rem]'>
          <AccountCard
            accountInfo={{
              accountNumber: '1234567890123456',
              bankName: accountInfo.bankName,
              balance: accountInfo.balance,
              autoDonation: 'active',
            }}
          />
          <div className='flex flex-col items-start ml-[2.5rem] mt-[2rem]'>
            <p>얼마 단위로 기부할까요?</p>
            <div className='flex flex-wrap gap-2'>
              {AMOUNT_OPTIONS.map((amount) => (
                <SettingButton
                  key={amount}
                  text={`${amount}원`}
                  isSelected={settings.amount === amount}
                  onClick={() => setSettings((prev) => ({ ...prev, amount }))}
                />
              ))}
            </div>
            <p>얼마 주기로 기부할까요?</p>
            <div className='flex flex-wrap gap-2'>
              {FREQUENCY_OPTIONS.map(({ value, label }) => (
                <SettingButton
                  key={value}
                  text={label}
                  isSelected={settings.frequency === value}
                  onClick={() =>
                    setSettings((prev) => ({ ...prev, frequency: value as 'daily' | 'weekly' | 'monthly' }))
                  }
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
        <div className='flex gap-2'>
          <BasicButton text='설정하기' onClick={handleSave} className='w-[313px] h-[48px]' />
        </div>
      </AccountModal>
      {isConfirmModalOpen &&
        createPortal(
          <ConfirmModal
            onClose={() => {
              setIsConfirmModalOpen(false);
              handleClose();
            }}
            amount={settings.amount}
            frequency={FREQUENCY_OPTIONS.find((opt) => opt.value === settings.frequency)?.label || '매일'}
            purpose={selectedPurpose}
          />,
          document.body,
        )}
      {isOrganizationModalOpen &&
        createPortal(
          <OrganizationModal
            onClose={() => setIsOrganizationModalOpen(false)}
            onSave={handleOrganizationSave}
            selectedId={setSelectedOrganizationId}
            currentPurpose={selectedPurpose === '기부처 랜덤 선택' ? '리브라운드가 정해주세요!' : selectedPurpose}
          />,
          document.body,
        )}
    </>
  );
};
