import tossIcon from "@/assets/icons/toss.svg";
import AccountPreview from "@/components/Account/AccountPreview";
import TitleLayout from "@/components/layout/TitleLayout";

const ManageAccountsPage: React.FC = () => {
  return (
    <TitleLayout title="계좌 관리">
      {/* 총 기부금액 */}
      <div className="px-[32px] pt-[36px]">
        <p className="text-detail text-right">총 기부금액</p>
        <p className="text-[36px] text-right mt-[12px] font-heading">17,450,405 원</p>

        {/* 전체 계좌 리스트 */}
        <p className="mt-[45px] text-body">전체 계좌</p>

        <div className="mt-[16px] space-y-[16px] pb-[5rem]">
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
          <AccountPreview
            bankIcon={tossIcon}
            bankName="신한은행"
            accountNumber="123456789012"
            balance={352000}
            accountStatus="AUTO_DISABLED"
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
          <AccountPreview
            bankIcon={tossIcon}
            bankName="신한은행"
            accountNumber="123456789012"
            balance={352000}
            accountStatus="AUTO_DISABLED"
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
      </div>
    </TitleLayout>
  );
};

export default ManageAccountsPage;
