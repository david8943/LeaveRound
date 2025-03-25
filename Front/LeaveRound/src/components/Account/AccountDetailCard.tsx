interface AccouontDeatilCardProps {
  accountDetailInfo: {
    userId: string;
    accountId: string;
    donateDate: string;
    donateAmount: number;
    donatePurpose: string;
  };
}

export const AccountDetailCard = ({ accountDetailInfo }: AccouontDeatilCardProps) => {
  return (
    <div className='flex flex-col mx-8 p-[16px_16px] mb-[12px] items-start gap-[10px] flex-shrink-0 rounded-[8px] bg-[rgba(255,239,169,0.2)] border border-primary-light w-[345px] h-[80px]'>
      <div className='flex justify-between w-full'>
        <span className='text-detail text-gray-700'>{accountDetailInfo.donateDate}</span>
      </div>
      <div className='flex justify-between w-full'>
        <span>{accountDetailInfo.donateAmount}원</span>
        <span>{accountDetailInfo.donatePurpose}</span>
      </div>
    </div>
  );
};
