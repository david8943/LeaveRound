import { tossIcon } from '@/assets/aseets';

interface AccountCardProps {
  accountInfo: {
    bankIcon?: string;
    bankName: string;
    accountNumber: string;
    balance: number;
    autoDonation?: 'active' | 'inactive' | 'unregistered';
  };
}

export function AccountCard({ accountInfo }: AccountCardProps) {
  const { bankName, accountNumber, balance, bankIcon = tossIcon, autoDonation = 'unregistered' } = accountInfo;

  // 금액 포맷팅 (1,000단위 콤마)
  const formattedBalance = balance.toLocaleString('ko-KR');

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

  return (
    <div
      className={`flex mx-8 p-[16px_8px_16px_32px] mb-[12px] items-center gap-[21px] flex-shrink-0 rounded-[8px] border ${cardStyle()}`}
    >
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
    </div>
  );
}
