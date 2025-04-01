import React from "react";
import TitleLayout from "@/components/layout/TitleLayout";
import AccountPreview from "@/components/Account/AccountPreview";
import tossIcon from '@/assets/icons/toss.svg';

const ManageAccountsPage: React.FC = () => {
  return (
    <TitleLayout title="계좌 관리">
      <div className="mt-[16px] space-y-[16px]">
        <AccountPreview
          bankIcon={tossIcon}
          bankName="토스뱅크"
          accountNumber="112398849502"
          balance={1798493}
          accountStatus="AUTO_ENABLED"
          showButton
        />
        <AccountPreview
          bankIcon={tossIcon}
          bankName="토스뱅크"
          accountNumber="112398849502"
          balance={1798493}
          accountStatus="AUTO_PAUSED"
          showButton
        />
        <AccountPreview
          bankIcon={tossIcon}
          bankName="신한은행"
          accountNumber="123456789012"
          balance={352000}
          accountStatus="AUTO_DISABLED"
          showButton
        />
      </div>
    </TitleLayout>
  );
};

export default ManageAccountsPage;
