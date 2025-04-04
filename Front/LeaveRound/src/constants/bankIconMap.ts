import tossIcon from "@/assets/icons/toss.svg";
import kakaoIcon from "@/assets/icons/kakao.svg";
import kbIcon from "@/assets/icons/kb.svg";
import wooriIcon from "@/assets/icons/woori.svg";
import nhIcon from "@/assets/icons/nh.svg";
import ssafyIcon from "@/assets/icons/ssafy.svg"; // 기본 아이콘

export const BANK_ICON_MAP: Record<string, string> = {
    "토스": tossIcon,
    "카카오뱅크": kakaoIcon,
    "국민은행": kbIcon,
    "우리은행": wooriIcon,
    "농협은행": nhIcon,
};

export const getBankIcon = (bankName: string): string => {
    return BANK_ICON_MAP[bankName] || ssafyIcon; // fallback 아이콘
};
