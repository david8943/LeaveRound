import { Link } from "react-router-dom";
import AddAccountIcon from "@/assets/icons/AddAccount.svg";
import ArrowRightIcon from "@/assets/icons/ArrowRight.svg";
import tossIcon from "@/assets/icons/toss.svg";
import AccountPreview from "@/components/Account/AccountPreview";

const MainPage: React.FC = () => {
  return (
    <div className="px-[32px] pt-[98px] pb-[95px]">
      {/* 총 기부금액 */}
      <p className="text-detail text-right">총 기부금액</p>
      <p className="text-[36px] text-right mt-[12px] font-heading">17,450,405 원</p>

      {/* 자동 기부 등록된 계좌 */}
      <div className="flex justify-between items-center mt-[45px]">
        <p className="text-body">자동 기부 등록된 계좌</p>
        <Link to="/manage" className="cursor-pointer ml-2">
          <img src={AddAccountIcon} alt="Add Account" />
        </Link>
        <div className="flex-grow flex justify-end">
          <Link to="/:userId/donate" className="cursor-pointer">
            <img src={ArrowRightIcon} alt="계좌 관리로 이동" />
          </Link>
        </div>
      </div>

      <div className="mt-[16px] space-y-[16px]">
        <AccountPreview
          bankIcon={tossIcon}
          bankName="토스뱅크"
          accountNumber="112398849502"
          balance={1798493}
          accountStatus="AUTO_ENABLED"
        />
        <AccountPreview
          bankIcon={tossIcon}
          bankName="토스뱅크"
          accountNumber="112398849502"
          balance={1798493}
          accountStatus="AUTO_PAUSED"
        />
        <AccountPreview
          bankIcon={tossIcon}
          bankName="토스뱅크"
          accountNumber="112398849502"
          balance={1798493}
          accountStatus="AUTO_PAUSED"
        />
      </div>

      {/* 등록되지 않은 계좌 */}
      <p className="mt-[32px] text-body">등록되지 않은 계좌</p>
      <div className="mt-[16px] space-y-[16px]">
        <AccountPreview
          bankIcon={tossIcon}
          bankName="신한은행"
          accountNumber="123456789012"
          balance={352000}
          accountStatus="AUTO_DISABLED"
        />
        <AccountPreview
          bankIcon={tossIcon}
          bankName="신한은행"
          accountNumber="123456789012"
          balance={352000}
          accountStatus="AUTO_DISABLED"
        />
        <AccountPreview
          bankIcon={tossIcon}
          bankName="신한은행"
          accountNumber="123456789012"
          balance={352000}
          accountStatus="AUTO_DISABLED"
        />
        <AccountPreview
          bankIcon={tossIcon}
          bankName="신한은행"
          accountNumber="123456789012"
          balance={352000}
          accountStatus="AUTO_DISABLED"
        />
      </div>
    </div>
  );
};

export default MainPage;
