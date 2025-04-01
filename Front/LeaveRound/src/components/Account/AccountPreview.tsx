import React from "react";
import MinusIcon from "@/assets/icons/minus.svg";
import PlusIcon from "@/assets/icons/plus.svg";

type AccountStatus = 'AUTO_ENABLED' | 'AUTO_PAUSED' | 'AUTO_DISABLED';

interface AccountCardProps {
    bankIcon: string;
    bankName: string;
    accountNumber: string;
    balance: number;
    accountStatus: AccountStatus;
    showButton?: boolean;
}

const AccountCard: React.FC<AccountCardProps> = ({
    bankIcon,
    bankName,
    accountNumber,
    balance,
    accountStatus,
    showButton = false,
}) => {
    const formattedBalance = balance.toLocaleString() + "원";

    // 상태별 스타일 및 아이콘 설정
    let cardStyle = "";
    let icon = null;

    if (accountStatus === "AUTO_ENABLED") {
        cardStyle = "bg-[#ffd95f] bg-opacity-20 border-[#ffd95f]";
        icon = MinusIcon;
    } else if (accountStatus === "AUTO_PAUSED") {
        cardStyle = "bg-[#ffefa9] bg-opacity-20 border-[#ffefa9]";
        icon = MinusIcon;
    } else {
        cardStyle = "bg-[#E0E0E0] bg-opacity-20 border-[#E0E0E0]";
        icon = PlusIcon;
    }

    return (
        <div className={`w-[348px] h-[86px] border ${cardStyle} rounded-lg flex items-center relative`}>
            <img src={bankIcon} alt="Bank Icon" className="w-[34px] h-[34px] ml-[32px]" />
            <div className="ml-[21px] flex flex-col justify-center">
                <p className="text-deepgray text-detail">{`${bankName} ${accountNumber}`}</p>
                <p className="font-bold text-[20px]">{formattedBalance}</p>
            </div>
            {showButton && (
                <img src={icon} alt="Action Icon" className="absolute right-[36px] w-[24px] h-[24px]" />
            )}
        </div>
    );
};

export default AccountCard;
